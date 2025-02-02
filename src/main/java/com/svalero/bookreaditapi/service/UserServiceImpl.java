package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.domain.dto.UserDTO;
import com.svalero.bookreaditapi.exception.UserNotFoundException;
import com.svalero.bookreaditapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) throws UserNotFoundException {
        logger.info("ID User: " + id);
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }


    @Override
    public User findByUserName(String username){
        logger.info("Username User: " + username);
        return userRepository.findByUsername(username);
    }


    @Override
    public User addUser(UserDTO userDTO) {
        logger.info("User added: " + userDTO);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User newUser = new User();

        modelMapper.map(userDTO, newUser);
        newUser.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(newUser);
    }

    @Override
    public boolean deleteUser(long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        logger.info("Deleted user:" + user);
        userRepository.delete(user);
        return true;
    }

    @Override
    public User modifyUser(long id, User newUser) throws UserNotFoundException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User existingUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        logger.info("Existing User: " + existingUser);
        logger.info("New User " + newUser);
        modelMapper.map(newUser, existingUser);
        existingUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        existingUser.setId(id);
        return userRepository.save(existingUser);
    }

}
