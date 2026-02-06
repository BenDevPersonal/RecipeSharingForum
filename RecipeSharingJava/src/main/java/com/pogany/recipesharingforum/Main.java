package com.pogany.recipesharingforum;

import com.pogany.recipesharingforum.dao.*;
import com.pogany.recipesharingforum.entities.*;

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

    private static void userMenu(Scanner sc, UserDao userDao) throws SQLException {
        System.out.println("""
                --- USER MENU ---
                1 - Create user
                2 - Update user
                3 - Remove user
                4 - Find all
                5 - Find by ID
                6 - Find by login
                7 - Find by email
                """);

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Login: ");
                String login = sc.nextLine();
                System.out.print("Password: ");
                String pass = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();
                System.out.print("Country: ");
                String country = sc.nextLine();

                userDao.createUser(new User(login, pass, email, country));
            }
            case 2 -> {
                System.out.print("Login: ");
                String login = sc.nextLine();
                System.out.print("Password: ");
                String pass = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();
                System.out.print("Country: ");
                String country = sc.nextLine();

                userDao.updateUser(new User(login, pass, email, country));
            }
            case 3 -> {
                System.out.print("ID: ");
                int id = sc.nextInt();
                userDao.removeUser(userDao.findById(id));
            }
            case 4 -> {
                List<User> users = userDao.findAll();
                users.forEach(u ->
                        System.out.println(u.getId() + " " + u.getLogin() + " " + u.getEmail()));
            }
            case 5 -> {
                System.out.print("ID: ");
                System.out.println(userDao.findById(sc.nextInt()));
            }
            case 6 -> {
                System.out.print("Login: ");
                System.out.println(userDao.findByLogin(sc.nextLine()));
            }
            case 7 -> {
                System.out.print("Email: ");
                System.out.println(userDao.findByEmail(sc.nextLine()));
            }
        }
    }
    private static void postMenu(Scanner sc, PostDao postDao) throws SQLException {}
    private static void commentMenu(Scanner sc, CommentDao commentDao) throws SQLException {}

}
