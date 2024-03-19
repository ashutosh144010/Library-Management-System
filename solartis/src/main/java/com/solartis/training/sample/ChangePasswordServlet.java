package com.solartis.training.sample;
 
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentIdStr = request.getParameter("your_Id");
        String newPassword = request.getParameter("newPassword");
 
        int studentId = Integer.parseInt(studentIdStr);
        
        // You need to handle the database connection setup. Here, I assume you have a LibraryManagementSystem object.
 
        try {
            // Change the password
         LibraryManagementSystem library = new LibraryManagementSystem("jdbc:mysql://localhost/ashutosh", "root", "password");
            library.changeStudentPassword(studentId, newPassword);
            response.sendRedirect("password_changed.jsp?success=true");
        } catch (SQLException e) {
            // Handle database error
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}