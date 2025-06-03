package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.Comment;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
@EnableScanCount
public interface CommentRepository extends CrudRepository<Comment, String> {
    List<Comment> findByTopicId(String topicId); // necesita GSI topicId
    List<Comment> findByParentCommentId(String parentCommentId); // GSI opcional
    int countByTopicId(String topicId);


}
