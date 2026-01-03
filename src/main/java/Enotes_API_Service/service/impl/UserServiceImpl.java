package Enotes_API_Service.service.impl;

import Enotes_API_Service.Dto.EmailRequest;
import Enotes_API_Service.Dto.PasswordChangeRequest;
import Enotes_API_Service.Dto.PasswordResetRequest;
import Enotes_API_Service.entity.User;
import Enotes_API_Service.exception.ResourceNotFoundException;
import Enotes_API_Service.repository.UserRepository;
import Enotes_API_Service.service.UserService;
import Enotes_API_Service.util.CommonUtil;
import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        User loggedInUser = CommonUtil.getLoggedInUser();
        if(!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), loggedInUser.getPassword())){
            throw new IllegalArgumentException("Old password is incorrect");
        }
        String encodePassword = passwordEncoder.encode(passwordChangeRequest.getNewPassword());
        loggedInUser.setPassword(encodePassword);
        userRepository.save(loggedInUser);
    }

    @Override
    public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception {
        User user = userRepository.findByEmail(email);
        if(ObjectUtils.isEmpty(user)){
            throw new ResourceNotFoundException("Invalid Email");
        }

        // Generate Unique Password Reset Token
        String passwordResetToken = UUID.randomUUID().toString();
        user.getStatus().setPasswordResetToken(passwordResetToken);
        User updatedUser = userRepository.save(user);
        String url = CommonUtil.getUrl(request);
        sendEmailRequest(updatedUser, url);
    }

    @Override
    public void verifyPasswordResetLink(Integer uid, String code) throws Exception {
        User user = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("Invalid User"));
        verifyPasswordResetCode(user.getStatus().getPasswordResetToken(), code);
    }

    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest) throws Exception {
        User user = userRepository.findById(passwordResetRequest.getUid()).orElseThrow(() -> new ResourceNotFoundException("Invalid User"));
        String encodePassword = passwordEncoder.encode(passwordResetRequest.getNewPassword());
        user.setPassword(encodePassword);
        user.getStatus().setPasswordResetToken(null);
        userRepository.save(user);

    }

    private void verifyPasswordResetCode(String existToken, String reqToken) {
        if(StringUtils.hasText(reqToken)){
            if(!StringUtils.hasText(existToken)){
                throw new IllegalArgumentException("Already Password Reset");
            }
            if(!existToken.equals(reqToken)){
                throw new IllegalArgumentException("Invalid URL");
            }
            
        }else{
            throw new IllegalArgumentException("Invalid Token");
        }
    }

    private void sendEmailRequest(User user, String url) throws Exception {
        String verificationLink = url+"/api/v1/home/verify-pswd-link?uid=%d&code=%s"
                .formatted(user.getId(), user.getStatus().getPasswordResetToken());

        String message = """
    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
        <div style="background-color: #4CAF50; color: white; padding: 20px; text-align: center;">
            <h2>Password Reset</h2>
        </div>
        <div style="padding: 20px; background-color: #f9f9f9;">
            <p>Hi <strong>%s</strong>,</p>
           \s
            <p></p>
           \s
            <p>Please reset your password by clicking the button below:</p>
           \s
            <div style="text-align: center; margin: 30px 0;">
                <a href="%s" style="background-color: #4CAF50; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; display: inline-block;">
                  Change My Password
                </a>
            </div>
           \s
            <p style="color: #666; font-size: 14px;">If you didn't create this account, please ignore this email.</p>
        </div>
        <div style="text-align: center; padding: 20px; color: #666; font-size: 12px;">
            <p>Thanks,<br>Enotes Team</p>
        </div>
    </div>
   \s""".formatted(user.getFirstName(), verificationLink);

        EmailRequest emailRequest = EmailRequest.builder()
                .to(user.getEmail())
                .title("Enotes Team")
                .subject("Password Reset For Your Enotes Account")
                .message(message)
                .build();

        // send password reset email to User
        emailService.sendEmail(emailRequest);
    }

}
