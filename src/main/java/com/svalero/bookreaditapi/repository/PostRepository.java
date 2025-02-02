package com.svalero.bookreaditapi.repository;

import com.svalero.bookreaditapi.domain.Book;
import com.svalero.bookreaditapi.domain.Post;
import com.svalero.bookreaditapi.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAll();


    List<Post> findByBookPost(Book book);

    List<Post> findByUserPost(User user);

}
