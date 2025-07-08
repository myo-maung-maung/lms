package lms.com.controller;

import jakarta.validation.Valid;
import lms.com.common.Constant;
import lms.com.common.LMSResponse;
import lms.com.dtos.CategoryDTO;
import lms.com.dtos.PageDTO;
import lms.com.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add-category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(savedCategory);
    }

    @PutMapping("/update/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LMSResponse> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {

        return ResponseEntity.ok(categoryService.updateCategory(categoryId, categoryDTO));
    }

    @GetMapping("/all-categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<LMSResponse> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(LMSResponse.success(Constant.GET_ALL, categories));
    }

    @GetMapping("/categoryById/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LMSResponse> getById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @DeleteMapping("/delete/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pagination")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LMSResponse> getPaginationCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ) {
        PageDTO<CategoryDTO> pageDto = categoryService.getPaginationCategory(page, size);
        return ResponseEntity.ok(LMSResponse.success(Constant.PAGINATION, pageDto));
    }
}
