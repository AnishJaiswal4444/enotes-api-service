package Enotes_API_Service.service;

import Enotes_API_Service.Dto.PasswordChangeRequest;
import Enotes_API_Service.Dto.PasswordResetRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    public void changePassword(PasswordChangeRequest passwordChangeRequest);

    public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception;

    public void verifyPasswordResetLink(Integer uid, String code) throws Exception;

    public void resetPassword(PasswordResetRequest passwordResetRequest) throws Exception;

}
