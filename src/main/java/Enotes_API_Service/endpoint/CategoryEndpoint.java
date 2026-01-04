package Enotes_API_Service.endpoint;

import Enotes_API_Service.Dto.CategoryDto;
import static Enotes_API_Service.util.Constants.ROLE_ADMIN;
import static Enotes_API_Service.util.Constants.ROLE_USER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Category", description = "below are all the Category API")
@RequestMapping("/api/v1/category")
public interface CategoryEndpoint {

    @Operation(summary = "Create New Category", description = "Admin only - Create a new category", tags = {"Category"})
    @PostMapping("/save")
    @PreAuthorize(ROLE_ADMIN)
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);

    @Operation(summary = "Get All Categories", description = "Admin only - Retrieve all categories", tags = {"Category"})
    @GetMapping("/")
    @PreAuthorize(ROLE_ADMIN)
    public ResponseEntity<?> getAllCategory();

    @Operation(summary = "Get Active Categories", description = "User accessible - Retrieve only active categories", tags = {"Category"})
    @GetMapping("/active")
    @PreAuthorize(ROLE_USER)
    public ResponseEntity<?> getActiveCategory();

    @Operation(summary = "Get Category by ID", description = "Admin only - Retrieve category details by ID", tags = {"Category"})
    @GetMapping("/{id}")
    @PreAuthorize(ROLE_ADMIN)
    public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Delete Category", description = "Admin only - Delete category by ID", tags = {"Category"})
    @DeleteMapping("/{id}")
    @PreAuthorize(ROLE_ADMIN)
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id);
}