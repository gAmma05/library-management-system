package com.example.library_management_application.modules.library.books.dto;

import com.example.library_management_application.databases.entities.books.Book;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;

@Data
public class CreateBookDTO {
    private Integer bookId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String isbn;

    @NotBlank
    private String author;

    private Instant createdAt;

    private Instant updatedAt;

    public Book toEntity() {
        return new Book(bookId, title, description, isbn, author, createdAt, updatedAt);
    }
}
