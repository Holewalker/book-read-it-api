package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.Post;
import com.svalero.bookreaditapi.domain.dto.PostDTO;
import com.svalero.bookreaditapi.exception.BookNotFoundException;
import com.svalero.bookreaditapi.exception.ErrorException;
import com.svalero.bookreaditapi.exception.PostNotFoundException;
import com.svalero.bookreaditapi.exception.UserNotFoundException;
import com.svalero.bookreaditapi.service.PostService;
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
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(@RequestParam Map<String, String> data) throws UserNotFoundException, BookNotFoundException {
        logger.info("GET Posts");
        if (data.isEmpty()) {
            logger.info("END GET Posts");
            return ResponseEntity.ok(postService.findAll());
        }
        if (data.containsKey("userId")) {
            long userId = Long.parseLong(data.get("userId"));
            List<Post> posts = postService.findByUserId(userId);
            logger.info("GET Posts by User ID");
            return ResponseEntity.ok(posts);
        }
        if (data.containsKey("bookId")) {
            long bookId = Long.parseLong(data.get("bookId"));
            List<Post> posts = postService.findByBookId(bookId);
            logger.info("GET Posts by Book ID");
            return ResponseEntity.ok(posts);
        }
        logger.error("Bad Request");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable long id) throws PostNotFoundException {
        logger.info("GET Post");
        Post post = postService.findById(id);
        logger.info("END GET Post");
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> addPost(@Valid @RequestBody PostDTO postDTO) throws UserNotFoundException {
        logger.info("POST Post");
        Post newPost = postService.addPost(postDTO);
        logger.info("END POST Post");
        return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> modifyPost(@PathVariable long id, @Valid @RequestBody Post post) throws PostNotFoundException {
        logger.info("PUT Post");
        Post updatedPost = postService.modifyPost(id, post);
        logger.info("END PUT Post");
        return ResponseEntity.status(HttpStatus.OK).body(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ErrorException> deletePost(@PathVariable long id) throws PostNotFoundException {
        logger.info("DELETE Post");

        boolean result = postService.deletePost(id);
        logger.info("END DELETE Post");
        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            ErrorException error = new ErrorException(403, "El borrado no se ha permitido.");
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorException> handlePostNotFoundException(PostNotFoundException pnfe) {
        logger.error("Post no encontrado");
        ErrorException errorException = new ErrorException(404, pnfe.getMessage());
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