package Enotes_API_Service.controller;

import Enotes_API_Service.Dto.PasswordChangeRequest;
import Enotes_API_Service.Dto.UserResponse;
import Enotes_API_Service.endpoint.UserEndpoint;
import Enotes_API_Service.entity.User;
import Enotes_API_Service.service.UserService;
import Enotes_API_Service.util.CommonUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController implements UserEndpoint {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<?> getProfile(){
        User loggedInUser = CommonUtil.getLoggedInUser();
        UserResponse userResponse = mapper.map(loggedInUser, UserResponse.class);
        return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> changePassword(PasswordChangeRequest passwordChangeRequest){
        userService.changePassword(passwordChangeRequest);
        return CommonUtil.createBuildResponseMessage("Password Changed Successfully", HttpStatus.OK);
    }
}
