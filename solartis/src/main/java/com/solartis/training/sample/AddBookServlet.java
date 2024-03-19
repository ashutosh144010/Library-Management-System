package com.solartis.training.sample;

import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/AddBookServlet")
public class AddBookServlet extends HttpServlet {
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
        int publishYear = 0;
        String idStr = request.getParameter("Id");
        String publishYearStr = request.getParameter("publishYear");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        
        // Validate and parse integer parameters
        try {
            id = idStr != null && !idStr.isEmpty() ? Integer.parseInt(idStr) : 0;
            publishYear = publishYearStr != null && !publishYearStr.isEmpty() ? Integer.parseInt(publishYearStr) : 0;
            
            // Proceed if id and publishYear are valid, i.e., non-zero
            if (id > 0 && publishYear > 0) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO books (id, title, author, publish_year) VALUES (?, ?, ?, ?)")) {
                    statement.setInt(1, id);
                    statement.setString(2, title);
                    statement.setString(3, author);
                    statement.setInt(4, publishYear);
                    
                    int result = statement.executeUpdate();
                    
                    if (result > 0) {
                        response.getWriter().println("Book added successfully!");
                    } else {
                        response.getWriter().println("Failed to add book.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.getWriter().println("Database error: " + e.getMessage());
                }
            } else {
                response.getWriter().println("Invalid ID or Publish Year.");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.getWriter().println("ID or Publish Year must be valid numbers.");
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
