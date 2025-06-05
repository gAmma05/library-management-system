package com.example.library_management_application.modules.library.categories.dto;

import com.example.library_management_application.databases.entities.categories.Category;
import lombok.Data;

import java.time.Instant;
import java.util.List;


@Data
public class CategoryDTO {
    private Integer categoryId;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    public CategoryDTO(Integer categoryId, String name, String description, Instant createdAt, Instant updatedAt) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CategoryDTO fromCategory(Category category) {
        return new CategoryDTO(
                category.getCategoryId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt());
    }

    public static List<CategoryDTO> fromCategories(List<Category> categories) {
        return categories.stream().map(CategoryDTO::fromCategory).toList();
    }
}
