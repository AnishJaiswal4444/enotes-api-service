package Enotes_API_Service.endpoint;

import Enotes_API_Service.Dto.TodoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static Enotes_API_Service.util.Constants.ROLE_USER;

@RequestMapping("/api/v1/todo")
public interface TodoEndpoint {

    @PostMapping("/")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> saveTodo (@RequestBody TodoDto todoDto) throws Exception;

    @GetMapping("/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getTodo (@PathVariable Integer id) throws Exception;

    @GetMapping("/list")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getAllTodoByUser () throws Exception;
}
