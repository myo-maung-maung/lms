package lms.com.service;

import lms.com.common.LMSResponse;
import lms.com.dtos.CategoryDTO;
import lms.com.dtos.PageDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    LMSResponse updateCategory(Long categoryId, CategoryDTO categoryDTO);

    void deleteCategory(Long categoryId);

    List<CategoryDTO> getAllCategories();

    PageDTO<CategoryDTO> getPaginationCategory(int page, int size);

    LMSResponse getCategoryById(Long categoryId);
}
