package com.solartis.training.sample;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/RemoveStudentServlet")
public class RemoveStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));

        try {
            LibraryManagementSystem system = new LibraryManagementSystem("jdbc:mysql://localhost/ashutosh", "root", "password");
            boolean isSuccess = system.removeStudent(studentId);
            
            response.setContentType("text/html;charset=UTF-8");
            if (isSuccess) {
                response.getWriter().println("<p>Student removed successfully.</p>");
            } else {
                response.getWriter().println("<p>Error removing student.</p>");
            }
        } catch (SQLException e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            
            // Send an error response to the client
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database access error: " + e.getMessage());
        }
    }
}
