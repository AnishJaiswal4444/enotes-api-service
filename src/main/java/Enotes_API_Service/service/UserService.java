package Enotes_API_Service.service;

import Enotes_API_Service.Dto.PasswordChangeRequest;

public interface UserService {
    public void changePassword(PasswordChangeRequest passwordChangeRequest);
}
