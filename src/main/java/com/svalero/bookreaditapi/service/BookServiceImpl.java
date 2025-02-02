package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.Book;
import com.svalero.bookreaditapi.domain.dto.BookDTO;
import com.svalero.bookreaditapi.exception.BookNotFoundException;
import com.svalero.bookreaditapi.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(long id) throws BookNotFoundException {
        logger.info("ID Book: " + id);
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }


    @Override
    public List<Book> findByName(String bookname){
        logger.info("Bookname Book: " + bookname);
        return bookRepository.findByName(bookname);
    }


    @Override
    public Book addBook(BookDTO bookDTO) {
        logger.info("Book added: " + bookDTO);
        Book newBook = new Book();
        modelMapper.map(bookDTO, newBook);
        newBook.setPosts(new ArrayList<>());
        return bookRepository.save(newBook);
    }

    @Override
    public boolean deleteBook(long id) throws BookNotFoundException {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        logger.info("Deleted book:" + book);
        bookRepository.delete(book);
        return true;
    }

    @Override
    public Book modifyBook(long id, Book newBook) throws BookNotFoundException {
        Book existingBook = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        logger.info("Existing Book: " + existingBook);
        logger.info("New Book " + newBook);
        modelMapper.map(newBook, existingBook);
        existingBook.setId(id);
        return bookRepository.save(existingBook);
    }

}
