package com.pogany.recipesharingforum.dao;

import com.pogany.recipesharingforum.entities.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoImplTest extends BaseDaoTest {
    private Connection conn;
    private UserDaoImpl userDao;

    @BeforeEach
    void setup() throws Exception {
        conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
        conn.setAutoCommit(false);
        userDao = new UserDaoImpl(conn);
    }

    @AfterEach
    void rollback() throws Exception {
        conn.rollback();
        conn.close();
    }

    @Test
    void testCreateUser() throws SQLException {
        User user = new User(0, "alice", "pw", "alice@test.com", "US");

        userDao.createUser(user);
        User found = userDao.findByLogin("alice");

        assertNotNull(found);
        assertEquals("alice", found.getLogin());
    }

    @Test
    void testFindAllUsers() throws SQLException {
        userDao.createUser(new User(0, "u1", "pw1", "u1@test.com", "US"));
        userDao.createUser(new User(0, "u2", "pw2", "u2@test.com", "UK"));

        List<User> users = userDao.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void testFindUserById() throws SQLException {
        userDao.createUser(
                new User(0, "byid", "pw", "byid@test.com", "CA")
        );

        User inserted = userDao.findByLogin("byid");
        User found = userDao.findById(inserted.getId());

        assertNotNull(found);
        assertEquals(inserted.getId(), found.getId());
    }

    @Test
    void testFindUserByEmail() throws SQLException {
        userDao.createUser(
                new User(0, "emailuser", "pw", "email@test.com", "DE")
        );

        User found = userDao.findByEmail("email@test.com");

        assertNotNull(found);
        assertEquals("emailuser", found.getLogin());
    }

    @Test
    void testFindUserByLogin() throws SQLException {
        userDao.createUser(
                new User(0, "loginuser", "pw", "login@test.com", "FR")
        );

        User found = userDao.findByLogin("loginuser");

        assertNotNull(found);
        assertEquals("FR", found.getCountry());
    }

    @Test
    void testUpdateUser() throws SQLException {
        userDao.createUser(
                new User(0, "update_me", "pw", "upd@test.com", "US")
        );

        User user = userDao.findByLogin("update_me");
        user.setCountry("Canada");
        user.setPassword("new_pw");

        userDao.updateUser(user);

        User updated = userDao.findById(user.getId());

        assertEquals("Canada", updated.getCountry());
        assertEquals("new_pw", updated.getPassword());
    }

    @Test
    void testDeleteUser() throws SQLException {
        userDao.createUser(
                new User(0, "delete_me", "pw", "del@test.com", "US")
        );

        User user = userDao.findByLogin("delete_me");
        userDao.removeUser(user);

        assertNull(userDao.findByLogin("delete_me"));
    }

    @Test
    void testDuplicateLoginViolation() throws SQLException {
        userDao.createUser(
                new User(0, "dup", "pw", "dup1@test.com", "US")
        );

        assertThrows(SQLException.class, () ->
                userDao.createUser(
                        new User(0, "dup", "pw", "dup2@test.com", "US")
                )
        );
    }

    @Test
    void testDuplicateEmailViolation() throws SQLException {
        userDao.createUser(
                new User(0, "email1", "pw", "same@test.com", "US")
        );

        assertThrows(SQLException.class, () ->
                userDao.createUser(
                        new User(0, "email2", "pw", "same@test.com", "US")
                )
        );
    }

    @Test
    void testFindUserByInvalidId() throws SQLException {
        User found = userDao.findById(-1);
        assertNull(found);
    }

    @Test
    void testFindUserByNonExistingId() throws SQLException {
        User found = userDao.findById(99999);
        assertNull(found);
    }

    @Test
    void testCreateUserWithNullLogin() {
        User user = new User(0, null, "pw", "null@test.com", "US");

        assertThrows(SQLException.class, () -> userDao.createUser(user));
    }

    @Test
    void testCreateUserWithNullEmail() {
        User user = new User(0, "nullmail", "pw", null, "US");

        assertThrows(SQLException.class, () -> userDao.createUser(user));
    }

    @Test
    void testUpdateNonExistingUser() throws SQLException {
        User ghost = new User(99999, "ghost", "pw", "ghost@test.com", "US");

        userDao.updateUser(ghost);

        assertNull(userDao.findById(ghost.getId()));
    }

    @Test
    void testDeleteNonExistingUser() throws SQLException {
        User ghost = new User(99999, "ghost", "pw", "ghost@test.com", "US");

        userDao.removeUser(ghost);

        assertNull(userDao.findById(ghost.getId()));
    }

    @Test
    void testFindAllUsersEmpty() throws SQLException {
        List<User> users = userDao.findAll();
        assertTrue(users.isEmpty());
    }
}
