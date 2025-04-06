package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.Comment;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface CommentRepository extends CrudRepository<Comment, String> {
    List<Comment> findByTopicId(String topicId); // necesita GSI topicId
    List<Comment> findByParentCommentId(String parentCommentId); // GSI opcional
}
