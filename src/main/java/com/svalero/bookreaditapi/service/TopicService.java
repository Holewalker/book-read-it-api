package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.Topic;
import com.svalero.bookreaditapi.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public Topic createTopic(Topic topic) {
        topic.setId(UUID.randomUUID().toString());
        topic.setCreatedAt(System.currentTimeMillis());
        return topicRepository.save(topic);
    }

    public Iterable<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Optional<Topic> getTopicById(String topicId) {
        return topicRepository.findById(topicId);
    }

    public List<Topic> getTopicsByBookId(String bookId) {
        return topicRepository.findByBookId(bookId);
    }

    public void deleteTopic(String topicId) {
        topicRepository.deleteById(topicId);
    }

    public Topic updateTopic(Topic topic) {
        return topicRepository.save(topic);
    }
}
