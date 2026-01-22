package com.pogany.recipesharingforum.dao;



import com.pogany.recipesharingforum.entities.Comment;

import java.sql.SQLException;
import java.util.List;

public interface CommentDao {
    void createComment(Comment Comment) throws SQLException;
    void updateComment(Comment Comment) throws SQLException;
    void removeComment(Comment Comment) throws SQLException;

    Comment findById(int id) throws SQLException;
    
    List<Comment> findByPostId(int postId);
    List<Comment> findByUserId(int userId) throws SQLException;

}
