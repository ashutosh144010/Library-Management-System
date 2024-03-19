package com.solartis.training.sample;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/BookRenewServlet")
public class BookRenewServlet extends HttpServlet {
    private LibraryManagementSystem library; // Declare LibraryManagementSystem object

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            String dbUrl = "jdbc:mysql://localhost:3306/ashutosh";
            String user = "root";
            String password = "password";
            library = new LibraryManagementSystem(dbUrl, user, password); // Initialize LibraryManagementSystem object
        } catch (SQLException e) {
            throw new ServletException("Database connection problem", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));

        if (library.renewBook(studentId, bookId)) { // Use the existing LibraryManagementSystem object
		    response.getWriter().println("Book renewed successfully.");
		} else {
		    response.getWriter().println("Failed to renew the book.");
		}
    }

    
}

