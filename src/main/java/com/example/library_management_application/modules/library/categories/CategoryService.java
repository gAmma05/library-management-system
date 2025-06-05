package com.example.library_management_application.modules.library.categories;

import com.example.library_management_application.databases.entities.categories.Category;
import com.example.library_management_application.databases.repositories.categories.CategoryRepository;
import com.example.library_management_application.modules.library.categories.dto.CreateCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(CreateCategoryDTO ccDTO) {
        return categoryRepository.save(ccDTO.toEntity());
    }
}
