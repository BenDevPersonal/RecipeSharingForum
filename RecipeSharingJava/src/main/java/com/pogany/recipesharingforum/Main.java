package com.pogany.recipesharingforum;

import com.pogany.recipesharingforum.dao.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Properties prop = new Properties();

        try {
            prop.load(new FileReader("src/db_conn.property"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final String CONN_URL = prop.get("url").toString();
        final String DB_USER = prop.get("user").toString();
        final String DB_PASS = prop.get("pass").toString();

        try (Connection conn = DriverManager.getConnection(CONN_URL, DB_USER, DB_PASS)) {
            conn.setAutoCommit(false);

            UserDao userDao = new UserDaoImpl(conn);
            PostDao postDao = new PostDaoImpl(conn);
            CommentDao commentDao = new CommentDaoImpl(conn);

            Scanner sc = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("\n=== Recipe Sharing Forum ===");
                System.out.println("1 - User DAO demo");
                System.out.println("2 - Post DAO demo");
                System.out.println("3 - Comment DAO demo");
                System.out.println("0 - Exit");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> userMenu(sc, userDao);
                    case 2 -> postMenu(sc, postDao);
                    case 3 -> commentMenu(sc, commentDao);
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option");
                }
            }
            conn.commit();

            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //above is the selector, below are the soon to be console demos
    private static void userMenu(Scanner sc, UserDao userDao) throws SQLException {}
    private static void postMenu(Scanner sc, PostDao postDao) throws SQLException {}
    private static void commentMenu(Scanner sc, CommentDao commentDao) throws SQLException {}

}
