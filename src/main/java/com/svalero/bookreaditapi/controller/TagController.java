package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.BookPage;
import com.svalero.bookreaditapi.service.BookPageService;
import com.svalero.bookreaditapi.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private BookPageService bookPageService;

    @GetMapping("/{tag}/books")
    public ResponseEntity<List<BookPage>> getBooksByTag(@PathVariable String tag) {

        List<BookPage> books = bookPageService.getBooksByTag(tag);
        return ResponseEntity.ok(books);
    }

}
