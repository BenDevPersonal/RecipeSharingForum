package com.pogany.recipesharingforum.recipesharingjava;

import com.pogany.recipesharingforum.recipesharingjava.dao.*;
import com.pogany.recipesharingforum.recipesharingjava.entities.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
            RoleDao roleDao = new RoleDaoImpl(conn);

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
                System.out.print("Role ID: ");
                int roleId = sc.nextInt();

                userDao.createUser(new User(login, pass, email, country, roleId));
            }
            case 2 -> {
                System.out.print("User ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Login: ");
                String login = sc.nextLine();
                System.out.print("Password: ");
                String pass = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();
                System.out.print("Country: ");
                String country = sc.nextLine();
                System.out.print("Role ID: ");
                int roleId = sc.nextInt();

                User user = userDao.findById(id);

                user.setLogin(login);
                user.setPassword(pass);
                user.setEmail(email);
                user.setCountry(country);
                user.setRoleId(roleId);

                userDao.updateUser(user);
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

    private static void postMenu(Scanner sc, PostDao postDao) throws SQLException {
        System.out.println("""
                --- POST MENU ---
                1 - Create post
                2 - Update post
                3 - Remove post
                4 - Find all
                5 - Find by user ID
                6 - Find by ID
                """);

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("User ID: ");
                int userId = sc.nextInt();
                sc.nextLine();
                System.out.print("Title: ");
                String title = sc.nextLine();
                System.out.print("Content: ");
                String content = sc.nextLine();

                postDao.createPost(new Post(userId, title, content, null, null));
            }
            case 2 -> {
                System.out.print("Post ID: ");
                int id = sc.nextInt();
                sc.nextLine();
                System.out.print("Title: ");
                String title = sc.nextLine();
                System.out.print("Content: ");
                String content = sc.nextLine();

                Post post = postDao.findById(id);

                post.setTitle(title);
                post.setContent(content);

                postDao.updatePost(post);
            }
            case 3 -> {
                System.out.print("Post ID: ");
                int id = sc.nextInt();
                postDao.removePost(postDao.findById(id));
            }
            case 4 -> postDao.findAll().forEach(p ->
                    System.out.println(p.getId() + " " + p.getTitle() + "\n" + p.getContent()));
            case 5 -> {
                System.out.print("User ID: ");
                postDao.findByUserId(sc.nextInt())
                        .forEach(p -> System.out.println(p.getTitle() + "\n" + p.getContent()));
            }
            case 6 -> {
                System.out.print("Post ID: ");

                System.out.println(postDao.findById(sc.nextInt()));
            }
        }
    }

    private static void commentMenu(Scanner sc, CommentDao commentDao) throws SQLException {
        System.out.println("""
                --- COMMENT MENU ---
                1 - Create comment
                2 - Update comment
                3 - Remove comment
                4 - Find by ID
                5 - Find by user ID
                6 - Find by post ID
                """);

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("User ID: ");
                int userId = sc.nextInt();
                System.out.print("Post ID: ");
                int postId = sc.nextInt();
                System.out.print("Rating (1-5): ");
                int rating = sc.nextInt();
                sc.nextLine();
                System.out.print("Content: ");
                String content = sc.nextLine();

                commentDao.createComment(new Comment(userId, postId, rating, content));
            }
            case 2 -> {
                System.out.print("Comment ID: ");
                int id = sc.nextInt();

                System.out.print("Rating (1-5): ");
                int rating = sc.nextInt();
                sc.nextLine();
                System.out.print("Content: ");
                String content = sc.nextLine();

                Comment comment = commentDao.findById(id);

                comment.setRating(rating);
                comment.setContent(content);

                commentDao.updateComment(comment);
            }
            case 3 -> {
                System.out.print("Comment ID: ");
                int id = sc.nextInt();
                commentDao.removeComment(commentDao.findById(id));
            }
            case 4 -> {
                System.out.print("Comment ID: ");
                System.out.println(commentDao.findById(sc.nextInt()));
            }
            case 5 -> {
                System.out.print("User ID: ");
                commentDao.findByUserId(sc.nextInt())
                        .forEach(c -> System.out.println(c.getContent()));
            }
            case 6 -> {
                System.out.print("Post ID: ");
                commentDao.findByPostId(sc.nextInt())
                        .forEach(c -> System.out.println(c.getContent()));
            }
        }
    }
}
