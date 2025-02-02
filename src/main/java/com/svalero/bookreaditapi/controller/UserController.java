package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.domain.dto.UserDTO;
import com.svalero.bookreaditapi.exception.ErrorException;
import com.svalero.bookreaditapi.exception.UserNotFoundException;
import com.svalero.bookreaditapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.svalero.bookreaditapi.util.ErrorExceptionUtil.getErrorExceptionResponseEntity;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestParam Map<String, String> data) {
        logger.info("GET User");
        if (data.isEmpty()) {
            logger.info("END GET User");
            return ResponseEntity.ok(userService.findAll());
        }
        if (data.containsKey("username")) {
            List<User> users = new ArrayList<>();
            users.add(userService.findByUserName(data.get("username")));
            logger.info("GET User with name");
            return ResponseEntity.ok(users);
        }
        logger.error("Bad Request");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) throws UserNotFoundException {
        logger.info("GET User");
        User user = userService.findById(id);
        logger.info("END GET User");
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@Valid @RequestBody UserDTO userDTO) {
        logger.info("POST User");
        User newUser = userService.addUser(userDTO);
        logger.info("END POST User");
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> modifyUser(@PathVariable long id, @Valid @RequestBody User user) throws UserNotFoundException {
        logger.info("PUT User");
        User newUser = userService.modifyUser(id, user);
        logger.info("END PUT User");
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ErrorException> deleteUser(@PathVariable long id) throws UserNotFoundException {
        logger.info("DELETE User");

        boolean result = userService.deleteUser(id);
        logger.info("END DELETE User");
        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            ErrorException error = new ErrorException(403, "El borrado no se ha permitido.");
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorException> handleUserNotFoundException(UserNotFoundException pnfe) {
        logger.error("User no encontrado");
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