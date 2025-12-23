package Enotes_API_Service.controller;

import Enotes_API_Service.Dto.UserDto;
import Enotes_API_Service.service.UserService;
import Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
        Boolean registered = userService.register(userDto);
        if(registered){
            return CommonUtil.createBuildResponseMessage("User Registered Successfully", HttpStatus.CREATED);
        }else {
            return CommonUtil.createErrorResponseMessage("Unable to register the user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
