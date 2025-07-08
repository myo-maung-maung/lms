package lms.com.service.impls;

import lms.com.common.Constant;
import lms.com.common.LMSResponse;
import lms.com.dtos.CategoryDTO;
import lms.com.dtos.PageDTO;
import lms.com.entity.Category;
import lms.com.exceptions.DuplicateException;
import lms.com.mapper.CategoryMapper;
import lms.com.repository.CategoryRepository;
import lms.com.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findByNameIgnoreCase(categoryDTO.getName());
        if (existingCategory.isPresent()) {
            throw new DuplicateException("Category with name '" + categoryDTO.getName() + "' already exists");
        }
        Category category = CategoryMapper.dtoToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.entityToDto(savedCategory);
    }

    @Override
    @Transactional
    public LMSResponse updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existing.setName(categoryDTO.getName());

        categoryRepository.save(existing);
        return LMSResponse.success(Constant.CATEGORY_UPDATED, CategoryMapper.entityToDto(existing));
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PageDTO<CategoryDTO> getPaginationCategory(int page, int size) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        Page<CategoryDTO> dto = categoryPage.map(CategoryMapper::entityToDto);
        return PageDTO.of(dto);
    }

    @Override
    public LMSResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        CategoryDTO categoryDTO = CategoryMapper.entityToDto(category);
        return LMSResponse.success(Constant.CATEGORY_ID, categoryDTO);
    }
}
