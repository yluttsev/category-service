package ru.example.categoryservice.model.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(

        @NotBlank(message = "{category.id.not-blank}")
        @Size(max = 255, message = "{category.id.size}")
        String id,

        @NotBlank(message = "{category.name.not-blank}")
        @Size(max = 255, message = "{category.name.size}")
        String name,

        CategoryPayload parent
) {
}
