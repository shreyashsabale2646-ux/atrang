package in.ac.iiitt.atrang.controller;

import in.ac.iiitt.atrang.dto.ApiResponse;
import in.ac.iiitt.atrang.dto.CategoryCreateRequest;
import in.ac.iiitt.atrang.dto.CategoryDto;
import in.ac.iiitt.atrang.dto.CategoryUpdateRequest;
import in.ac.iiitt.atrang.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories() {
        List<CategoryDto> list = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryById(@PathVariable Long id) {
        CategoryDto dto = categoryService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(
            @Valid @RequestBody CategoryCreateRequest request,
            Authentication authentication) {
        String performer = authentication != null ? authentication.getName() : "ADMIN";
        CategoryDto created = categoryService.createCategory(request, performer);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Category created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request,
            Authentication authentication) {
        String performer = authentication != null ? authentication.getName() : "ADMIN";
        CategoryDto updated = categoryService.updateCategory(id, request, performer);
        return ResponseEntity.ok(ApiResponse.ok("Category updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(
            @PathVariable Long id,
            Authentication authentication) {
        String performer = authentication != null ? authentication.getName() : "ADMIN";
        categoryService.deleteCategory(id, performer);
        return ResponseEntity.ok(ApiResponse.ok("Category deleted successfully", "Deleted category id: " + id));
    }
}
