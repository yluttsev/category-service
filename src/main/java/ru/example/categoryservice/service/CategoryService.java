package ru.example.categoryservice.service;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.example.categoryservice.exception.NotFoundException;
import ru.example.categoryservice.model.entity.Category;
import ru.example.categoryservice.model.mapper.CategoryMapper;
import ru.example.categoryservice.model.payload.CategoryPayload;
import ru.example.categoryservice.model.payload.CreateCategoryRequest;
import ru.example.categoryservice.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Caching(evict = {
            @CacheEvict(value = "all_categories", allEntries = true),
            @CacheEvict(value = "categories", key = "#createCategoryRequest.id()")
    })
    public CategoryPayload create(@Valid CreateCategoryRequest createCategoryRequest) {
        Category category = categoryMapper.mapPayloadToEntity(createCategoryRequest);
        if (categoryRepository.existsById(createCategoryRequest.id())) {
            throw new EntityExistsException("Категория с ID '%s' уже существует".formatted(createCategoryRequest.id()));
        }
        category = categoryRepository.save(category);
        return categoryMapper.mapEntityToPayload(category);
    }

    @Cacheable(value = "all_categories")
    @Transactional(readOnly = true)
    public List<CategoryPayload> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapEntityToPayload)
                .toList();
    }

    @Cacheable(value = "categories", key = "#id")
    @Transactional(readOnly = true)
    public CategoryPayload getById(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Категория '%s' не найдена".formatted(id))
        );
        return categoryMapper.mapEntityToPayload(category);
    }
}
