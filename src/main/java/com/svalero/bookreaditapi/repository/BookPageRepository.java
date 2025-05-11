package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.BookPage;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@EnableScan
@EnableScanCount
public interface BookPageRepository extends PagingAndSortingRepository<BookPage, String> {
    List<BookPage> findByTitleContaining(String title);


    Page<BookPage> findAll(Pageable pageable);

    List<BookPage> findByTagsContains(String tag);
}
