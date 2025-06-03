package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.Comment;
import com.svalero.bookreaditapi.domain.DTO.CommentDTO;
import com.svalero.bookreaditapi.domain.DTO.CommentTree;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.repository.CommentRepository;
import com.svalero.bookreaditapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {


    @Autowired
    private TopicService topicService;
    @Autowired
    private BookPageService bookPageService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationService notificationService;

    public Comment createComment(Comment comment) {
        comment.setId(UUID.randomUUID().toString());
        comment.setCreatedAt(System.currentTimeMillis());
        Comment saved = commentRepository.save(comment);

        Set<String> notifiedUserIds = new HashSet<>();


        String parentId = comment.getParentCommentId();
        if (parentId != null) {
            commentRepository.findById(parentId).ifPresent(parent -> {
                if (!parent.getAuthorUserId().equals(comment.getAuthorUserId())) {
                    notifiedUserIds.add(parent.getAuthorUserId());
                }
            });

            List<Comment> siblingReplies = commentRepository.findByParentCommentId(parentId);
            siblingReplies.stream()
                    .map(Comment::getAuthorUserId)
                    .filter(userId -> !userId.equals(comment.getAuthorUserId()))
                    .forEach(notifiedUserIds::add);
        }

        topicService.getTopicById(comment.getTopicId()).ifPresent(topic -> {
            if (!topic.getAuthorUserId().equals(comment.getAuthorUserId())) {
                notifiedUserIds.add(topic.getAuthorUserId());
            }
        });
        String bookId = topicService.getTopicById(comment.getTopicId()).get().getBookId();
        for (String userId : notifiedUserIds) {
            notificationService.createNotification(
                    userId,
                    "Nuevo comentario en un tema  de \"" + bookPageService.getBookPageById(bookId).get().getTitle()+"\" en el que has participado",
                    comment.getTopicId(),
                    bookId

            );
        }

        return saved;
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
            String username = userRepository.findById(c.getAuthorUserId())
                    .map(User::getUsername)
                    .orElse("Usuario desconocido");

            CommentDTO dto = new CommentDTO(
                    c.getId(),
                    c.getTopicId(),
                    username,
                    c.getParentCommentId(),
                    c.getBody(),
                    c.getCreatedAt()
            );

            CommentTree node = new CommentTree(dto);
            node.setReplies(buildTree(c.getId(), grouped));
            result.add(node);
        }

        return result;
    }


}
