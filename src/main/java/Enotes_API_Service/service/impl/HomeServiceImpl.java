package Enotes_API_Service.service.impl;

import Enotes_API_Service.entity.AccountStatus;
import Enotes_API_Service.entity.User;
import Enotes_API_Service.exception.ResourceNotFoundException;
import Enotes_API_Service.exception.SuccessException;
import Enotes_API_Service.repository.UserRepository;
import Enotes_API_Service.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public Boolean verifyAccount(Integer userId, String verificationCode) throws Exception{
        log.info("HomeServiceImpl : verifyUserAccount() : Start");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Invalid User"));

        if(user.getStatus().getVerificationCode() == null){
            log.info("message : Account already verified");
            throw new SuccessException("Account is already verified");
        }
        if(user.getStatus().getVerificationCode().equals(verificationCode)){
            AccountStatus status = user.getStatus();
            status.setIsActive(true);
            status.setVerificationCode(null);
            userRepository.save(user);
            log.info("message : Account verification Success");
            return true;
        }
        log.info("HomeServiceImpl : verifyUserAccount() : End");
        return false;
    }
}
