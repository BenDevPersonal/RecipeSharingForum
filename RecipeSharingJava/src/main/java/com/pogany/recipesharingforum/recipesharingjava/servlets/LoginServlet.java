package com.pogany.recipesharingforum.recipesharingjava.servlets;

import com.pogany.recipesharingforum.recipesharingjava.dao.UserDao;
import com.pogany.recipesharingforum.recipesharingjava.dao.UserDaoImpl;
import com.pogany.recipesharingforum.recipesharingjava.entities.User;
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

        Context initCtx = null;
        Connection conn = null;

        try {
            initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/recipeforum_db");
            conn = ds.getConnection();

            UserDao userDao = new UserDaoImpl(conn);

            user = userDao.authUser(username, password);

            if (user == null) {
                //TO DO: Redirect and/or error message
                System.out.println("Hibás belépési adatok.");
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                request.getRequestDispatcher("home").forward(request, response);
            }

            conn.close();
        } catch (SQLException e) {
            throw new IOException(e);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}