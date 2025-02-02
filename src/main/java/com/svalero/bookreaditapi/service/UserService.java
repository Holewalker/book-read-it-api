package com.svalero.bookreaditapi.service;

import com.svalero.bookreaditapi.domain.User;
import com.svalero.bookreaditapi.domain.dto.UserDTO;
import com.svalero.bookreaditapi.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(long id) throws UserNotFoundException;

    User findByUserName(String username);

    User addUser(UserDTO userDTO);

    boolean deleteUser(long id) throws  UserNotFoundException;

    User modifyUser(long id, User newUser) throws UserNotFoundException;

}
