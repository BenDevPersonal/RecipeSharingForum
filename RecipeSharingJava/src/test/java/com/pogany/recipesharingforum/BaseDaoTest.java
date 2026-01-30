package com.pogany.recipesharingforum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseDaoTest {

    protected static final String JDBC_URL =
            "jdbc:mariadb://localhost:3306/recipeforum_testdb";
    protected static final String DB_USER = "root";
    protected static final String DB_PASS = "";

    @BeforeAll
    void createSchema() throws Exception {
        try (Connection conn =
                     DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement()) {

            // Drop tables in FK order
            stmt.execute("DROP TABLE IF EXISTS feedback");
            stmt.execute("DROP TABLE IF EXISTS post");
            stmt.execute("DROP TABLE IF EXISTS user");

            stmt.execute("""
                CREATE TABLE user (
                  id int(11) NOT NULL AUTO_INCREMENT,
                  login varchar(21) NOT NULL,
                  email varchar(256) NOT NULL,
                  password varchar(127) NOT NULL,
                  country varchar(45) NOT NULL,
                  PRIMARY KEY (id),
                  UNIQUE KEY username (login),
                  UNIQUE KEY email (email)
                )
            """);

            stmt.execute("""
                CREATE TABLE post (
                  id int(11) NOT NULL AUTO_INCREMENT,
                  user_id int(11) NOT NULL,
                  title varchar(40) NOT NULL,
                  content text NOT NULL,
                  creation_date date NOT NULL,
                  update_date date NOT NULL,
                  PRIMARY KEY (id),
                  KEY fk_user_id (user_id),
                  CONSTRAINT fk_user_id
                    FOREIGN KEY (user_id) REFERENCES user (id)
                )
            """);

            stmt.execute("""
                CREATE TABLE feedback (
                  id int(11) NOT NULL AUTO_INCREMENT,
                  user_id int(11) NOT NULL,
                  post_id int(11) NOT NULL,
                  rating int(11) NOT NULL,
                  content text NOT NULL,
                  PRIMARY KEY (id),
                  KEY fk_post_id (post_id),
                  KEY fk_user_id2 (user_id),
                  CONSTRAINT fk_post_id
                    FOREIGN KEY (post_id) REFERENCES post (id),
                  CONSTRAINT fk_user_id2
                    FOREIGN KEY (user_id) REFERENCES user (id)
                )
            """);
        }
    }
}
