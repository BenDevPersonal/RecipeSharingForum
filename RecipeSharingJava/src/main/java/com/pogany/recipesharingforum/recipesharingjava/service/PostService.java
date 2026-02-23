package com.pogany.recipesharingforum.recipesharingjava.service;

import com.pogany.recipesharingforum.recipesharingjava.dao.PostDao;
import com.pogany.recipesharingforum.recipesharingjava.entities.Post;

import java.sql.SQLException;
import java.util.List;

public class PostService {
    private PostDao postDao;

    public PostService(PostDao postDao) {
        this.postDao = postDao;
    }

    public List<Post> getAllPosts() throws SQLException {
        return postDao.findAll();
    }
}
