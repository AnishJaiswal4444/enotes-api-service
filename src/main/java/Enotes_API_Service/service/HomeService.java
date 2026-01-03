package Enotes_API_Service.service;

public interface HomeService {

    public Boolean verifyAccount (Integer userId, String verificationCode) throws Exception;
}
