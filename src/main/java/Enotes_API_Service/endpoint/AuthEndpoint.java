package Enotes_API_Service.endpoint;

import Enotes_API_Service.Dto.LoginRequest;
import Enotes_API_Service.Dto.UserRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
public interface AuthEndpoint {

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userDto, HttpServletRequest request) throws Exception;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception;
}
