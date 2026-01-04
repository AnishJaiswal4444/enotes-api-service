package Enotes_API_Service.endpoint;

import Enotes_API_Service.Dto.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/category")
public interface CategoryEndpoint {
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCategory();


    @GetMapping("/active")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getActiveCategory();

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCategoryDetailsById (@PathVariable Integer id) throws Exception;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategoryById (@PathVariable Integer id);
}
