package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.Book;
import com.svalero.bookreaditapi.domain.dto.BookDTO;
import com.svalero.bookreaditapi.exception.BookNotFoundException;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book findById(long id) throws BookNotFoundException;

    List<Book> findByName(String name);

    Book addBook(BookDTO bookDTO);

    boolean deleteBook(long id) throws  BookNotFoundException;

    Book modifyBook(long id, Book newBook) throws BookNotFoundException;

}
