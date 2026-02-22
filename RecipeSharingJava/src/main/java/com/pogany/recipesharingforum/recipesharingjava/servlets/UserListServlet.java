package com.pogany.recipesharingforum.recipesharingjava.servlets;

import com.pogany.recipesharingforum.recipesharingjava.dao.UserDao;
import com.pogany.recipesharingforum.recipesharingjava.dao.UserDaoImpl;
import com.pogany.recipesharingforum.recipesharingjava.entities.User;
import com.pogany.recipesharingforum.recipesharingjava.service.UserService;
import com.pogany.recipesharingforum.recipesharingjava.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/users")
public class UserListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;

        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/recipeforum_db");
            conn = ds.getConnection();
            UserDao userDao = new UserDaoImpl(conn);
            UserService userService = new UserServiceImpl(userDao);

            List<User> users = userService.getAllUsers();

            request.setAttribute("users", users);
            request.getRequestDispatcher("/users.jsp").forward(request, response);

        } catch (NamingException | SQLException e) {
            throw new ServletException(e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
    }