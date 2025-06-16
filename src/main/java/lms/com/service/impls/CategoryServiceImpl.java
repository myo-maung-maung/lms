package lms.com.service.impls;

import lms.com.dtos.CategoryDTO;
import lms.com.entity.Category;
import lms.com.mapper.CategoryMapper;
import lms.com.repository.CategoryRepository;
import lms.com.service.CategoryService;
import lombok.RequiredArgsConstructor;
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
            throw new RuntimeException("Category with name '" + categoryDTO.getName() + "' already exists");
        }
        Category category = CategoryMapper.dtoToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.entityToDto(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existing.setName(categoryDTO.getName());

        categoryRepository.save(existing);
        return CategoryMapper.entityToDto(existing);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> CategoryMapper.entityToDto(category))
                .collect(Collectors.toList());
    }
}
