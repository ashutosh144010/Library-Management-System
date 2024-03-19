package com.solartis.training.sample;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/CheckBookStatusServlet")
public class CheckBookStatusServlet extends HttpServlet {
    private Connection getConnection() throws SQLException {
        String dbUrl = "jdbc:mysql://localhost:3306/ashutosh";
        String user = "root";
        String password = "password";
        return DriverManager.getConnection(dbUrl, user, password);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        String statusMessage;

        try (Connection connection = getConnection()) {
            String sql = "SELECT is_borrowed FROM books WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, bookId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    boolean isBorrowed = rs.getBoolean("is_borrowed");
                    statusMessage = "The book with ID " + bookId + " is currently " + (isBorrowed ? "borrowed." : "available.");
                } else {
                    statusMessage = "No book found with ID " + bookId + ".";
                }
            }
        } catch (SQLException e) {
            statusMessage = "Database error: " + e.getMessage();
        }

        response.setContentType("text/html");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h2>Book Status</h2>");
        response.getWriter().println("<p>" + statusMessage + "</p>");
        response.getWriter().println("</body></html>");
    }
}

