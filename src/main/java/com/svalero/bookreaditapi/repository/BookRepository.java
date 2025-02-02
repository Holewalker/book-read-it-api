package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findAll();

    List<Book> findByName(String name);

}
