package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.DTO.UserDTO;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.service.UserService;
import com.svalero.bookreaditapi.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtils securityUtils;

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // ---- LIBROS SEGUIDOS ----

    @PutMapping("/me/follow-book/{bookId}")
    public ResponseEntity<Void> followBook(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable String bookId) {
        User user = securityUtils.getCurrentUser(userDetails);
        userService.followBook(user.getId(), bookId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/unfollow-book/{bookId}")
    public ResponseEntity<Void> unfollowBook(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable String bookId) {
        User user = securityUtils.getCurrentUser(userDetails);
        userService.unfollowBook(user.getId(), bookId);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/me/follow-tag/{tag}")
    public ResponseEntity<Void> followTag(@AuthenticationPrincipal UserDetails userDetails,
                                          @PathVariable String tag) {
        User user = securityUtils.getCurrentUser(userDetails);
        userService.followTag(user.getId(), tag);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/unfollow-tag/{tag}")
    public ResponseEntity<Void> unfollowTag(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable String tag) {
        User user = securityUtils.getCurrentUser(userDetails);
        userService.unfollowTag(user.getId(), tag);
        return ResponseEntity.ok().build();
    }

    // ---- CONSULTA DE FOLLOW ----

    @GetMapping("/me/followed-books")
    public ResponseEntity<List<String>> getFollowedBooks(@AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);
        return ResponseEntity.ok(user.getFollowedBookIds());
    }

    @GetMapping("/me/followed-tags")
    public ResponseEntity<List<String>> getFollowedTags(@AuthenticationPrincipal UserDetails userDetails) {
        User user = securityUtils.getCurrentUser(userDetails);
        return ResponseEntity.ok(user.getFollowedTags());
    }
}
