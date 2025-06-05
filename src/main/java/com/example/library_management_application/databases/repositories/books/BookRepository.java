package com.example.library_management_application.databases.repositories.books;

import com.example.library_management_application.databases.entities.books.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByTitle(String title);
}
