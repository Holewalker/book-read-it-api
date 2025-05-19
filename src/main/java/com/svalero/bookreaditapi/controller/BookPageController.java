package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.BookPage;
import com.svalero.bookreaditapi.domain.RoleAssignment;
import com.svalero.bookreaditapi.domain.Topic;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.service.BookPageService;
import com.svalero.bookreaditapi.service.RoleAssignmentService;
import com.svalero.bookreaditapi.service.RoleValidatorService;
import com.svalero.bookreaditapi.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Book;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/book-pages")
public class BookPageController {

    @Autowired
    private BookPageService bookPageService;

    @Autowired
    private RoleAssignmentService roleAssignmentService;

    @Autowired
    private RoleValidatorService roleValidator;

    @Autowired
    private SecurityUtils securityUtils;



    @PostMapping
    public ResponseEntity<BookPage> createBookPage(@RequestBody BookPage bookPage,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);

        // Forzamos lowercase a las tags
        if (bookPage.getTags() != null) {
            List<String> lowerTags = bookPage.getTags().stream()
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());
            bookPage.setTags(lowerTags);
        }

        // Set owner
        bookPage.setOwnerUserId(user.getId());
        BookPage created = bookPageService.createBookPage(bookPage);

        // Crear rol OWNER para el usuario que lo crea
        RoleAssignment role = new RoleAssignment();
        role.setId(UUID.randomUUID().toString());
        role.setUserId(user.getId());
        role.setBookId(created.getId());
        role.setRole("OWNER");
        roleAssignmentService.saveRole(role);

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

    @GetMapping("/paginated")
    public ResponseEntity<Page<BookPage>> getPaginatedBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BookPage> paginatedBooks = bookPageService.getPaginatedBooks(pageable);
        return ResponseEntity.ok(paginatedBooks);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<BookPage>> searchBooks(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<BookPage> byTitle = bookPageService.searchByTitle(query);
        List<BookPage> byTag = bookPageService.getBooksByTag(query);

        Set<BookPage> combined = new HashSet<>();
        combined.addAll(byTitle);
        combined.addAll(byTag);

        List<BookPage> combinedList = new ArrayList<>(combined);

        // Crear paginación manualmente
        int start = Math.min(page * size, combinedList.size());
        int end = Math.min(start + size, combinedList.size());
        List<BookPage> pagedList = combinedList.subList(start, end);

        Page<BookPage> pageResult = new PageImpl<>(pagedList, PageRequest.of(page, size), combinedList.size());

        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/me/followed-books")
    public ResponseEntity<List<BookPage>> getFollowedBooks(@AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);
        List<String> bookIds = user.getFollowedBookIds();
        if (bookIds == null || bookIds.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<BookPage> books = new ArrayList<>();
        for (String id : bookIds) {
            bookPageService.getBookPageById(id).ifPresent(books::add);
        }

        return ResponseEntity.ok(books);
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

    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBookTags(@PathVariable String bookId,
                                            @RequestBody List<String> tags,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);
        BookPage bookPage = bookPageService.getBookPageById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));

        boolean isOwner = roleValidator.isOwner(bookId, user.getId());
        boolean isAdmin = "ADMIN".equals(user.getRole());
        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para modificar los tags de este libro. " + "" +
                    "Eres el propietario del libro? " + isOwner + " Eres admin? " + isAdmin);
        }


        List<String> normalizedTags = tags.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(tag -> !tag.isEmpty())
                .toList();

        bookPage.setTags(normalizedTags);
        BookPage updated = bookPageService.updateBook(bookPage); // reutiliza create como upsert. cosas de dynamodb. OJO con el uuid
        return ResponseEntity.ok(updated);
    }
}
