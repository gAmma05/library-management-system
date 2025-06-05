package com.example.library_management_application.modules.library.books;

import com.example.library_management_application.databases.entities.books.Book;
import com.example.library_management_application.modules.library.books.dto.BookDTO;
import com.example.library_management_application.modules.library.books.dto.CreateBookDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/library/books")
public class BookController {
    @Autowired
    private BookService bookServices;

    @PostMapping("/create")
    public ResponseEntity createBook(@Valid @RequestBody CreateBookDTO createBookDTO) {
        Book book = bookServices.createBook(createBookDTO);
        return new ResponseEntity(BookDTO.fromBook(book), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity getAllBooks() {
        return new ResponseEntity(BookDTO.fromBooks(bookServices.getAllBooks()), HttpStatus.OK);
    }

    @PostMapping("/searchBook")
    public ResponseEntity searchBook(@RequestParam String title) {
        Book book = bookServices.getBookByTitle(title);
        return new ResponseEntity(BookDTO.fromBook(book), HttpStatus.OK);
    }

    @GetMapping("/{title}")
    public ResponseEntity getBookByTitleThroughParam(@PathVariable String title) {
        Book book = bookServices.getBookByTitle(title);
        return new ResponseEntity(BookDTO.fromBook(book), HttpStatus.OK);
    }
}
