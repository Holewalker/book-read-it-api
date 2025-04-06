package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.DTO.UserDTO;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{userId}/follow-book/{bookId}")
    public ResponseEntity<Void> followBook(@PathVariable String userId, @PathVariable String bookId) {
        userService.followBook(userId, bookId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/unfollow-book/{bookId}")
    public ResponseEntity<Void> unfollowBook(@PathVariable String userId, @PathVariable String bookId) {
        userService.unfollowBook(userId, bookId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/follow-tag/{tag}")
    public ResponseEntity<Void> followTag(@PathVariable String userId, @PathVariable String tag) {
        userService.followTag(userId, tag);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/unfollow-tag/{tag}")
    public ResponseEntity<Void> unfollowTag(@PathVariable String userId, @PathVariable String tag) {
        userService.unfollowTag(userId, tag);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followed-books")
    public ResponseEntity<List<String>> getFollowedBooks(@PathVariable String userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(user.getFollowedBookIds()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/followed-tags")
    public ResponseEntity<List<String>> getFollowedTags(@PathVariable String userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(user.getFollowedTags()))
                .orElse(ResponseEntity.notFound().build());
    }
}

