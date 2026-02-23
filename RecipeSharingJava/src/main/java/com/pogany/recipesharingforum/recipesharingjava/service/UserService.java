package com.pogany.recipesharingforum.recipesharingjava.service;

import com.pogany.recipesharingforum.recipesharingjava.dao.UserDao;
import com.pogany.recipesharingforum.recipesharingjava.entities.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao){
        this.userDao = userDao;
    }

    public List<User> getAllUsers() throws SQLException {
        return userDao.findAll();
    }

    public User login(String login, String password) throws SQLException {
        return userDao.authUser(login, password);
    }
}
