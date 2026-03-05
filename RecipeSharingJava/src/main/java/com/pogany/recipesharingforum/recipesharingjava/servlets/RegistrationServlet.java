package com.pogany.recipesharingforum.recipesharingjava.servlets;

import com.pogany.recipesharingforum.recipesharingjava.dao.RoleDao;
import com.pogany.recipesharingforum.recipesharingjava.dao.RoleDaoImpl;
import com.pogany.recipesharingforum.recipesharingjava.dao.UserDao;
import com.pogany.recipesharingforum.recipesharingjava.dao.UserDaoImpl;
import com.pogany.recipesharingforum.recipesharingjava.entities.Role;
import com.pogany.recipesharingforum.recipesharingjava.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String login = request.getParameter("login");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String country = request.getParameter("country");

        String error = validate(login, email, password, country);

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("registration.jsp")
                    .forward(request, response);
            return;
        }

        try {
            UserDao userDao = new UserDaoImpl();
            RoleDao roleDao = new RoleDaoImpl();

            // Check if login/email already exists
            if (userDao.findByLogin(login) != null) {
                request.setAttribute("error", "Login already exists");
                request.getRequestDispatcher("registration.jsp").forward(request, response);
                return;
            }

            if (userDao.findByEmail(email) != null) {
                request.setAttribute("error", "Email already exists");
                request.getRequestDispatcher("registration.jsp").forward(request, response);
                return;
            }

            // Set default role (ID 2 = regular user)
            Role defaultRole = roleDao.findById(2);

            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            user.setEmail(email);
            user.setCountry(country);
            user.setRole(defaultRole);

            userDao.createUser(user);

            response.sendRedirect("login.jsp");

        } catch (Exception e) {
            request.setAttribute("errorTitle", "Exception");
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private String validate(String login, String email,
                            String password, String country) {

        if (login == null || login.isBlank())
            return "Login is required";

        if (email == null || email.isBlank())
            return "Email is required";

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            return "Invalid email format";

        if (password == null || password.length() < 4)
            return "Password must be at least 4 characters";

        if (country == null || country.isBlank())
            return "Country is required";

        return null;
    }
}