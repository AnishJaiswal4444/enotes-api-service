package Enotes_API_Service.controller;

import Enotes_API_Service.Dto.CategoryDto;
import Enotes_API_Service.Dto.CategoryResponse;
import Enotes_API_Service.exception.ResourceNotFoundException;
import Enotes_API_Service.service.CategoryService;
import Enotes_API_Service.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/save")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) {
        Boolean saveCategory = categoryService.saveCategory(categoryDto);
        if (saveCategory) {
            return CommonUtil.createBuildResponseMessage("saved success", HttpStatus.CREATED);
//            return new ResponseEntity<>("saved success", HttpStatus.CREATED);
        } else {
            return CommonUtil.createErrorResponseMessage("not success", HttpStatus.INTERNAL_SERVER_ERROR);
//            return new ResponseEntity<>("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCategory() {
        List <CategoryDto> allCategory = categoryService.getAllCategory();
        if (CollectionUtils.isEmpty(allCategory)) {
            return ResponseEntity.noContent().build();
        } else {
            return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
//            return new ResponseEntity<>(allCategory, HttpStatus.OK);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveCategory() {
        List <CategoryResponse> allCategory = categoryService.getActiveCategory();
        if (CollectionUtils.isEmpty(allCategory)) {
            return ResponseEntity.noContent().build();
        } else {
//            return new ResponseEntity<>(allCategory, HttpStatus.OK);
            return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryDetailsById (@PathVariable Integer id) throws Exception {
        // Global exception handler will only be called when we have not used try catch, here we are not using the handler as we used try - catch
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        if (ObjectUtils.isEmpty (categoryDto)) {
//            return new ResponseEntity<>("Internal Server Error", HttpStatus.NOT_FOUND);
            return CommonUtil.createErrorResponseMessage("Internal Server Error", HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity<> (categoryDto, HttpStatus. OK);
        return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
    }
    @DeleteMapping ("/{id}")
    public ResponseEntity<?> deleteCategoryById (@PathVariable Integer id) {
        Boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
//            return new ResponseEntity<>("Category deleted success", HttpStatus.OK);
            return CommonUtil.createBuildResponseMessage("Category deleted success", HttpStatus.OK);
        }
//        return new ResponseEntity<>("Category Not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        return CommonUtil.createErrorResponseMessage("Category Not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
