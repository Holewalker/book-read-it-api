package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.Follow;
import com.svalero.bookreaditapi.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping
    public ResponseEntity<Follow> followBook(@RequestBody Follow follow) {
        Follow created = followService.followBook(follow);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{followId}")
    public ResponseEntity<Void> unfollowBook(@PathVariable String followId) {
        followService.unfollowBook(followId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<Follow> getFollowsByUser(@PathVariable String userId) {
        return followService.getFollowsByUser(userId);
    }

    @GetMapping("/book/{bookId}")
    public List<Follow> getFollowsByBook(@PathVariable String bookId) {
        return followService.getFollowsByBook(bookId);
    }
}
