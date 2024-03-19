package com.solartis.training.sample;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
//import javax.servlet.*;
//import javax.servlet.http.*;
import java.sql.*;
@WebServlet("/LibraryManagementServlet")
public class LibraryManagementServlet extends HttpServlet {
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//        // Here you should validate the username and password (hash the password in a real app)
//        if ("admin".equals(username) && "adminPass".equals(password)) {
//            // Redirect to admin page
//            response.sendRedirect("admin.html");
//        } else if ("librarian".equals(username) && "librarianPass".equals(password)) {
//            // Redirect to librarian page
//            response.sendRedirect("librarian.html");
//        }else if ("student".equals(username) && "studentPass".equals(password)) {
//            // Redirect to librarian page
//            response.sendRedirect("student.html");
//        } else {
//            // Redirect back to index.html with error message
//            response.sendRedirect("index.html?error=invalidCredentials");
//        }
//    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
 
        String url = "jdbc:mysql://localhost:3306/ashutosh";
        String dbName = "ashutosh";
        String driver = "com.mysql.cj.jdbc.Driver";
        String userName = "root";
        String password = "password";
 
        String username = request.getParameter("username");
        String pass = request.getParameter("password");
 
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, userName, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM student_passwords WHERE id='" + username + "' AND password='" + pass + "'");
 
            if (rs.next()) {
                String role = rs.getString("role");
                if (role.equals("student")) {
                    request.getRequestDispatcher("student.html").forward(request, response);
                } else if (role.equals("librarian")) {
                    request.getRequestDispatcher("librarian.html").forward(request, response);
                } else if (role.equals("admin")) {
                    request.getRequestDispatcher("admin.html").forward(request, response);
                }
            } else {
                request.setAttribute("failureMessage", "Incorrect username/password!");
                request.getRequestDispatcher("lndex.jsp").include(request, response);
            }
 
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            out.println("<html><body><b>Error: " + e.getMessage() + "</b></body></html>");
        }
}
}