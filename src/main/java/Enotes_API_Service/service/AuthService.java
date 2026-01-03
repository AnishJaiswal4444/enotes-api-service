package Enotes_API_Service.service;

import Enotes_API_Service.Dto.LoginRequest;
import Enotes_API_Service.Dto.LoginResponse;
import Enotes_API_Service.Dto.UserRequest;

public interface AuthService {

    public Boolean register(UserRequest userDto, String url) throws Exception;

    public LoginResponse login(LoginRequest loginRequest);
}
