package com.solartis.training.sample;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/ModifyStudentServlet")
public class ModifyStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        String newName = request.getParameter("newName");

        try {
            LibraryManagementSystem system = new LibraryManagementSystem("jdbc:mysql://localhost/ashutosh", "root", "password");
            boolean isSuccess = system.modifyStudent(studentId, newName);
            
            response.setContentType("text/html;charset=UTF-8");
            if (isSuccess) {
                response.getWriter().println("<p>Student details modified successfully.</p>");
            } else {
                response.getWriter().println("<p>Error modifying student details.</p>");
            }
            response.getWriter().println("<a href='modify_student.html'>Back to Student Operations</a>");
        } catch (SQLException e) {
            throw new ServletException("Database access error:", e);
        }
    }
}
