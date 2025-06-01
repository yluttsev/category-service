package ru.example.categoryservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.categoryservice.model.payload.CategoryPayload;
import ru.example.categoryservice.model.payload.CreateCategoryRequest;
import ru.example.categoryservice.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryPayload create(@RequestBody CreateCategoryRequest createCategoryRequest) {
        return categoryService.create(createCategoryRequest);
    }

    @GetMapping
    public List<CategoryPayload> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryPayload getById(@PathVariable("id") String id) {
        return categoryService.getById(id);
    }
}
