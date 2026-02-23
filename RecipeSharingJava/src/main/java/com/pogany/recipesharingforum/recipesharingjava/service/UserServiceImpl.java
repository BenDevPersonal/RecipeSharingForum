package com.pogany.recipesharingforum.recipesharingjava.service;

import com.pogany.recipesharingforum.recipesharingjava.dao.UserDao;
import com.pogany.recipesharingforum.recipesharingjava.entities.User;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService{

    private UserDao userDao;

    public UserServiceImpl(UserDao userDao){
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        return userDao.findAll();
    }

    @Override
    public User login(String login, String password) throws SQLException {
        return userDao.authUser(login, password);
    }
}
