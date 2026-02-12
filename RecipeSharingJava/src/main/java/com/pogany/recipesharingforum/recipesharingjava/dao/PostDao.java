package com.pogany.recipesharingforum.recipesharingjava.dao;



import com.pogany.recipesharingforum.recipesharingjava.entities.Post;

import java.sql.SQLException;
import java.util.List;

public interface PostDao {
    void createPost(Post post) throws SQLException;
    void updatePost(Post post) throws SQLException;
    void removePost(Post post) throws SQLException;

    List<Post> findAll() throws SQLException;
    List<Post> findByUserId(int userId) throws SQLException;

    Post findById(int id) throws SQLException;
}
