package com.solartis.training.sample;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/SetStudentActiveStatusServlet")
public class SetStudentActiveStatusServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        boolean isActive = "active".equals(request.getParameter("status"));

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/ashutosh", "root", "password")) {
            String sql = "UPDATE students SET isActive = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setBoolean(1, isActive);
                statement.setInt(2, studentId);
                int rowsUpdated = statement.executeUpdate();
                
                response.setContentType("text/html;charset=UTF-8");
                if (rowsUpdated > 0) {
                    response.getWriter().println("<p>Student active status updated successfully.</p>");
                } else {
                    response.getWriter().println("<p>Failed to update student active status.</p>");
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database access error:", e);
        }
        
        response.getWriter().println("<a href='block_student.html'>Back to Set Active Status</a>");
    }
}
