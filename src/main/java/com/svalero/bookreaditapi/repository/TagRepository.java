package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.Tag;
import com.svalero.bookreaditapi.domain.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface TagRepository extends CrudRepository<Tag, String> {
    Optional<Tag> findByName(String name);
}
