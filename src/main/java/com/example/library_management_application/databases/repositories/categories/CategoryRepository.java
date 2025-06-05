package com.example.library_management_application.databases.repositories.categories;

import com.example.library_management_application.databases.entities.categories.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
