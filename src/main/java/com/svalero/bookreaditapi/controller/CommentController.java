package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.Comment;
import com.svalero.bookreaditapi.domain.DTO.CommentTree;
import com.svalero.bookreaditapi.domain.Topic;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.service.CommentService;
import com.svalero.bookreaditapi.service.RoleValidatorService;
import com.svalero.bookreaditapi.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.svalero.bookreaditapi.util.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private RoleValidatorService roleValidator;

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);
        comment.setId(UUID.randomUUID().toString());
        comment.setAuthorUserId(user.getId());
        comment.setCreatedAt(System.currentTimeMillis());
        commentService.createComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<List<Comment>> getCommentsByTopic(@PathVariable String topicId) {
        return ResponseEntity.ok(commentService.getCommentsByTopic(topicId));
    }

    @PutMapping("/{commentId}/delete")
    public ResponseEntity<?> softDeleteComment(@PathVariable String commentId,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);
        Comment comment = commentService.getCommentById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Topic topic = topicService.getTopicById(comment.getTopicId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!roleValidator.hasAnyRole(topic.getBookId(), user.getId(), List.of("OWNER", "MODERATOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para eliminar comentarios.");
        }
        comment.setBody("[deleted]");
        comment.setAuthorUserId(null); // Clear author to indicate deletion
        commentService.updateComment(comment);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);
        Comment existing = commentService.getCommentById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Topic topic = topicService.getTopicById(existing.getTopicId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!roleValidator.hasAnyRole(topic.getBookId(), user.getId(), List.of("OWNER", "MODERATOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para eliminar comentarios.");
        }

        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/replies/{parentId}")
    public ResponseEntity<List<Comment>> getReplies(@PathVariable String parentId) {
        return ResponseEntity.ok(commentService.getReplies(parentId));
    }

    @GetMapping("/tree/{topicId}")
    public ResponseEntity<List<CommentTree>> getCommentTree(@PathVariable String topicId) {
        List<CommentTree> tree = commentService.getCommentTreeByTopic(topicId);
        return ResponseEntity.ok(tree);
    }

}
