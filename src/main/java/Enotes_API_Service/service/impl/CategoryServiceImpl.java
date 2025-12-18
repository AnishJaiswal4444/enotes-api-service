package Enotes_API_Service.service.impl;

import Enotes_API_Service.Dto.CategoryDto;
import Enotes_API_Service.Dto.CategoryResponse;
import Enotes_API_Service.entity.Category;
import Enotes_API_Service.exception.ResourceNotFoundException;
import Enotes_API_Service.repository.CategoryRepository;
import Enotes_API_Service.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryRepository categoryRepo;

    @Override
    public Boolean saveCategory (CategoryDto categoryDto) {
//        Category category = new Category();
//        category.setName(categoryDto.getName());
//        category.setDescription(categoryDto.getDescription());
//        category.setIsActive(categoryDto.getIsActive () );

        Category category = mapper.map (categoryDto, Category.class);

        if(ObjectUtils.isEmpty(category.getId())){
            category.setIsDeleted (false);
            category.setCreatedBy (1);
            category.setCreatedOn (new Date());
        }else{
            updateCategory(category);
        }

        Category saveCategory = categoryRepo.save (category);
        if (ObjectUtils.isEmpty (saveCategory)) {
            return false;
        }
        return true;
    }

    private void updateCategory(Category category) {

        Optional<Category> findById = categoryRepo.findById(category.getId());
        if(findById.isPresent()){
            Category existCategory = findById.get();
            category.setCreatedBy(existCategory.getCreatedBy());
            category.setCreatedOn(existCategory.getCreatedOn());
            category.setIsDeleted(existCategory.getIsDeleted());
            category.setUpdatedBy(1);
            category.setUpdatedOn(new Date());
        }
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepo.findByIsDeletedFalse();
        return (List<CategoryDto>) categories.stream().map (cat->mapper.map (cat, CategoryDto.class)).toList();
    }

    @Override
    public List<CategoryResponse> getActiveCategory() {

        List<Category> categories = categoryRepo.findByIsActiveTrueAndIsDeletedFalse();
        List<CategoryResponse> categoryList = categories.stream().map(cat -> mapper.map(cat, CategoryResponse.class))
                .toList();
        return categoryList;
    }

    @Override
    public CategoryDto getCategoryById(Integer id) throws Exception {
        Category category = categoryRepo.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id = " +id));

        if(!ObjectUtils.isEmpty(category)){
            return mapper.map(category, CategoryDto.class);
        }
        return null;
    }

    @Override
    public Boolean deleteCategory(Integer id) {
        Optional<Category> findByCategory =categoryRepo.findById(id);

        if(findByCategory.isPresent()){
            Category category = findByCategory.get();
            category.setIsDeleted(true);
            categoryRepo.save(category);
            return true;
        }
        return false;
    }
}
