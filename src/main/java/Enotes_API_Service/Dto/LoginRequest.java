package Enotes_API_Service.Dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
