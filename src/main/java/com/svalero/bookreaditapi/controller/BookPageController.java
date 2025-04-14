package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.BookPage;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.service.BookPageService;
import com.svalero.bookreaditapi.service.RoleValidatorService;
import com.svalero.bookreaditapi.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/book-pages")
public class BookPageController {

    @Autowired
    private BookPageService bookPageService;

    @Autowired
    private RoleValidatorService roleValidator;

    @Autowired
    private SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<BookPage> createBookPage(@RequestBody BookPage bookPage,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);
        bookPage.setOwnerUserId(user.getId());
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
    public ResponseEntity<Void> deleteBookPage(@PathVariable String bookId,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);

        BookPage bookPage = bookPageService.getBookPageById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));

        boolean isOwner = roleValidator.isOwner(bookId, user.getId());
        boolean isAdmin = "ADMIN".equals(user.getRole());

        if (!isOwner && !isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado para eliminar este libro");
        }

        bookPageService.deleteBookPage(bookId);
        return ResponseEntity.noContent().build();
    }

}
