package com.example.library_management_application.modules.library.books;

import com.example.library_management_application.databases.entities.books.Book;
import com.example.library_management_application.databases.repositories.books.BookRepository;
import com.example.library_management_application.modules.library.books.dto.CreateBookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book createBook(CreateBookDTO CreateBookDTO) {
        return bookRepository.save(CreateBookDTO.toEntity());
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookByTitle(String title) {
        return bookRepository.findByTitle(title).orElse(null);
    }
}
