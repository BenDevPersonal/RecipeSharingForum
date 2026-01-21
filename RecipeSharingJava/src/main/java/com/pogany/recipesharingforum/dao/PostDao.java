package com.pogany.recipesharingforum.dao;



import com.pogany.recipesharingforum.entities.Post;

import java.sql.SQLException;
import java.util.List;

public interface PostDao {
    void createPost(Post Post) throws SQLException;
    void updatePost(Post Post) throws SQLException;
    void removePost(Post Post) throws SQLException;

    List<Post> findAll() throws SQLException;
    Post findById(int id) throws SQLException;
    List<Post> findByUserId(int userId) throws SQLException;

}
