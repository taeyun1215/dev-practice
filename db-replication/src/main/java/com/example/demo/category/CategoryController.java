package com.example.demo.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<?> register(
            @RequestBody CategoryRegisterRequest categoryRegisterRequest
    ) {
        categoryService.categoryRegister(categoryRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
