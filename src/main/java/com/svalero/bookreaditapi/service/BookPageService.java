package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.BookPage;
import com.svalero.bookreaditapi.repository.BookPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookPageService {

    @Autowired
    private BookPageRepository bookPageRepository;

    public BookPage createBookPage(BookPage bookPage) {
        bookPage.setBookId(UUID.randomUUID().toString());
        return bookPageRepository.save(bookPage);
    }

    public Optional<BookPage> getBookPageById(String bookId) {
        return bookPageRepository.findById(bookId);
    }

    public Iterable<BookPage> getAllBookPages() {
        return bookPageRepository.findAll();
    }

    public List<BookPage> searchByTitle(String title) {
        return bookPageRepository.findByTitleContaining(title);
    }

    public void deleteBookPage(String bookId) {
        bookPageRepository.deleteById(bookId);
    }
}
