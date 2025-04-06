package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.Comment;
import com.svalero.bookreaditapi.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(Comment comment) {
        comment.setCommentId(UUID.randomUUID().toString());
        comment.setCreatedAt(System.currentTimeMillis());
        return commentRepository.save(comment);
    }
    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Optional<Comment> getCommentById(String commentId) {
        return commentRepository.findById(commentId);
    }

    public List<Comment> getCommentsByTopic(String topicId) {
        return commentRepository.findByTopicId(topicId);
    }

    public List<Comment> getRepliesByComment(String parentCommentId) {
        return commentRepository.findByParentCommentId(parentCommentId);
    }

    public void deleteComment(String commentId) {
        commentRepository.deleteById(commentId);
    }
}
