package com.pogany.recipesharingforum.recipesharingjava;

import com.pogany.recipesharingforum.recipesharingjava.dao.*;
import com.pogany.recipesharingforum.recipesharingjava.entities.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        UserDao userDao = new UserDaoImpl();
        PostDao postDao = new PostDaoImpl();
        FeedbackDao feedbackDao = new FeedbackDaoImpl();
        RoleDao roleDao = new RoleDaoImpl();

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
                case 1 -> userMenu(sc, userDao, roleDao);
                case 2 -> postMenu(sc, postDao, userDao);
                case 3 -> commentMenu(sc, feedbackDao, userDao, postDao);
                case 0 -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void userMenu(Scanner sc, UserDao userDao, RoleDao roleDao) throws SQLException {
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

                Role role = roleDao.findById(roleId);
                User user = new User();
                user.setLogin(login);
                user.setPassword(pass);
                user.setEmail(email);
                user.setCountry(country);
                user.setRole(role);

                userDao.createUser(user);
            }
            case 2 -> {
                System.out.print("User ID: ");
                int id = sc.nextInt();
                sc.nextLine();
                User user = userDao.findById(id);

                System.out.print("Login: ");
                user.setLogin(sc.nextLine());
                System.out.print("Password: ");
                user.setPassword(sc.nextLine());
                System.out.print("Email: ");
                user.setEmail(sc.nextLine());
                System.out.print("Country: ");
                user.setCountry(sc.nextLine());
                System.out.print("Role ID: ");
                int roleId = sc.nextInt();
                Role role = roleDao.findById(roleId);
                user.setRole(role);

                userDao.updateUser(user);
            }
            case 3 -> {
                System.out.print("ID: ");
                int id = sc.nextInt();
                userDao.removeUser(userDao.findById(id));
            }
            case 4 -> userDao.findAll()
                    .forEach(u -> System.out.println(u.getId() + " " + u.getLogin() + " " + u.getEmail()));
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

    private static void postMenu(Scanner sc, PostDao postDao, UserDao userDao) throws SQLException {
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
                User user = userDao.findById(userId);

                System.out.print("Title: ");
                String title = sc.nextLine();
                System.out.print("Content: ");
                String content = sc.nextLine();

                Post post = new Post();
                post.setUser(user);
                post.setTitle(title);
                post.setContent(content);

                postDao.createPost(post);
            }
            case 2 -> {
                System.out.print("Post ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                Post post = postDao.findById(id);

                System.out.print("Title: ");
                post.setTitle(sc.nextLine());
                System.out.print("Content: ");
                post.setContent(sc.nextLine());

                postDao.updatePost(post);
            }
            case 3 -> {
                System.out.print("Post ID: ");
                int id = sc.nextInt();
                postDao.removePost(postDao.findById(id));
            }
            case 4 -> postDao.findAll()
                    .forEach(p -> System.out.println(p.getId() + " " + p.getTitle() + "\n" + p.getContent()));
            case 5 -> {
                System.out.print("User ID: ");
                int userId = sc.nextInt();
                postDao.findByUserId(userId)
                        .forEach(p -> System.out.println(p.getTitle() + "\n" + p.getContent()));
            }
            case 6 -> {
                System.out.print("Post ID: ");
                System.out.println(postDao.findById(sc.nextInt()));
            }
        }
    }

    private static void commentMenu(Scanner sc, FeedbackDao feedbackDao, UserDao userDao, PostDao postDao) throws SQLException {
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

                User user = userDao.findById(userId);
                Post post = postDao.findById(postId);

                Feedback feedback = new Feedback();
                feedback.setUser(user);
                feedback.setPost(post);
                feedback.setRating(rating);
                feedback.setContent(content);

                feedbackDao.createComment(feedback);
            }
            case 2 -> {
                System.out.print("Comment ID: ");
                int id = sc.nextInt();
                Feedback comment = feedbackDao.findById(id);

                System.out.print("Rating (1-5): ");
                comment.setRating(sc.nextInt());
                sc.nextLine();
                System.out.print("Content: ");
                comment.setContent(sc.nextLine());

                feedbackDao.updateComment(comment);
            }
            case 3 -> {
                System.out.print("Comment ID: ");
                int id = sc.nextInt();
                feedbackDao.removeComment(feedbackDao.findById(id));
            }
            case 4 -> {
                System.out.print("Comment ID: ");
                System.out.println(feedbackDao.findById(sc.nextInt()));
            }
            case 5 -> {
                System.out.print("User ID: ");
                int userId = sc.nextInt();
                feedbackDao.findByUserId(userId)
                        .forEach(c -> System.out.println(c.getContent()));
            }
            case 6 -> {
                System.out.print("Post ID: ");
                int postId = sc.nextInt();
                feedbackDao.findByPostId(postId)
                        .forEach(c -> System.out.println(c.getContent()));
            }
        }
    }
}