package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.Book;
import com.svalero.bookreaditapi.domain.Post;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.domain.dto.PostDTO;
import com.svalero.bookreaditapi.exception.BookNotFoundException;
import com.svalero.bookreaditapi.exception.PostNotFoundException;
import com.svalero.bookreaditapi.exception.UserNotFoundException;
import com.svalero.bookreaditapi.repository.BookRepository;
import com.svalero.bookreaditapi.repository.PostRepository;
import com.svalero.bookreaditapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post findById(long id) throws PostNotFoundException {
        logger.info("ID Post: " + id);
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }


    @Override
    public Post addPost(PostDTO postDTO) throws UserNotFoundException {
        logger.info("Post added: " + postDTO);
        Post newPost = new Post();
        modelMapper.map(postDTO, newPost);
        newPost.setUserPost(userRepository.findById(postDTO.getUserId()).orElseThrow(UserNotFoundException::new));
        newPost.setBookPost(bookRepository.findById(postDTO.getBookId()).orElseThrow(UserNotFoundException::new));
        newPost.setDate(LocalDate.now());
        newPost.setReplies(new ArrayList<>());
        return postRepository.save(newPost);
    }

    @Override
    public boolean deletePost(long id) throws PostNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        logger.info("Deleted post:" + post);
        postRepository.delete(post);
        return true;
    }

    @Override
    public Post modifyPost(long id, Post newPost) throws PostNotFoundException {
        Post existingPost = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        logger.info("Existing Post: " + existingPost);
        logger.info("New Post " + newPost);
        modelMapper.map(newPost, existingPost);
        existingPost.setId(id);
        return postRepository.save(existingPost);
    }


    @Override
    public List<Post> findByUserId(long userId) throws UserNotFoundException {
        logger.info("userId Post: " + userId);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return postRepository.findByUserPost(user);
    }

    @Override
    public List<Post> findByBookId(long bookId) throws BookNotFoundException {
      logger.info("bookId Post: " + bookId);
      Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        return postRepository.findByBookPost(book);
    }

}
