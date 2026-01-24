package com.pogany.recipesharingforum.dao;

import com.pogany.recipesharingforum.entities.Comment;
import com.pogany.recipesharingforum.entities.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDaoImpl implements CommentDao {
    private Connection conn;
    private PreparedStatement getCommentsByUserIdPstmt;
    private PreparedStatement getCommentsByPostIdPstmt;
    private PreparedStatement getCommentByIdPstmt;

    public CommentDaoImpl(Connection conn) throws SQLException {
        this.conn = conn;
        getCommentByIdPstmt = conn.prepareStatement("SELECT * FROM feedback WHERE id=?");
        getCommentsByUserIdPstmt = conn.prepareStatement("SELECT * FROM feedback WHERE user_id=?");
        getCommentsByPostIdPstmt = conn.prepareStatement("SELECT * FROM feedback WHERE post_id=?");
    }

    @Override
    public void createComment(Comment comment) throws SQLException {
        PreparedStatement createCommentPstmt = conn.prepareStatement("INSERT INTO feedback " +
                "(user_id, post_id, rating, content) VALUES (?, ?, ?, ?)");

        createCommentPstmt.setInt(1, comment.getUserId());
        createCommentPstmt.setInt(2, comment.getPostId());
        createCommentPstmt.setInt(3, comment.getRating());
        createCommentPstmt.setString(4, comment.getContent());

        createCommentPstmt.executeUpdate();
    }

    @Override
    public void updateComment(Comment comment) throws SQLException {
        PreparedStatement updateCommentPstmt = conn.prepareStatement("UPDATE feedback " +
                "SET user_id = ?, post_id = ?, rating = ?, content = ?" +
                "WHERE id = ?");

        updateCommentPstmt.setInt(1, comment.getUserId());
        updateCommentPstmt.setInt(2, comment.getPostId());
        updateCommentPstmt.setInt(3, comment.getRating());
        updateCommentPstmt.setString(4, comment.getContent());

        updateCommentPstmt.setInt(5, comment.getId());

        updateCommentPstmt.executeUpdate();
    }

    @Override
    public void removeComment(Comment comment) throws SQLException {
        PreparedStatement removeCommentPstmt = conn.prepareStatement("DELETE FROM feedback " +
                "WHERE id = ?");

        removeCommentPstmt.setInt(1, comment.getId());

        removeCommentPstmt.executeUpdate();
    }

    @Override
    public Comment findById(int id) throws SQLException {
        getCommentByIdPstmt.setInt(1, id);
        ResultSet rs = getCommentByIdPstmt.executeQuery();

        if (rs.next()) {
            return new Comment(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("post_id"),
                    rs.getInt("rating"),
                    rs.getString("content")
            );
        }

        return null;
    }

    @Override
    public List<Comment> findByPostId(int postId) throws SQLException {
        getCommentsByPostIdPstmt.setInt(1, postId);
        ResultSet rs = getCommentsByPostIdPstmt.executeQuery();

        List<Comment> comments = new ArrayList<>();

        while (rs.next()) {
            comments.add(new Comment(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("post_id"),
                    rs.getInt("rating"),
                    rs.getString("content")
            ));
        }

        return comments;
    }

    @Override
    public List<Comment> findByUserId(int userId) throws SQLException {
        getCommentsByUserIdPstmt.setInt(1, userId);
        ResultSet rs = getCommentsByUserIdPstmt.executeQuery();

        List<Comment> comments = new ArrayList<>();

        while (rs.next()) {
            comments.add(new Comment(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("post_id"),
                    rs.getInt("rating"),
                    rs.getString("content")
            ));
        }

        return comments;
    }
}
