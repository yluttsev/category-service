package ru.example.categoryservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testFindAll_WhenRepositoryFull_ShouldReturnAllCategories() {
        assertThat(categoryRepository.findAll())
                .isNotEmpty()
                .allSatisfy(category -> assertFalse(category.getChildren().isEmpty()));
    }

    @Test
    void testFindById_WhenCategoryExists_ShouldReturnCategory() {
        assertTrue(categoryRepository.findById("ELECTRONICS").isPresent());
    }
}
