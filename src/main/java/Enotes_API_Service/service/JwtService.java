package Enotes_API_Service.service;

import Enotes_API_Service.entity.User;

public interface JwtService {

    public String generateToken(User user);
}
