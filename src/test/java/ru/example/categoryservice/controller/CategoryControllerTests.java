package ru.example.categoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.categoryservice.config.SecurityConfig;
import ru.example.categoryservice.exception.NotFoundException;
import ru.example.categoryservice.model.payload.CategoryPayload;
import ru.example.categoryservice.model.payload.CreateCategoryRequest;
import ru.example.categoryservice.security.JwtAccessAuthenticationProvider;
import ru.example.categoryservice.service.CategoryService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(CategoryController.class)
@Import(SecurityConfig.class)
@MockitoBean(types = {JwtAccessAuthenticationProvider.class})
class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreate_WhenCategoryIsNewAndHaveAccessRights_ShouldReturnStatusCode201() throws Exception {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("TEST_1", "Test 1", null);
        when(categoryService.create(createCategoryRequest)).thenReturn(null);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreate_WhenCategoryAlreadyExistsAndHaveAccessRights_ShouldReturnStatusCode409() throws Exception {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("TEST_1", "Test 1", null);
        when(categoryService.create(createCategoryRequest)).thenThrow(EntityExistsException.class);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreate_WhenNoAccessRights_shouldReturnStatusCode403() throws Exception {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("TEST_1", "Test 1", null);
        when(categoryService.create(createCategoryRequest)).thenReturn(null);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testGetAll_WhenRepositoryFull_ShouldReturnStatusCode200() throws Exception {
        List<CategoryPayload> categories = List.of(
                new CategoryPayload("TEST_1", "Test 1", null),
                new CategoryPayload("TEST_2", "Test 2", null),
                new CategoryPayload("TEST_3", "Test 3", null)
        );
        when(categoryService.getAll()).thenReturn(categories);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetById_WhenCategoryExists_ShouldReturnStatusCode200() throws Exception {
        CategoryPayload categoryPayload = new CategoryPayload("TEST_1", "Test 1", null);
        when(categoryService.getById(categoryPayload.id())).thenReturn(categoryPayload);

        mockMvc.perform(get("/categories/" + categoryPayload.id()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetById_WhenCategoryNonExists_ShouldReturnStatusCode404() throws Exception {
        String id = "NULL";
        when(categoryService.getById(id)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/categories/" + id))
                .andExpect(status().isNotFound());
    }
}
