package com.pogany.recipesharingforum.recipesharingjava.dao;

import com.pogany.recipesharingforum.recipesharingjava.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private Connection conn;
    private PreparedStatement getAllUsersPstmt;
    private PreparedStatement getUserByIdPstmt;
    private PreparedStatement getUserByLoginPstmt;
    private PreparedStatement getUserByEmailPstmt;
    private PreparedStatement authUserPstmt;

    public UserDaoImpl(Connection conn) throws SQLException {
        this.conn = conn;
        getAllUsersPstmt = conn.prepareStatement("SELECT * FROM user");
        getUserByIdPstmt = conn.prepareStatement("SELECT * FROM user WHERE id=?");
        getUserByLoginPstmt = conn.prepareStatement("SELECT * FROM user WHERE login=?");
        getUserByEmailPstmt = conn.prepareStatement("SELECT * FROM user WHERE email=?");
        authUserPstmt = conn.prepareStatement("SELECT * FROM user WHERE login=? AND password=?");
    }

    @Override
    public void createUser(User user) throws SQLException {
        PreparedStatement createUserPstmt = conn.prepareStatement("INSERT INTO user " +
                "(login, email, password, country, role_id) VALUES (?, ?, ?, ?, ?)");
        createUserPstmt.setString(1, user.getLogin());
        createUserPstmt.setString(2, user.getEmail());
        createUserPstmt.setString(3, user.getPassword());
        createUserPstmt.setString(4, user.getCountry());
        createUserPstmt.setLong(5, user.getRoleId());

        createUserPstmt.executeUpdate();
    }

    @Override
    public void updateUser(User user) throws SQLException {
        PreparedStatement updateUserPstmt = conn.prepareStatement("UPDATE user " +
                "SET login = ?, password = ?, email = ?, country = ?,  role_id = ? " +
                "WHERE id = ?");

        updateUserPstmt.setString(1, user.getLogin());
        updateUserPstmt.setString(2, user.getPassword());
        updateUserPstmt.setString(3, user.getEmail());
        updateUserPstmt.setString(4, user.getCountry());
        updateUserPstmt.setLong(5, user.getRoleId());

        updateUserPstmt.setLong(6, user.getId());

        updateUserPstmt.executeUpdate();
    }

    @Override
    public void removeUser(User user) throws SQLException {
        PreparedStatement removeUserPstmt = conn.prepareStatement("DELETE FROM user " +
                "WHERE id = ?");

        removeUserPstmt.setLong(1, user.getId());

        removeUserPstmt.executeUpdate();
    }

    @Override
    public List<User> findAll() throws SQLException {
        ResultSet rs = getAllUsersPstmt.executeQuery();
        List<User> users = new ArrayList<>();

        while (rs.next()) {
            users.add(new User(
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("country"),
                    rs.getInt("role_id")
            ));
        }

        return users;
    }

    @Override
    public User findById(int id) throws SQLException {
        getUserByIdPstmt.setInt(1, id);
        ResultSet rs = getUserByIdPstmt.executeQuery();

        if (rs.next()) {
            return new User(
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("country"),
                    rs.getInt("role_id")
            );
        }

        return null;
    }

    @Override
    public User findByLogin(String login) throws SQLException {
        getUserByLoginPstmt.setString(1, login);
        ResultSet rs = getUserByLoginPstmt.executeQuery();

        if (rs.next()) {
            return new User(
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("country"),
                    rs.getInt("role_id")
            );
        }

        return null;
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        getUserByEmailPstmt.setString(1, email);
        ResultSet rs = getUserByEmailPstmt.executeQuery();

        if (rs.next()) {
            return new User(
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("country"),
                    rs.getInt("role_id")
            );
        }

        return null;
    }

    @Override
    public User authUser(String login, String password) throws SQLException {
        authUserPstmt.setString(1, login);
        authUserPstmt.setString(2, password);
        ResultSet rs = authUserPstmt.executeQuery();
        User user = null;
        if (rs.next()) {
            user = new User(
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("country"),
                    rs.getInt("role_id")
            );
        }
        return user;
    }
}
