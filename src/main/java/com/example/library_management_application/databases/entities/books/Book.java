package com.example.library_management_application.databases.entities.books;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    @Column(length = 255) // Adjust the length according to your needs, e.g., 255 characters for a long title
    private String title;

    @Column(length = 1000)
    private String description;

    @Column
    private String isbn;

    @Column(length = 255)
    private String author;

    private Instant createdAt;
    private Instant updatedAt;

}