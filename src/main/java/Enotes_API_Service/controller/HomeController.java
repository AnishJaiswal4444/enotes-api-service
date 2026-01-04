package Enotes_API_Service.controller;

import Enotes_API_Service.Dto.PasswordResetRequest;
import Enotes_API_Service.endpoint.HomeEndpoint;
import Enotes_API_Service.service.HomeService;
import Enotes_API_Service.service.UserService;
import Enotes_API_Service.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController implements HomeEndpoint {

    Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception {
        log.info("HomeController : verifyUserAccount : Execution Start");
        Boolean verifyAccount = homeService.verifyAccount(uid, code);
        if(verifyAccount){
            return CommonUtil.createBuildResponseMessage("Account is verified", HttpStatus.CREATED);
        }
        log.info("HomeController : verifyUserAccount : Execution End");
        return CommonUtil.createErrorResponseMessage("Invalid Verification link", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception {
        userService.sendEmailPasswordReset(email, request);
        return CommonUtil.createBuildResponseMessage("Reset email has been sent", HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code) throws Exception {
        userService.verifyPasswordResetLink(uid, code);
        return CommonUtil.createBuildResponseMessage("verification success", HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception {
        userService.resetPassword(passwordResetRequest);
        return CommonUtil.createBuildResponseMessage("Password Reset Successfully", HttpStatus.OK);
    }
}
