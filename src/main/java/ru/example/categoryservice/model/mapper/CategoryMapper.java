package ru.example.categoryservice.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.example.categoryservice.model.entity.Category;
import ru.example.categoryservice.model.payload.CategoryPayload;
import ru.example.categoryservice.model.payload.CreateCategoryRequest;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    Category mapPayloadToEntity(CreateCategoryRequest payload);

    @Mapping(target = "children", source = "children", qualifiedByName = "mapChildren")
    CategoryPayload mapEntityToPayload(Category category);

    @Named("mapChildren")
    default List<CategoryPayload> mapChildren(Set<Category> children) {
        if (children == null) return Collections.emptyList();

        return children.stream()
                .map(c -> new CategoryPayload(c.getId(), c.getName(), null))
                .toList();
    }
}
