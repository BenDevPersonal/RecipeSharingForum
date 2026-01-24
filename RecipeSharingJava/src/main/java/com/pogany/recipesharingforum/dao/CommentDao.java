package com.pogany.recipesharingforum.dao;



import com.pogany.recipesharingforum.entities.Comment;

import java.sql.SQLException;
import java.util.List;

public interface CommentDao {
    void createComment(Comment comment) throws SQLException;
    void updateComment(Comment comment) throws SQLException;
    void removeComment(Comment comment) throws SQLException;

    Comment findById(int id) throws SQLException;

    List<Comment> findByPostId(int postId) throws SQLException;
    List<Comment> findByUserId(int userId) throws SQLException;

}
