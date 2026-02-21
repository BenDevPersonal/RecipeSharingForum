package com.pogany.recipesharingforum.recipesharingjava.service;

import com.pogany.recipesharingforum.recipesharingjava.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    List<User> getAllUsers() throws SQLException;
    User login(String login, String password) throws SQLException;
}
