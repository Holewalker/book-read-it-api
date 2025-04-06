package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.Tag;
import com.svalero.bookreaditapi.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag createTag(Tag tag) {
        tag.setTagId(UUID.randomUUID().toString());
        return tagRepository.save(tag);
    }

    public Optional<Tag> getTagById(String tagId) {
        return tagRepository.findById(tagId);
    }

    public Optional<Tag> getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    public Iterable<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public void deleteTag(String tagId) {
        tagRepository.deleteById(tagId);
    }
}
