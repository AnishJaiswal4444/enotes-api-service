package Enotes_API_Service.service.impl;

import Enotes_API_Service.Dto.EmailRequest;
import Enotes_API_Service.Dto.UserDto;
import Enotes_API_Service.entity.Role;
import Enotes_API_Service.entity.User;
import Enotes_API_Service.repository.RoleRepository;
import Enotes_API_Service.repository.UserRepository;
import Enotes_API_Service.service.EmailService;
import Enotes_API_Service.service.UserService;
import Enotes_API_Service.util.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

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

    @Override
    public Boolean register(UserDto userDto) throws Exception {

        // Validate user role
        validation.userValidation(userDto);
        User user = mapper.map(userDto, User.class);
        setRole(userDto, user);
        User savedUser = userRepository.save(user);
        if(!ObjectUtils.isEmpty(savedUser)){
            sendVerificationEmail(savedUser);
            return true;
        }
        return false;
    }

    private void sendVerificationEmail(User savedUser) throws Exception {

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
                    <a href="#" style="background-color: #4CAF50; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; display: inline-block;">
                        Verify My Account
                    </a>
                </div>
                
                <p style="color: #666; font-size: 14px;">If you didn't create this account, please ignore this email.</p>
            </div>
            <div style="text-align: center; padding: 20px; color: #666; font-size: 12px;">
                <p>Thanks,<br>Enotes Team</p>
            </div>
        </div>
        """.formatted(savedUser.getFirstName());

        EmailRequest emailRequest = EmailRequest.builder()
                .to(savedUser.getEmail())
                .title("Enotes Team")
                .subject("Verify Your Enotes Account")
                .message(message)
                .build();

        emailService.sendEmail(emailRequest);
    }

    private void setRole(UserDto userDto, User user) {
        List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
        List<Role> roles = roleRepository.findAllById(reqRoleId);
        user.setRoles(roles);
    }
}
