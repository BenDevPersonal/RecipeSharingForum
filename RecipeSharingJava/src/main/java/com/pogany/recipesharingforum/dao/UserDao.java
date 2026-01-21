package com.pogany.recipesharingforum.dao;

import com.pogany.recipesharingforum.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    void createUser(User user) throws SQLException;
    void updateUser(User user) throws SQLException;
    void removeUser(User user) throws SQLException;

    List<User> findAll() throws SQLException;
    User findById(int id) throws SQLException;
    User findByLogin(String login) throws SQLException;
    User findByEmail(String email) throws SQLException;
}
