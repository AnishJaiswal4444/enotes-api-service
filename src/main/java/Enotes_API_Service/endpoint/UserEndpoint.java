package Enotes_API_Service.endpoint;

import Enotes_API_Service.Dto.PasswordChangeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "User Profile", description = "User profile and account settings management")
@RequestMapping("/api/v1/user")
public interface UserEndpoint {

    @Operation(summary = "Get User Profile", description = "Retrieve current authenticated user's profile information", tags = {"User Profile"})
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile();

    @Operation(summary = "Change Password", description = "Update password for the authenticated user", tags = {"User Profile"})
    @PostMapping("/chng-pswd")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest);
}