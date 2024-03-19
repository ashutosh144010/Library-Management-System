package com.solartis.training.sample;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/AvailableBooksServlet")
public class AvailableBooksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            LibraryManagementSystem system = new LibraryManagementSystem("jdbc:mysql://localhost/ashutosh", "root", "password");
            List<String> availableBooks = system.findAvailableBooks();

            response.getWriter().println("<!DOCTYPE html>");
            response.getWriter().println("<html><head><title>Available Books</title></head><body>");
            response.getWriter().println("<h2>Available Books</h2>");
            response.getWriter().println("<ul>");
            for (String book : availableBooks) {
                response.getWriter().println("<li>" + book + "</li>");
            }
            response.getWriter().println("</ul>");
            response.getWriter().println("</body></html>");
        } catch (SQLException e) {
            throw new ServletException("Database access error:", e);
        }
    }
}
