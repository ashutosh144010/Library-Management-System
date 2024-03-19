package com.solartis.training.sample;

import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/AddStudentServlet")
public class AddStudentServlet extends HttpServlet {
    private Connection connection;

    public void init() throws UnavailableException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ashutosh", "root", "password"); // Adjust with your details.
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new UnavailableException("Cannot load database driver or establish connection.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Initialize variables and perform input validation or defaulting
        int id = 0;
        String idStr = request.getParameter("Id");
        String name = request.getParameter("Name");
        
        // Validate and parse integer parameters
        try {
            id = idStr != null && !idStr.isEmpty() ? Integer.parseInt(idStr) : 0;
            
            // Proceed if id and publishYear are valid, i.e., non-zero
            if (id > 0) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO students (id, name) VALUES (?, ?)")) {
                    statement.setInt(1, id);
                    statement.setString(2, name);
                    
                    int result = statement.executeUpdate();
                    
                    if (result > 0) {
                        response.getWriter().println("student added successfully!");
                    } else {
                        response.getWriter().println("Failed to add book.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.getWriter().println("Database error: " + e.getMessage());
                }
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO student_passwords (id) VALUES (?)")) {
                    statement.setInt(1, id);
                    
                    int result = statement.executeUpdate();
                    
//                    if (result > 0) {
//                        response.getWriter().println("student added successfully!");
//                    } else {
//                        response.getWriter().println("Failed to add book.");
//                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.getWriter().println("Database error: " + e.getMessage());
                }
            } else {
                response.getWriter().println("Invalid ID");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.getWriter().println("ID  must be valid numbers.");
        }
    }

    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
