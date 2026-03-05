package com.pogany.recipesharingforum.recipesharingjava.dao;



import com.pogany.recipesharingforum.recipesharingjava.entities.Feedback;

import java.sql.SQLException;
import java.util.List;

public interface FeedbackDao {
    void createComment(Feedback feedback)throws SQLException;
    void updateComment(Feedback feedback)throws SQLException;
    void removeComment(Feedback feedback)throws SQLException;

    Feedback findById(int id)throws SQLException;
    List<Feedback> findByPostId(int postId)throws SQLException;
    List<Feedback> findByUserId(int userId)throws SQLException;
}
