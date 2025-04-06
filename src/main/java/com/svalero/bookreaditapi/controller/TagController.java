package com.svalero.bookreaditapi.controller;

import com.svalero.bookreaditapi.domain.Tag;
import com.svalero.bookreaditapi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag created = tagService.createTag(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<Tag> getTagById(@PathVariable String tagId) {
        return tagService.getTagById(tagId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Tag> getTagByName(@RequestParam String name) {
        return tagService.getTagByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Iterable<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable String tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }
}
