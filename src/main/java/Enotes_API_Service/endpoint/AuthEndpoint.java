package Enotes_API_Service.endpoint;

import Enotes_API_Service.Dto.LoginRequest;
import Enotes_API_Service.Dto.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Authentication", description = "below are all the authentication API")
@RequestMapping("/api/v1/auth")
public interface AuthEndpoint {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Register Success"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
    })
    @Operation(summary = "User Registration Endpoint", tags = {"Authentication"})
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userDto, HttpServletRequest request) throws Exception;

    @Operation(summary = "User Login Endpoint", tags = {"Authentication"})
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception;
}
