package Enotes_API_Service.endpoint;

import Enotes_API_Service.Dto.CategoryDto;
import static Enotes_API_Service.util.Constants.ROLE_ADMIN;
import static Enotes_API_Service.util.Constants.ROLE_USER;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/category")
public interface CategoryEndpoint {
    @PostMapping("/save")
    @PreAuthorize(ROLE_ADMIN)
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);

    @GetMapping("/")
    @PreAuthorize(ROLE_ADMIN)
    public ResponseEntity<?> getAllCategory();


    @GetMapping("/active")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getActiveCategory();

    @GetMapping("/{id}")
    @PreAuthorize(ROLE_ADMIN)
    public ResponseEntity<?> getCategoryDetailsById (@PathVariable Integer id) throws Exception;

    @DeleteMapping("/{id}")
    @PreAuthorize(ROLE_ADMIN)
    public ResponseEntity<?> deleteCategoryById (@PathVariable Integer id);
}
