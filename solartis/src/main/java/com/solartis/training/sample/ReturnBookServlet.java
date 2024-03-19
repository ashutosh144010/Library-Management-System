package com.solartis.training.sample;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/ReturnBookServlet")
public class ReturnBookServlet extends HttpServlet {
    private Connection getConnection() throws SQLException {
        String dbUrl = "jdbc:mysql://localhost:3306/ashutosh";
        String user = "root";
        String password = "password";
        return DriverManager.getConnection(dbUrl, user, password);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));

        try (Connection connection = getConnection()) {
        	String dbUrl = "jdbc:mysql://localhost:3306/ashutosh";
            String user = "root";
            String password = "password";
           LibraryManagementSystem system = new LibraryManagementSystem(dbUrl,user,password);
            if (system.returnBook(studentId, bookId)) {
                response.getWriter().println("Book returned successfully.");
            } else {
                response.getWriter().println("Failed to return the book.");
            }
        } catch (SQLException e) {
            response.getWriter().println("Database error: " + e.getMessage());
        }
    }
}

