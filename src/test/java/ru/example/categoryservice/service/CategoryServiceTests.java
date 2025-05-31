package ru.example.categoryservice.service;

import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.example.categoryservice.exception.NotFoundException;
import ru.example.categoryservice.model.entity.Category;
import ru.example.categoryservice.model.mapper.CategoryMapperImpl;
import ru.example.categoryservice.model.payload.CreateCategoryRequest;
import ru.example.categoryservice.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(
        classes = {
                CategoryMapperImpl.class,
                CategoryRepository.class,
                CategoryService.class
        }
)
class CategoryServiceTests {

    @MockitoBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Test
    void testCreate_WhenCategoryIsNew_ShouldReturnCreatedCategory() {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("TEST_1", "Test 1", null);
        Category category = new Category("TEST_1", "Test 1", null, null);
        when(categoryRepository.save(category)).thenReturn(category);

        assertNotNull(categoryService.create(createCategoryRequest));
    }

    @Test
    void testCreate_WhenCategoryAlreadyExists_ShouldThrowsException() {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("TEST_1", "Test 1", null);
        when(categoryRepository.existsById(createCategoryRequest.id())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> categoryService.create(createCategoryRequest));
    }

    @Test
    void testGetAll_WhenRepositoryFull_ShouldReturnCategoryList() {
        List<Category> categories = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> categories.add(new Category("TEST_" + i, "Test " + i, null, null)));
        when(categoryRepository.findAll()).thenReturn(categories);

        assertEquals(10, categoryService.getAll().size());
    }

    @Test
    void testGetById_WhenCategoryExists_ShouldReturnCategory() {
        Category category = new Category("TEST_1", "Test 1", null, null);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        assertNotNull(categoryService.getById(category.getId()));
    }

    @Test
    void testGetById_WhenCategoryNonExists_ShouldThrowsException() {
        String id = "NULL";
        when(categoryRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> categoryService.getById(id));
    }
}
