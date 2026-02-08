package com.pogany.recipesharingforum.dao;

import com.pogany.recipesharingforum.entities.Post;
import com.pogany.recipesharingforum.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PostDaoImplTest extends BaseDaoTest {
    private Connection conn;
    private UserDaoImpl userDao;
    private PostDaoImpl postDao;
    private int userID;

    @BeforeEach
    void setup() throws Exception {
        conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
        conn.setAutoCommit(false);
        userDao = new UserDaoImpl(conn);
        postDao = new PostDaoImpl(conn);

        userDao.createUser(new User("tu", "pw", "tu@test.com", "US"));

        userID = userDao.findByLogin("tu").getId();
    }

    @AfterEach
    void rollback() throws Exception {
        conn.rollback();
        conn.close();
    }

    @Test
    void testCreatePost() throws SQLException {
        Post post = new Post(userID, "Test title", "Test content", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()));

        postDao.createPost(post);
        Post found = postDao.findByUserId(userID).getLast();

        assertNotNull(found);
        assertEquals("Test title", found.getTitle());
    }

    @Test
    void testFindAllPosts() throws SQLException {
        Post post = new Post(userID, "Test title", "Test content", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()));
        Post post2 = new Post(userID, "Test title 2", "Test content 2", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()));

        postDao.createPost(post);
        postDao.createPost(post2);

        List<Post> posts = postDao.findAll();

        assertEquals(2, posts.size());
    }

    @Test
    void testFindPostsByUserId() throws SQLException {
        userDao.createUser(new User("tu2", "pw2", "tu2@test.com", "UK"));
        int userID2 = userDao.findByLogin("tu2").getId();

        Post post = new Post(userID, "Test title", "Test content", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()));
        Post post2 = new Post(userID2, "Test title 2", "Test content 2", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()));
        Post post3 = new Post(userID, "Test title 3", "Test content 3", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()));

        postDao.createPost(post);
        postDao.createPost(post2);
        postDao.createPost(post3);

        List<Post> posts = postDao.findByUserId(userID);

        assertEquals(2, posts.size());
    }

    @Test
    void testFindPostById() throws SQLException {
        Post post = new Post(userID, "Test title", "Test content", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()));
        postDao.createPost(post);

        Post inserted = postDao.findAll().getLast();
        Post found = postDao.findById(inserted.getId());

        assertNotNull(found);
        assertEquals(inserted.getId(), found.getId());
    }

    @Test
    void testUpdatePost() throws SQLException {
        Post post = new Post(userID, "Test title", "Test content", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()));
        postDao.createPost(post);

        Post found = postDao.findAll().getLast();

        found.setTitle("Updated title");
        found.setContent("Updated content");

        postDao.updatePost(found);

        Post updated = postDao.findById(found.getId());

        assertEquals("Updated title", updated.getTitle());
        assertEquals("Updated content", updated.getContent());
        assertEquals(Date.valueOf(LocalDate.now()), updated.getUpdateDate());
    }

    @Test
    void testRemovePost() throws SQLException {
        postDao.createPost(
                new Post(userID, "Test title", "Test content", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()))
        );

        Post post = postDao.findAll().getLast();

        postDao.removePost(post);

        assertNull(postDao.findById(post.getId()));
    }

    @Test
    void testPostBelongsToUser() throws SQLException {
        Post post1 = new Post(
                userID,
                "User post 1",
                "Content 1",
                Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now())
        );

        Post post2 = new Post(
                userID,
                "User post 2",
                "Content 2",
                Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now())
        );

        postDao.createPost(post1);
        postDao.createPost(post2);

        List<Post> userPosts = postDao.findByUserId(userID);

        assertFalse(userPosts.isEmpty());

        for (Post post : userPosts) {
            assertEquals(userID, post.getUserId());
        }
    }

}
