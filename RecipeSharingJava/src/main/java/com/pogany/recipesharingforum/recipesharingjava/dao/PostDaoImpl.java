package com.pogany.recipesharingforum.recipesharingjava.dao;

import com.pogany.recipesharingforum.recipesharingjava.entities.Post;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostDaoImpl implements PostDao {
    private Connection conn;
    private PreparedStatement getAllPostsPstmt;
    private PreparedStatement getPostsByUserIdPstmt;
    private PreparedStatement getPostByIdPstmt;

    public PostDaoImpl(Connection conn) throws SQLException {
        this.conn = conn;
        getAllPostsPstmt = conn.prepareStatement("SELECT * FROM post");
        getPostsByUserIdPstmt = conn.prepareStatement("SELECT * FROM post WHERE user_id=?");
        getPostByIdPstmt = conn.prepareStatement("SELECT * FROM post WHERE id=?");

    }

    @Override
    public void createPost(Post post) throws SQLException {
        PreparedStatement createPostPstmt = conn.prepareStatement("INSERT INTO post " +
                "(user_id, title, content, creation_date, update_date) VALUES (?, ?, ?, ?, ?)");

        createPostPstmt.setInt(1, post.getUserId());
        createPostPstmt.setString(2, post.getTitle());
        createPostPstmt.setString(3, post.getContent());
        createPostPstmt.setDate(4, Date.valueOf(LocalDate.now()));
        createPostPstmt.setDate(5, Date.valueOf(LocalDate.now()));

        createPostPstmt.executeUpdate();
    }

    @Override
    public void updatePost(Post post) throws SQLException {
        PreparedStatement updatePostPstmt = conn.prepareStatement("UPDATE post " +
                "SET user_id = ?, title = ?, content = ?, creation_date = ?, update_date = ? " +
                "WHERE id = ?");

        updatePostPstmt.setInt(1, post.getUserId());
        updatePostPstmt.setString(2, post.getTitle());
        updatePostPstmt.setString(3, post.getContent());
        updatePostPstmt.setDate(4, post.getCreationDate());
        updatePostPstmt.setDate(5, Date.valueOf(LocalDate.now()));

        updatePostPstmt.setInt(6, post.getId());

        updatePostPstmt.executeUpdate();
    }

    @Override
    public void removePost(Post post) throws SQLException {
        PreparedStatement removePostPstmt = conn.prepareStatement("DELETE FROM post " +
                "WHERE id = ?");

        removePostPstmt.setInt(1, post.getId());

        removePostPstmt.executeUpdate();
    }

    @Override
    public List<Post> findAll() throws SQLException {
        ResultSet rs = getAllPostsPstmt.executeQuery();
        List<Post> posts = new ArrayList<>();

        while (rs.next()) {
            posts.add(new Post(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getDate("creation_date"),
                    rs.getDate("update_date")
            ));
        }

        return posts;
    }

    @Override
    public List<Post> findByUserId(int userId) throws SQLException {
        getPostsByUserIdPstmt.setInt(1, userId);
        ResultSet rs = getPostsByUserIdPstmt.executeQuery();

        List<Post> posts = new ArrayList<>();

        while (rs.next()) {
            posts.add(new Post(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getDate("creation_date"),
                    rs.getDate("update_date")
            ));
        }

        return posts;
    }

    @Override
    public Post findById(int id) throws SQLException {
        getPostByIdPstmt.setInt(1, id);
        ResultSet rs = getPostByIdPstmt.executeQuery();

        if (rs.next()) {
            return new Post(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getDate("creation_date"),
                    rs.getDate("update_date")
            );
        }

        return null;
    }
}
