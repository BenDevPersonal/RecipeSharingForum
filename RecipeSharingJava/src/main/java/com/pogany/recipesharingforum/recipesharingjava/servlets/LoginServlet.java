package com.pogany.recipesharingforum.recipesharingjava.servlets;

import com.pogany.recipesharingforum.recipesharingjava.dao.*;
import com.pogany.recipesharingforum.recipesharingjava.entities.Post;
import com.pogany.recipesharingforum.recipesharingjava.entities.Role;
import com.pogany.recipesharingforum.recipesharingjava.entities.User;
import com.pogany.recipesharingforum.recipesharingjava.service.PostService;
import com.pogany.recipesharingforum.recipesharingjava.service.RoleService;
import com.pogany.recipesharingforum.recipesharingjava.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("user");
        String password = request.getParameter("password");
        User user;
        Role role;

        Context initCtx = null;
        Connection conn = null;

        try {
            initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/recipeforum_db");
            conn = ds.getConnection();

            UserDao userDao = new UserDaoImpl(conn);
            RoleDao roleDao = new RoleDaoImpl(conn);
            PostDao postDao = new PostDaoImpl(conn);

            UserService userService = new UserService(userDao);
            PostService postService = new PostService(postDao);
            RoleService roleService = new RoleService(roleDao);

            user = userService.login(username, password);

            if (user == null) {
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                role = roleService.getRole(user.getRoleId());

                List<Post> posts = postService.getAllPosts();

                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("role", role);
                session.setAttribute("posts", posts);

                request.getRequestDispatcher("home.jsp").forward(request, response);
            }

            conn.close();
        } catch (SQLException e) {
            request.setAttribute("errorTitle", "SQLException");
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (NamingException e) {
            request.setAttribute("errorTitle", "NamingException");
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}