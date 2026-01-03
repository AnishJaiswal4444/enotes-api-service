package Enotes_API_Service.config;

import Enotes_API_Service.entity.User;
import Enotes_API_Service.util.CommonUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditAwareConfig implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        User loggedInUser = CommonUtil.getLoggedInUser();
        return Optional.of(loggedInUser.getId());
    }
}
