package com.pogany.recipesharingforum.dao;

import com.pogany.recipesharingforum.entities.Comment;
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

class CommentDaoImplTest extends BaseDaoTest {

    private Connection conn;
    private UserDaoImpl userDao;
    private PostDaoImpl postDao;
    private CommentDaoImpl commentDao;
    private int userID;
    private int postID;

    @BeforeEach
    void setup() throws Exception {
        conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
        conn.setAutoCommit(false);
        userDao = new UserDaoImpl(conn);
        postDao = new PostDaoImpl(conn);
        commentDao = new CommentDaoImpl(conn);

        userDao.createUser(new User("tu", "pw", "tu@test.com", "US"));
        userID = userDao.findByLogin("tu").getId();

        postDao.createPost(new Post(userID, "Test post", "Test post content", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now())));
        postID = postDao.findAll().getLast().getId();
    }

    @AfterEach
    void rollback() throws Exception {
        conn.rollback();
        conn.close();
    }

    @Test
    void testCreateComment() throws SQLException {
        Comment comment = new Comment(userID, postID, 5, "Tasty");

        commentDao.createComment(comment);
        Comment found = commentDao.findByUserId(userID).getLast();

        assertNotNull(found);
        assertEquals("Tasty", found.getContent());
    }

    @Test
    void testUpdateComment() throws SQLException {
        Comment comment = new Comment(userID, postID, 5, "Tasty");
        commentDao.createComment(comment);

        Comment found = commentDao.findByUserId(userID).getLast();

        found.setRating(3);
        found.setContent("It's okay");

        commentDao.updateComment(found);

        Comment updated = commentDao.findById(found.getId());

        assertEquals(3, updated.getRating());
        assertEquals("It's okay", updated.getContent());
    }

    @Test
    void testRemoveComment() throws SQLException {
        commentDao.createComment(
                new Comment(userID, postID, 5, "Tasty")
        );
        Comment comment = commentDao.findByUserId(userID).getLast();

        commentDao.removeComment(comment);

        assertNull(commentDao.findById(comment.getId()));
    }

    @Test
    void testFindById() throws SQLException {
        Comment comment = new Comment(userID, postID, 5, "Tasty");

        commentDao.createComment(comment);
        Comment inserted = commentDao.findByUserId(userID).getLast();
        Comment found = commentDao.findById(inserted.getId());

        assertNotNull(found);
        assertEquals(inserted.getId(), found.getId());
    }

    @Test
    void testFindByPostId() throws SQLException {
        postDao.createPost(new Post(userID, "Test post 2", "Test post content 2", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now())));
        int postID2 = postDao.findAll().getLast().getId();

        Comment comment = new Comment(userID, postID, 5, "Tasty");
        Comment comment2 = new Comment(userID, postID2, 2, "Meh");
        Comment comment3 = new Comment(userID, postID, 3, "It's okay");

        commentDao.createComment(comment);
        commentDao.createComment(comment2);
        commentDao.createComment(comment3);

        List<Comment> comments = commentDao.findByPostId(postID);

        assertEquals(2, comments.size());
    }

    @Test
    void testFindByUserId() throws SQLException {
        userDao.createUser(new User("tu2", "pw", "tu2@test.com", "US"));
        int userID2 = userDao.findByLogin("tu2").getId();

        Comment comment = new Comment(userID2, postID, 5, "Tasty");
        Comment comment2 = new Comment(userID, postID, 2, "Meh");
        Comment comment3 = new Comment(userID, postID, 3, "It's okay");

        commentDao.createComment(comment);
        commentDao.createComment(comment2);
        commentDao.createComment(comment3);

        List<Comment> comments = commentDao.findByUserId(userID);

        assertEquals(2, comments.size());
    }

    @Test
    void testCommentBelongsToUser() throws SQLException {
        Comment comment = new Comment(userID, postID, 5, "Tasty");
        Comment comment2 = new Comment(userID, postID, 2, "Meh");

        commentDao.createComment(comment);
        commentDao.createComment(comment2);

        List<Comment> userComments = commentDao.findByUserId(userID);

        assertFalse(userComments.isEmpty());
        for (Comment userComment : userComments) {
            assertEquals(userID, userComment.getUserId());
        }
    }

    @Test
    void testCommentBelongsToPost() throws SQLException {
        Comment comment = new Comment(userID, postID, 5, "Tasty");
        Comment comment2 = new Comment(userID, postID, 2, "Meh");

        commentDao.createComment(comment);
        commentDao.createComment(comment2);

        List<Comment> postComments = commentDao.findByPostId(postID);

        assertFalse(postComments.isEmpty());
        for (Comment postComment : postComments) {
            assertEquals(userID, postComment.getUserId());
        }
    }
}