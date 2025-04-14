package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.Comment;
import com.svalero.bookreaditapi.domain.DTO.CommentTree;
import com.svalero.bookreaditapi.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<Comment> getReplies(String parentCommentId) {
        return commentRepository.findByParentCommentId(parentCommentId);
    }


    /// ////////////////////////////comment tree

    public List<CommentTree> getCommentTreeByTopic(String topicId) {
        List<Comment> all = getCommentsByTopic(topicId);

        Map<String, List<Comment>> groupedByParent = new HashMap<>();
        for (Comment c : all) {
            String parentId = c.getParentCommentId();
            groupedByParent.computeIfAbsent(parentId, k -> new ArrayList<>()).add(c);
        }

        return buildTree(null, groupedByParent);
    }

    private List<CommentTree> buildTree(String parentId, Map<String, List<Comment>> grouped) {
        List<CommentTree> result = new ArrayList<>();
        List<Comment> children = grouped.getOrDefault(parentId, new ArrayList<>());

        for (Comment c : children) {
            CommentTree node = new CommentTree(c);
            node.setReplies(buildTree(c.getCommentId(), grouped));
            result.add(node);
        }

        return result;
    }


}
