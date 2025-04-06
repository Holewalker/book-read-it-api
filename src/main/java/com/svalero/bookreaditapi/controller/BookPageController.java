package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.BookPage;
import com.svalero.bookreaditapi.service.BookPageService;
import com.svalero.bookreaditapi.service.RoleValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-pages")
public class BookPageController {

    @Autowired
    private BookPageService bookPageService;

    @Autowired
    private RoleValidatorService roleValidator;

    @PostMapping
    public ResponseEntity<BookPage> createBookPage(@RequestBody BookPage bookPage) {
        BookPage created = bookPageService.createBookPage(bookPage);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookPage> getBookPage(@PathVariable String bookId) {
        return bookPageService.getBookPageById(bookId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Iterable<BookPage> getAllBookPages() {
        return bookPageService.getAllBookPages();
    }

    @GetMapping("/search")
    public List<BookPage> searchByTitle(@RequestParam String title) {
        return bookPageService.searchByTitle(title);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBookPage(@PathVariable String bookId) {
        bookPageService.deleteBookPage(bookId);
        return ResponseEntity.noContent().build();
    }
}
