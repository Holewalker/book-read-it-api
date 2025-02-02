package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.Reply;
import com.svalero.bookreaditapi.domain.dto.ReplyDTO;
import com.svalero.bookreaditapi.exception.ErrorException;
import com.svalero.bookreaditapi.exception.PostNotFoundException;
import com.svalero.bookreaditapi.exception.ReplyNotFoundException;
import com.svalero.bookreaditapi.exception.UserNotFoundException;
import com.svalero.bookreaditapi.service.ReplyService;
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
@RequestMapping("/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    private final Logger logger = LoggerFactory.getLogger(ReplyController.class);

    @GetMapping
    public ResponseEntity<List<Reply>> getReplies(@RequestParam Map<String, String> data) throws PostNotFoundException, UserNotFoundException {
        logger.info("GET Replies");
        if (data.isEmpty()) {
            logger.info("END GET Replies");
            return ResponseEntity.ok(replyService.findAll());
        }
        if (data.containsKey("userId")) {
            long userId = Long.parseLong(data.get("userId"));
            List<Reply> replies = replyService.findByUserId(userId);
            logger.info("GET Replies by User ID");
            return ResponseEntity.ok(replies);
        }
        if (data.containsKey("postId")) {
            long postId = Long.parseLong(data.get("postId"));
            List<Reply> replies = replyService.findByPostId(postId);
            logger.info("GET Replies by Post ID");
            return ResponseEntity.ok(replies);
        }
        logger.error("Bad Request");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reply> getReply(@PathVariable long id) throws ReplyNotFoundException {
        logger.info("GET Reply");
        Reply reply = replyService.findById(id);
        logger.info("END GET Reply");
        return ResponseEntity.ok(reply);
    }

    @PostMapping
    public ResponseEntity<Reply> addReply(@Valid @RequestBody ReplyDTO replyDTO) throws UserNotFoundException {
        logger.info("POST Reply");
        Reply newReply = replyService.addReply(replyDTO);
        logger.info("END POST Reply");
        return ResponseEntity.status(HttpStatus.CREATED).body(newReply);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reply> modifyReply(@PathVariable long id, @Valid @RequestBody Reply reply) throws ReplyNotFoundException {
        logger.info("PUT Reply");
        Reply updatedReply = replyService.modifyReply(id, reply);
        logger.info("END PUT Reply");
        return ResponseEntity.status(HttpStatus.OK).body(updatedReply);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ErrorException> deleteReply(@PathVariable long id) throws ReplyNotFoundException {
        logger.info("DELETE Reply");

        boolean result = replyService.deleteReply(id);
        logger.info("END DELETE Reply");
        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            ErrorException error = new ErrorException(403, "El borrado no se ha permitido.");
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }
    }

    @ExceptionHandler(ReplyNotFoundException.class)
    public ResponseEntity<ErrorException> handleReplyNotFoundException(ReplyNotFoundException rnfe) {
        logger.error("Reply no encontrado");
        ErrorException errorException = new ErrorException(404, rnfe.getMessage());
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
