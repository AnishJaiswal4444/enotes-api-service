package Enotes_API_Service.repository;

import Enotes_API_Service.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

    public List<Todo> findByCreatedBy(Integer userId);
}
