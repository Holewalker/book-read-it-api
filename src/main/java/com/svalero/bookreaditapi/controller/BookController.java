package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.Book;
import com.svalero.bookreaditapi.domain.dto.BookDTO;
import com.svalero.bookreaditapi.exception.ErrorException;
import com.svalero.bookreaditapi.exception.BookNotFoundException;
import com.svalero.bookreaditapi.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import java.util.List;
import java.util.Map;

import static com.svalero.bookreaditapi.util.ErrorExceptionUtil.getErrorExceptionResponseEntity;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    @GetMapping
    public ResponseEntity<List<Book>> getBooks(@RequestParam Map<String, String> data) {
        logger.info("GET Books");
        if (data.isEmpty()) {
            logger.info("END GET Books");
            return ResponseEntity.ok(bookService.findAll());
        }
        if (data.containsKey("name")) {
            List<Book> books = bookService.findByName(data.get("name"));

            logger.info("GET Books with name");
            return ResponseEntity.ok(books);
        }
        logger.error("Bad Request");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable long id) throws BookNotFoundException {
        logger.info("GET Book");
        Book book = bookService.findById(id);
        logger.info("END GET Book");
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody BookDTO bookDTO) {
        logger.info("POST Book");
        Book newBook = bookService.addBook(bookDTO);
        logger.info("END POST Book");
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> modifyBook(@PathVariable long id, @Valid @RequestBody Book book) throws BookNotFoundException {
        logger.info("PUT Book");
        Book newBook = bookService.modifyBook(id, book);
        logger.info("END PUT Book");
        return ResponseEntity.status(HttpStatus.OK).body(newBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ErrorException> deleteBook(@PathVariable long id) throws BookNotFoundException {
        logger.info("DELETE Book");

        boolean result = bookService.deleteBook(id);
        logger.info("END DELETE Book");
        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            ErrorException error = new ErrorException(403, "El borrado no se ha permitido.");
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorException> handleBookNotFoundException(BookNotFoundException bnfe) {
        logger.error("Book no encontrado");
        ErrorException errorException = new ErrorException(404, bnfe.getMessage());
        return new ResponseEntity<>(errorException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorException> handleConstraintViolationException(ConstraintViolationException cve) {
        logger.error("Restricciones violadas");
        return getErrorExceptionResponseEntity(cve);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorException> handleException(Exception e) {
        logger.error("Error Interno " + e.getMessage());
        ErrorException error = new ErrorException(500, "Ha ocurrido un error inesperado.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorException> handleMethodArgumentNotValidException(MethodArgumentNotValidException manve) {
        logger.error("Datos introducidos erroneos");
        return getErrorExceptionResponseEntity(manve);
    }
}