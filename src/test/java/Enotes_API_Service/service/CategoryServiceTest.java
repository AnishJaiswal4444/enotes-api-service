package Enotes_API_Service.service;

import Enotes_API_Service.Dto.CategoryDto;
import Enotes_API_Service.entity.Category;
import Enotes_API_Service.exception.ExistDataException;
import Enotes_API_Service.repository.CategoryRepository;
import Enotes_API_Service.service.impl.CategoryServiceImpl;
import Enotes_API_Service.util.Validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private Validation validation;

    @Mock
    private ModelMapper mapper;

    private CategoryDto categoryDto = null;
    private Category category = null;
    private List<Category> categories = new ArrayList<>();
    private List<CategoryDto> categoriesDto = new ArrayList<>();

    @BeforeEach
    public void initialize(){
        categoryDto = CategoryDto.builder()
                .id(null)
                .name("JAVA Notes")
                .description("Java notes")
                .isActive(true).build();

        category  = Category.builder()
                .id(null)
                .name("JAVA Notes")
                .description("Java notes")
                .isActive(true)
                .isDeleted(false)
                .build();

        categories.add(category);
        categoriesDto.add(categoryDto);
    }

    @Test
    public void testSaveCategory(){

        //Arrange
        when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(false);
        when(mapper.map(categoryDto,Category.class)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        //Act
        Boolean saveCategory = categoryService.saveCategory(categoryDto);

        //Assert
        assertTrue(saveCategory);

        //Verify
        verify(validation).categoryValidation(categoryDto);
        verify(categoryRepository).existsByName(categoryDto.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    public void testCategoryExist() {
        when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(true);
        ExistDataException exception = assertThrows(ExistDataException.class, () -> {
            categoryService.saveCategory(categoryDto);
        });
        assertEquals("Category already exist", exception.getMessage());
        verify(validation).categoryValidation(categoryDto);
        verify(categoryRepository).existsByName(categoryDto.getName());
        verify(categoryRepository, never()).save(category);
    }

    @Test
    public void testUpdateCategory(){
        categoryDto.setId(1);
        category.setId(1);

        //Arrange
        when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(false);
        when(mapper.map(categoryDto,Category.class)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        //Act
        Boolean saveCategory = categoryService.saveCategory(categoryDto);

        //Assert
        assertTrue(saveCategory);

        //Verify
        verify(validation).categoryValidation(categoryDto);
        verify(categoryRepository).existsByName(categoryDto.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    public void testGetAllCategory(){
        when(categoryRepository.findByIsDeletedFalse()).thenReturn(categories);
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        assertEquals(allCategory.size(), categories.size());
        verify(categoryRepository).findByIsDeletedFalse();
    }
}
