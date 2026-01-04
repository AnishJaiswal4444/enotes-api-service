package Enotes_API_Service.controller;

import Enotes_API_Service.Dto.LoginRequest;
import Enotes_API_Service.Dto.LoginResponse;
import Enotes_API_Service.Dto.UserRequest;
import Enotes_API_Service.service.AuthService;
import Enotes_API_Service.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userDto, HttpServletRequest request) throws Exception {
        log.info("AuthController : registerUser() : Start");
        String url = CommonUtil.getUrl(request);
        Boolean registered = authService.register(userDto, url);
        if(!registered){
            log.info("Error: {}","Register failed");
            return CommonUtil.createErrorResponseMessage("Unable to register the user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("AuthController : registerUser() : End");
        return CommonUtil.createBuildResponseMessage("User Registered Successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
        LoginResponse loginResponse = authService.login(loginRequest);
        if(ObjectUtils.isEmpty(loginResponse)){
            return CommonUtil.createErrorResponseMessage("invalid credentials", HttpStatus.BAD_REQUEST);
        }
        return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);
    }
}
