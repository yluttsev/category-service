package ru.example.categoryservice.model.payload;

import java.io.Serializable;
import java.util.List;

public record CategoryPayload(
        String id,
        String name,
        List<CategoryPayload> children
) implements Serializable {
}
