package com.solartis.training.sample;
 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
 
@WebServlet("/SearchBookServlet")
public class SearchBookServlet extends HttpServlet {
    private Connection getConnection() throws SQLException {
        String dbUrl = "jdbc:mysql://localhost:3306/ashutosh";
        String user = "root";
        String password = "password";
        return DriverManager.getConnection(dbUrl, user, password);
    }
 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchBy = request.getParameter("searchBy");
        String keyword = request.getParameter("keyword");
        response.setContentType("text/html");
 
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM books WHERE ";
            switch (searchBy) {
                case "author":
                    query += "author LIKE ?";
                    break;
                case "title":
                    query += "title LIKE ?";
                    break;
                case "id":
                    query += "id = ?";
                    break;
                default:
                    // Default to searching by author if invalid search criteria is provided
                    query += "author LIKE ?";
                    break;
            }
 
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                if (searchBy.equals("id")) {
                    // Parse the keyword as an integer for ID search
                    stmt.setInt(1, Integer.parseInt(keyword));
                } else {
                    stmt.setString(1, "%" + keyword + "%");
                }
 
                try (ResultSet rs = stmt.executeQuery()) {
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("<h2>Search Results for " + searchBy + ": " + keyword + "</h2>");
                    while (rs.next()) {
                        response.getWriter().println("ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") +
                                ", Author: " + rs.getString("author") + ", Year: " + rs.getInt("publish_year") + "<br>");
                    }
                    response.getWriter().println("</body></html>");
                }
            }
        } catch (SQLException e) {
            response.getWriter().println("Database error: " + e.getMessage());
        }
    }
 
}