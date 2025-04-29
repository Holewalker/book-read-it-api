package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.Topic;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface TopicRepository extends CrudRepository<Topic, String> {
    List<Topic> findByBookId(String bookId);

    List<Topic> findByBookIdIn(List<String> bookIds);

}

