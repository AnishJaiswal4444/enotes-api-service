package Enotes_API_Service.endpoint;

import Enotes_API_Service.Dto.TodoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static Enotes_API_Service.util.Constants.ROLE_USER;

@Tag(name = "Todo", description = "Todo list management API for user tasks")
@RequestMapping("/api/v1/todo")
public interface TodoEndpoint {

    @Operation(summary = "Create Todo", description = "User - Create a new todo item", tags = {"Todo"})
    @PostMapping("/")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> saveTodo(@RequestBody TodoDto todoDto) throws Exception;

    @Operation(summary = "Get Todo by ID", description = "User - Retrieve a specific todo item by ID", tags = {"Todo"})
    @GetMapping("/{id}")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getTodo(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Get All User Todos", description = "User - Retrieve all todo items for the current user", tags = {"Todo"})
    @GetMapping("/list")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getAllTodoByUser() throws Exception;
}