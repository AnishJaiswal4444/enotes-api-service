package Enotes_API_Service.service.impl;

import Enotes_API_Service.Dto.PasswordChangeRequest;
import Enotes_API_Service.entity.User;
import Enotes_API_Service.repository.UserRepository;
import Enotes_API_Service.service.UserService;
import Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

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

}
