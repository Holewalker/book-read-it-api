package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.Follow;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@
        EnableScan
public interface FollowRepository extends CrudRepository<Follow, String> {
    List<Follow> findByUserId(String userId);
    List<Follow> findByBookId(String bookId);
}
