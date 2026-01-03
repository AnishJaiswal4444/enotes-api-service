package Enotes_API_Service.controller;

import Enotes_API_Service.Dto.PasswordResetRequest;
import Enotes_API_Service.service.HomeService;
import Enotes_API_Service.service.UserService;
import Enotes_API_Service.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception {
        Boolean verifyAccount = homeService.verifyAccount(uid, code);
        if(verifyAccount){
            return CommonUtil.createBuildResponseMessage("Account is verified", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Invalid Verification link", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/send-email-reset")
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception {
        userService.sendEmailPasswordReset(email, request);
        return CommonUtil.createBuildResponseMessage("Reset email has been sent", HttpStatus.OK);
    }
    @GetMapping("/verify-pswd-link")
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code) throws Exception {
        userService.verifyPasswordResetLink(uid, code);
        return CommonUtil.createBuildResponseMessage("verification success", HttpStatus.OK);
    }
    @PostMapping("/reset-pswd")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception {
        userService.resetPassword(passwordResetRequest);
        return CommonUtil.createBuildResponseMessage("Password Reset Successfully", HttpStatus.OK);
    }
}
