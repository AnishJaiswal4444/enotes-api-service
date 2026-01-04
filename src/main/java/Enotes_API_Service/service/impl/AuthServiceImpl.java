package Enotes_API_Service.service.impl;

import Enotes_API_Service.Dto.*;
import Enotes_API_Service.config.security.CustomUserDetails;
import Enotes_API_Service.entity.AccountStatus;
import Enotes_API_Service.entity.Role;
import Enotes_API_Service.entity.User;
import Enotes_API_Service.repository.RoleRepository;
import Enotes_API_Service.repository.UserRepository;
import Enotes_API_Service.service.JwtService;
import Enotes_API_Service.service.AuthService;
import Enotes_API_Service.util.Validation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Validation validation;

    @Autowired
    private ModelMapper  mapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    @Override
    public Boolean register(UserRequest userDto, String url) throws Exception {
        log.info("AuthServiceImpl : registerUser() : Start");
        // Validate user role
        validation.userValidation(userDto);
        User user = mapper.map(userDto, User.class);
        setRole(userDto, user);

        AccountStatus status = AccountStatus.builder()
                .isActive(false)
                .verificationCode(UUID.randomUUID().toString())
                .build();
        user.setStatus(status);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        if(ObjectUtils.isEmpty(savedUser)){
            log.info("Error : {}","unable to save user");
            return false;
        }
        log.info("Message : {} "," User has been registered successfully");
        sendVerificationEmail(savedUser, url);
        log.info("Message : {} "," email sent successfully");
        log.info("AuthServiceImpl : registerUser() : End");
        return true;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        if(authenticate.isAuthenticated()){
            CustomUserDetails customUserDetails = (CustomUserDetails)authenticate.getPrincipal();
            String token = jwtService.generateToken(customUserDetails.getUser());
            LoginResponse loginResponse = LoginResponse.builder()
                    .user(mapper.map(customUserDetails.getUser(), UserResponse.class))
                    .token(token).build();
            return loginResponse;
        }
        return null;
    }

    private void sendVerificationEmail(User savedUser,String url) throws Exception {

        String verificationLink = url+"/api/v1/home/verify?uid=%d&code=%s"
                .formatted(savedUser.getId(), savedUser.getStatus().getVerificationCode());

        String message = """
    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
        <div style="background-color: #4CAF50; color: white; padding: 20px; text-align: center;">
            <h2>Welcome to Enotes!</h2>
        </div>
        <div style="padding: 20px; background-color: #f9f9f9;">
            <p>Hi <strong>%s</strong>,</p>
            
            <p>Your account has been created successfully! We're excited to have you on board.</p>
            
            <p>To get started, please verify your email address by clicking the button below:</p>
            
            <div style="text-align: center; margin: 30px 0;">
                <a href="%s" style="background-color: #4CAF50; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; display: inline-block;">
                    Verify My Account
                </a>
            </div>
            
            <p style="color: #666; font-size: 14px;">If you didn't create this account, please ignore this email.</p>
        </div>
        <div style="text-align: center; padding: 20px; color: #666; font-size: 12px;">
            <p>Thanks,<br>Enotes Team</p>
        </div>
    </div>
    """.formatted(savedUser.getFirstName(), verificationLink);

        EmailRequest emailRequest = EmailRequest.builder()
                .to(savedUser.getEmail())
                .title("Enotes Team")
                .subject("Verify Your Enotes Account")
                .message(message)
                .build();

        emailService.sendEmail(emailRequest);
    }

    private void setRole(UserRequest userDto, User user) {
        List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
        List<Role> roles = roleRepository.findAllById(reqRoleId);
        user.setRoles(roles);
    }
}
