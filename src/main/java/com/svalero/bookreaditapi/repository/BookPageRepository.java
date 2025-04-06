package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.BookPage;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
@EnableScan
public interface BookPageRepository extends CrudRepository<BookPage, String> {
    List<BookPage> findByTitleContaining(String title);

    List<BookPage> findByTagsContains(String tag);
}
