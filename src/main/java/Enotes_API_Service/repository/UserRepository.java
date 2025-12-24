package Enotes_API_Service.repository;

import Enotes_API_Service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
   public Boolean existsByEmail(String email);
}
