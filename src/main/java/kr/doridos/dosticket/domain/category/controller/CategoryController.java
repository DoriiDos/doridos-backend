package kr.doridos.dosticket.domain.category.controller;

import kr.doridos.dosticket.domain.auth.support.jwt.UserDetailsImpl;
import kr.doridos.dosticket.domain.category.dto.CategoryRequest;
import kr.doridos.dosticket.domain.category.dto.CategoryResponse;
import kr.doridos.dosticket.domain.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@AuthenticationPrincipal final UserDetailsImpl userDetails,
                                                           @RequestBody final CategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.createCategory(userDetails.getUser().getUserType(), categoryRequest);
        return ResponseEntity.ok(response);
    }
}
