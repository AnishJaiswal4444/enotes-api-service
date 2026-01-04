package Enotes_API_Service.endpoint;

import Enotes_API_Service.Dto.PasswordResetRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account Management", description = "Public endpoints for account verification and password reset")
@RequestMapping("/api/v1/home")
public interface HomeEndpoint {

    @Operation(summary = "Verify User Account", description = "Public - Verify user account using email verification link", tags = {"Account Management"})
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception;

    @Operation(summary = "Reset Password", description = "Public - Reset user password with new credentials", tags = {"Account Management"})
    @PostMapping("/reset-pswd")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception;

    @Operation(summary = "Verify Password Reset Link", description = "Public - Validate password reset link before showing reset form", tags = {"Account Management"})
    @GetMapping("/verify-pswd-link")
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code) throws Exception;

    @Operation(summary = "Send Password Reset Email", description = "Public - Request password reset link via email", tags = {"Account Management"})
    @GetMapping("/send-email-reset")
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception;
}