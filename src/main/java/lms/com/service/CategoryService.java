package lms.com.service;

import lms.com.dtos.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);

    void deleteCategory(Long categoryId);

    List<CategoryDTO> getAllCategories();
}
