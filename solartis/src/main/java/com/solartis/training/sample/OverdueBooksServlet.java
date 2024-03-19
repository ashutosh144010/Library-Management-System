package com.solartis.training.sample;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/OverdueBooksServlet")
public class OverdueBooksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        try {
            LibraryManagementSystem system = new LibraryManagementSystem("jdbc:mysql://localhost/ashutosh", "root", "password");
            List<String> overdueBooks = system.findOverdueBooks();

            response.getWriter().println("<html><head><title>Overdue Books</title></head><body>");
            response.getWriter().println("<h2>Overdue Books</h2>");
            response.getWriter().println("<ul>");
            for (String entry : overdueBooks) {
                response.getWriter().println("<li>" + entry + "</li>");
            }
            response.getWriter().println("</ul>");
            response.getWriter().println("</body></html>");
        } catch (SQLException e) {
            throw new ServletException("SQL Error: " + e.getMessage(), e);
        }
    }
}
