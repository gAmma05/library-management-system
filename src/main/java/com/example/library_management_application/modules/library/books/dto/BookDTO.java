package com.example.library_management_application.modules.library.books.dto;

import com.example.library_management_application.databases.entities.books.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class BookDTO {
    private Integer bookId;
    private String title;
    private String description;
    private String isbn;
    private String author;
    private Instant createdAt;
    private Instant updatedAt;

    public BookDTO(Integer bookId, String title, String description, String isbn, String author, Instant createdAt, Instant updatedAt) {
        this.bookId = bookId;
        this.title = title;
        this.description = description;
        this.isbn = isbn;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BookDTO fromBook(Book book) {
        return new BookDTO(
                book.getBookId(),
                book.getTitle(),
                book.getDescription(),
                book.getIsbn(), book.getAuthor(),
                book.getCreatedAt(),
                book.getUpdatedAt());
    }

    public static List<BookDTO> fromBooks(List<Book> books) {
        return books.stream().map(BookDTO::fromBook).toList();
    }
}
