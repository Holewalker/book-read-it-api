package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.BookPage;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.repository.UserRepository;
import com.svalero.bookreaditapi.service.BookPageService;
import com.svalero.bookreaditapi.service.RoleValidatorService;
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
    private UserRepository userRepository;

    // ✅ SOLO usuarios logueados pueden crear
    @PostMapping
    public ResponseEntity<BookPage> createBookPage(@RequestBody BookPage bookPage,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        bookPage.setOwnerUserId(user.getUserId());
        BookPage created = bookPageService.createBookPage(bookPage);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ✅ PÚBLICO
    @GetMapping("/{bookId}")
    public ResponseEntity<BookPage> getBookPage(@PathVariable String bookId) {
        return bookPageService.getBookPageById(bookId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ PÚBLICO
    @GetMapping
    public Iterable<BookPage> getAllBookPages() {
        return bookPageService.getAllBookPages();
    }

    // ✅ PÚBLICO
    @GetMapping("/search")
    public List<BookPage> searchByTitle(@RequestParam String title) {
        return bookPageService.searchByTitle(title);
    }

    // ✅ SOLO OWNER o ADMIN pueden eliminar
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBookPage(@PathVariable String bookId,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        // Validar permisos
        boolean isOwner = roleValidator.isOwner(bookId, user.getUserId());
        boolean isAdmin = "ADMIN".equals(user.getRole());

        if (!isOwner && !isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado para eliminar este libro");
        }

        bookPageService.deleteBookPage(bookId);
        return ResponseEntity.noContent().build();
    }
}
