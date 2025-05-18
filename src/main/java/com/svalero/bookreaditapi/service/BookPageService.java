package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.BookPage;
import com.svalero.bookreaditapi.repository.BookPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookPageService {

    @Autowired
    private BookPageRepository bookPageRepository;

    public BookPage createBookPage(BookPage bookPage) {
        bookPage.setId(UUID.randomUUID().toString());
        bookPage.setLowercaseTitle(bookPage.getTitle().toLowerCase());
        return bookPageRepository.save(bookPage);
    }

    public Optional<BookPage> getBookPageById(String bookId) {
        return bookPageRepository.findById(bookId);
    }

    public Iterable<BookPage> getAllBookPages() {
        return bookPageRepository.findAll();
    }


    public Page<BookPage> getPaginatedBooks(Pageable pageable) {
        return bookPageRepository.findAll(pageable);
    }


    public List<BookPage> searchByTitle(String title) {
        if (title == null || title.isBlank()) {
            return List.of();
        }
        return bookPageRepository.findByLowercaseTitleContaining(title.toLowerCase());
    }

    public void deleteBookPage(String bookId) {
        bookPageRepository.deleteById(bookId);
    }

    public List<BookPage> getBooksByTag(String tag) {
        List<BookPage> allBooks = (List<BookPage>) bookPageRepository.findAll();
        return allBooks.stream()
                .filter(book -> book.getTags() != null && book.getTags().contains(tag))
                .collect(Collectors.toList());
    }


}
