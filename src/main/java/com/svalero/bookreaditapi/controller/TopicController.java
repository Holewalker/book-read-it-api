package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.Topic;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.repository.UserRepository;
import com.svalero.bookreaditapi.service.RoleValidatorService;
import com.svalero.bookreaditapi.service.TopicService;
import com.svalero.bookreaditapi.service.BookPageService;
import com.svalero.bookreaditapi.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private RoleValidatorService roleValidator;

    @Autowired
    private BookPageService bookPageService;

    @Autowired
    private SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);

        if (bookPageService.getBookPageById(topic.getBookId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }

        topic.setCreatorUserId(user.getUserId());
        topic.setCreatedAt(System.currentTimeMillis());

        Topic created = topicService.createTopic(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/book/{bookId}")
    public List<Topic> getTopicsByBook(@PathVariable String bookId) {
        return topicService.getTopicsByBookId(bookId);
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<Topic> getTopic(@PathVariable String topicId) {
        return topicService.getTopicById(topicId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{topicId}")
    public ResponseEntity<?> updateTopic(@PathVariable String topicId,
                                         @RequestBody Topic topicRequest,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);
        Topic topic = topicService.getTopicById(topicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!roleValidator.hasAnyRole(topic.getBookId(), user.getUserId(), List.of("OWNER", "MODERATOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para editar este tema.");
        }

        topic.setTitle(topicRequest.getTitle());
        return ResponseEntity.ok(topicService.updateTopic(topic));
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<?> deleteTopic(@PathVariable String topicId,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);
        Topic topic = topicService.getTopicById(topicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!roleValidator.hasAnyRole(topic.getBookId(), user.getUserId(), List.of("OWNER", "MODERATOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para eliminar este tema.");
        }

        topicService.deleteTopic(topicId);
        return ResponseEntity.noContent().build();
    }
}


