package com.solartis.training.sample;

import java.io.IOException;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminServlet extends HttpServlet {

    private Connection connection;
//
//    public void init() {
//        try {
//            this.connection = DatabaseConnection.initializeDatabase();
//        } catch (ClassNotFoundException | SQLException e) {
//            throw new UnavailableException("Error initializing the database: " + e.getMessage());
//        }
//    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "addBook":
                addBook(request);
                break;
            // Add other cases for different admin actions
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action not supported");
                break;
        }
    }

    private void addBook(HttpServletRequest request) {
        // Extract book details from request
        int id = Integer.parseInt(request.getParameter("bookId"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        int publishYear = Integer.parseInt(request.getParameter("publishYear"));

        try {
            if (!bookExists(id)) {
                String sql = "INSERT INTO books (id, title, author, publish_year) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    pstmt.setString(2, title);
                    pstmt.setString(3, author);
                    pstmt.setInt(4, publishYear);
                    pstmt.executeUpdate();
                    // Redirect or forward to a success page
                }
            } else {
                // Handle case where book already exists
            }
        } catch (SQLException e) {
            // Handle SQL exception
        }
    }

    private boolean bookExists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM books WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // Handle SQL exception during close
        }
    }
}
