package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.DTO.UserDTO;
import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Optional<UserDTO> getUserById(String userId) {
        return userRepository.findById(userId)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    public Optional<String> getUsernameById(String userId) {
        return userRepository.findById(userId)
                .map(User::getUsername);
    }

    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    public List<UserDTO> getAllUsers() {
        List<UserDTO> result = new ArrayList<>();
        userRepository.findAll().forEach(user ->
                result.add(modelMapper.map(user, UserDTO.class))
        );
        return result;
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public void followBook(String userId, String bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        List<String> books = Optional.ofNullable(user.getFollowedBookIds()).orElse(new ArrayList<>());
        if (!books.contains(bookId)) {
            books.add(bookId);
            user.setFollowedBookIds(books);
            userRepository.save(user);
        }
    }

    public void unfollowBook(String userId, String bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        List<String> books = Optional.ofNullable(user.getFollowedBookIds()).orElse(new ArrayList<>());
        if (books.contains(bookId)) {
            books.remove(bookId);
            user.setFollowedBookIds(books);
            userRepository.save(user);
        }
    }

    public void followTag(String userId, String tag) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
        List<String> tags = Optional.ofNullable(user.getFollowedTags()).orElse(new ArrayList<>());
        if (!tags.contains(tag)) {
            tags.add(tag);
            user.setFollowedTags(tags);
            userRepository.save(user);
        }
    }


    public void unfollowTag(String userId, String tag) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        List<String> tags = Optional.ofNullable(user.getFollowedTags()).orElse(new ArrayList<>());
        if (tags.contains(tag)) {
            tags.remove(tag);
            user.setFollowedTags(tags);
            userRepository.save(user);
        }
    }
}
