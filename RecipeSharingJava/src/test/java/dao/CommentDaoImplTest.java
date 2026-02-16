package dao;

import com.pogany.recipesharingforum.recipesharingjava.entities.Comment;
import com.pogany.recipesharingforum.recipesharingjava.entities.Post;
import com.pogany.recipesharingforum.recipesharingjava.entities.User;
import com.pogany.recipesharingforum.recipesharingjava.dao.CommentDaoImpl;
import com.pogany.recipesharingforum.recipesharingjava.dao.UserDaoImpl;
import com.pogany.recipesharingforum.recipesharingjava.dao.PostDaoImpl;
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

    @Test
    void testCreateCommentWithInvalidUserId() {
        Comment comment = new Comment(-1, postID, 5, "Invalid user");

        assertThrows(SQLException.class, () -> commentDao.createComment(comment));
    }

    @Test
    void testCreateCommentWithInvalidPostId() {
        Comment comment = new Comment(userID, -1, 5, "Invalid post");

        assertThrows(SQLException.class, () -> commentDao.createComment(comment));
    }

    @Test
    void testCreateCommentWithNullContent() {
        Comment comment = new Comment(userID, postID, 5, null);

        assertThrows(SQLException.class, () -> commentDao.createComment(comment));
    }

    @Test
    void testFindCommentByInvalidId() throws SQLException {
        Comment found = commentDao.findById(-1);
        assertNull(found);
    }

    @Test
    void testFindCommentByNonExistingId() throws SQLException {
        Comment found = commentDao.findById(99999);
        assertNull(found);
    }

    @Test
    void testFindCommentsByPostIdEmpty() throws SQLException {
        postDao.createPost(
                new Post(userID, "Empty post", "No comments",
                        Date.valueOf(LocalDate.now()),
                        Date.valueOf(LocalDate.now()))
        );
        int emptyPostId = postDao.findAll().getLast().getId();

        List<Comment> comments = commentDao.findByPostId(emptyPostId);

        assertTrue(comments.isEmpty());
    }

    @Test
    void testFindCommentsByUserIdEmpty() throws SQLException {
        userDao.createUser(new User("nocomments", "pw", "nc@test.com", "US"));
        int noCommentUserId = userDao.findByLogin("nocomments").getId();

        List<Comment> comments = commentDao.findByUserId(noCommentUserId);

        assertTrue(comments.isEmpty());
    }

    @Test
    void testUpdateNonExistingComment() throws SQLException {
        Comment ghost = new Comment(userID, postID, 1, "Ghost");
        ghost.setId(99999);

        commentDao.updateComment(ghost);

        assertNull(commentDao.findById(ghost.getId()));
    }

    @Test
    void testRemoveNonExistingComment() throws SQLException {
        Comment ghost = new Comment(userID, postID, 1, "Ghost");
        ghost.setId(99999);

        commentDao.removeComment(ghost);

        assertNull(commentDao.findById(ghost.getId()));
    }
}