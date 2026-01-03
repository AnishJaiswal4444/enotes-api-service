package Enotes_API_Service.service;

import Enotes_API_Service.Dto.CategoryDto;
import Enotes_API_Service.Dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    public Boolean saveCategory(CategoryDto categoryDto);

    public List<CategoryDto> getAllCategory();

    public List<CategoryResponse> getActiveCategory();

    public CategoryDto getCategoryById(Integer id) throws Exception;

    public Boolean deleteCategory(Integer id);

}