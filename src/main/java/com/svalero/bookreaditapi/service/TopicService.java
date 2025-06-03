package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.DTO.TopicDTO;
import com.svalero.bookreaditapi.domain.Topic;
import com.svalero.bookreaditapi.repository.CommentRepository;
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

    @Autowired
    private CommentRepository commentRepository;

    public Topic createTopic(Topic topic) {
        topic.setId(UUID.randomUUID().toString());
        topic.setCreatedAt(System.currentTimeMillis());
        return topicRepository.save(topic);
    }

    public Iterable<Topic> getAllTopics() {
        return topicRepository.findAllOrderedByCreatedAtDesc();
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

    public List<Topic> getTopicsByBookIds(List<String> bookIds) {
        return topicRepository.findByBookIdIn(bookIds);
    }


    public List<TopicDTO> getTopicsWithCommentCount(List<Topic> topics) {
        return topics.stream()
                .map(topic -> {
                    int count = commentRepository.countByTopicId(topic.getId());
                    return new TopicDTO(topic, count);
                })
                .toList();
    }

    public List<Topic> getTopicsByUserId(String userId) {
        return topicRepository.findByAuthorUserId(userId);
    }


}
