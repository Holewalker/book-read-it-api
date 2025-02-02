package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.Post;
import com.svalero.bookreaditapi.domain.dto.PostDTO;
import com.svalero.bookreaditapi.exception.BookNotFoundException;
import com.svalero.bookreaditapi.exception.PostNotFoundException;
import com.svalero.bookreaditapi.exception.UserNotFoundException;

import java.util.List;

public interface PostService {

    List<Post> findAll();

    Post findById(long id) throws PostNotFoundException;

    Post addPost(PostDTO postDTO) throws UserNotFoundException;

    boolean deletePost(long id) throws  PostNotFoundException;

    Post modifyPost(long id, Post newPost) throws PostNotFoundException;

    List<Post> findByUserId(long userId) throws UserNotFoundException;

    List<Post> findByBookId(long bookId) throws BookNotFoundException;
}
