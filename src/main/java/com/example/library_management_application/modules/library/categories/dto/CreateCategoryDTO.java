package com.example.library_management_application.modules.library.categories.dto;

import com.example.library_management_application.databases.entities.categories.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;

@Data
public class CreateCategoryDTO {
    private Integer categoryId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    public Category toEntity() {
        return new Category(categoryId, name, description, createdAt, updatedAt);
    }
}
