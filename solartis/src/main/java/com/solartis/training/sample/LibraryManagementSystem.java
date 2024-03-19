


package com.solartis.training.sample;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import jakarta.servlet.http.HttpServletResponse;



public class LibraryManagementSystem implements AutoCloseable {
    private final Connection connection;
    private final Scanner scanner = new Scanner(System.in);
    private final String adminUsername = "admin";
    private final String adminPassword = "admin123";
    private final String librarianUsername = "librarian";
    private final String librarianPassword = "librarian123";

    public LibraryManagementSystem(String dbUrl, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl, user, password);
        initializeDatabase();
    }

	private void initializeDatabase() {
        try (Statement statement = connection.createStatement()) {
            String booksTable = "CREATE TABLE IF NOT EXISTS books (" +
                                "id INT PRIMARY KEY," +
                                "title VARCHAR(255) NOT NULL," +
                                "author VARCHAR(255) NOT NULL," +
                                "publish_year INT," +
                                "is_borrowed BOOLEAN DEFAULT FALSE" +
                                ");";
            String studentsTable = "CREATE TABLE IF NOT EXISTS students (" +
                                   "id INT PRIMARY KEY," +
                                   "name VARCHAR(255) NOT NULL" +
                                   ");";
            String studentBooksTable = "CREATE TABLE IF NOT EXISTS student_books (" +
                                       "student_id INT," +
                                       "book_id INT," +
                                       "borrow_date DATE," +
                                       "renewals INT DEFAULT 0," +
                                       "PRIMARY KEY (student_id, book_id)," +
                                       "FOREIGN KEY (student_id) REFERENCES students(id)," +
                                       "FOREIGN KEY (book_id) REFERENCES books(id)" +
                                       ");";
            statement.execute(booksTable);
            statement.execute(studentsTable);
            statement.execute(studentBooksTable);
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
    
    public void run() throws SQLException {
        System.out.println("Welcome to the Library Management System!");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (username.equals(adminUsername) && password.equals(adminPassword)) {
            handleAdminTasks();
        } else if (username.equals(librarianUsername) && password.equals(librarianPassword)) {
            handleLibrarianTasks();
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void handleAdminTasks() throws SQLException {
        System.out.println("Admin Panel");
        System.out.println("1. Add a Book");
        System.out.println("2. Add a Student");
        System.out.print("Select an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter book ID: ");
                int id = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter book title: ");
                String title = scanner.nextLine();
                System.out.print("Enter author name: ");
                String author = scanner.nextLine();
                System.out.print("Enter publish year: ");
                int year = scanner.nextInt();
			addBook(id, title, author, year);
                break;
            case 2:
                System.out.print("Enter student ID: ");
                int studentId = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter student name: ");
                String name = scanner.nextLine();
			addStudent(studentId, name);
                break;
            default:
                System.out.println("Invalid option.");
                break;
        }
    }

    private void handleLibrarianTasks() throws SQLException {
        System.out.println("Librarian Panel");
        System.out.println("1. Borrow a Book");
        System.out.println("2. Renew a Book");
        System.out.println("3. Generate Report");
        System.out.println("4. Return a Book");
        System.out.println("5. Search Books");
        System.out.println("6. Check Book Status");
        System.out.print("Select an option: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.print("Enter student ID: ");
                int studentId = scanner.nextInt();
                System.out.print("Enter book ID: ");
                int bookId = scanner.nextInt();
			if (borrowBook(studentId, bookId)) {
			    System.out.println("Book borrowed successfully.");
			} else {
			    System.out.println("Failed to borrow the book.");
			}
                break;
            case 2:
                System.out.print("Enter student ID: ");
                int sId = scanner.nextInt();
                System.out.print("Enter book ID: ");
                int bId = scanner.nextInt();
			if (renewBook(sId, bId)) {
			    System.out.println("Book renewed successfully.");
			} else {
			    System.out.println("Failed to renew the book.");
			}
                break;
                
            case 3:
                //generateReport(); // Call to generate the report
                break;  
            case 4:
                System.out.print("Enter student ID: ");
                int sid = scanner.nextInt();
                System.out.print("Enter book ID: ");
                int bid = scanner.nextInt();
                if (returnBook(sid, bid)) {
                    System.out.println("Book returned successfully.");
                } else {
                    System.out.println("Failed to return the book.");
                }
                break;
            case 5:
                searchBooks();
                break; 
            case 6:
                System.out.print("Enter book ID: ");
                int bd = scanner.nextInt();
                checkBookStatus(bd);
                break;    
            default:
                System.out.println("Invalid option.");
                break;
        }
    }
   
    // Add a book to the database
    private void addBook(int id, String title, String author, int publishYear) {
        try {
            if (!bookExists(id, "books")) {
                String sql = "INSERT INTO books (id, title, author, publish_year) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    pstmt.setString(2, title);
                    pstmt.setString(3, author);
                    pstmt.setInt(4, publishYear);
                    pstmt.executeUpdate();
                    System.out.println("Book added successfully.");
                }
            } else {
                System.out.println("Book with ID " + id + " already exists.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    // Add a student to the database
    private void addStudent(int id, String name) {
        try {
            if (!studentExists(id, "students")) {
                String sql = "INSERT INTO students (id, name) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    pstmt.setString(2, name);
                    pstmt.executeUpdate();
                    System.out.println("Student added successfully.");
                }
            } else {
                System.out.println("Student with ID " + id + " already exists.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    // Check if a book or student exists in the database
 // Note: This is a continuation of the previous code and focuses on previously placeholder methods.
    public boolean isActive(int studentId) throws SQLException {
        String checkIsActive = "SELECT isActive FROM students WHERE id = ?";
        try (PreparedStatement isActiveStmt = connection.prepareStatement(checkIsActive)) {
            isActiveStmt.setInt(1, studentId);
            ResultSet isActiveRs = isActiveStmt.executeQuery();
            if (isActiveRs.next()) {
                return isActiveRs.getBoolean("isActive");
            } else {
                return false; // Assuming inactive if student not found
            }
        }
    }

 // Borrow a book
 // Borrow a book
    public boolean borrowBook(int studentId, int bookId) {
        try {
            // Check if the student has already borrowed 5 books
            int borrowedBooks = countBooksBorrowedByStudent(studentId);
            if (borrowedBooks >= 5) {
                System.out.println("This student has already borrowed 5 books.");
                return false;
            }
            
            String checkIsActive = "SELECT isActive FROM students WHERE id = ?";
            try (PreparedStatement isActiveStmt = connection.prepareStatement(checkIsActive)) {
                isActiveStmt.setInt(1, studentId);
                ResultSet isActiveRs = isActiveStmt.executeQuery();
                if (isActiveRs.next()) {
                    boolean isActive = isActiveRs.getBoolean("isActive");
                    if (!isActive) {
                        System.out.println("Student is not active.");
                        return false;
                    }
                } else {
                    System.out.println("Student not found.");
                    return false;
                }
            }

            // Check if the book is already borrowed
            String checkBook = "SELECT is_borrowed FROM books WHERE id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkBook)) {
                checkStmt.setInt(1, bookId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getBoolean("is_borrowed")) {
                    System.out.println("This book is currently borrowed.");
                    return false;
                }
            }

            // Insert borrow record in student_books
            String borrowSql = "INSERT INTO student_books (student_id, book_id, borrow_date, renewals) VALUES (?, ?, ?, ?)";
            try (PreparedStatement borrowStmt = connection.prepareStatement(borrowSql)) {
                borrowStmt.setInt(1, studentId);
                borrowStmt.setInt(2, bookId);
                borrowStmt.setDate(3, Date.valueOf(LocalDate.now()));
                borrowStmt.setInt(4, 0); // Initial renewals count is 0
                borrowStmt.executeUpdate();
            }

            // Mark the book as borrowed
            String updateBook = "UPDATE books SET is_borrowed = ? WHERE id = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateBook)) {
                updateStmt.setBoolean(1, true);
                updateStmt.setInt(2, bookId);
                updateStmt.executeUpdate();
            }

            System.out.println("Book borrowed successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error borrowing book: " + e.getMessage());
            return false;
        }
    }
    public boolean returnBook(int studentId, int bookId) {
        try {
            // Delete the borrow record in student_books
            String returnSql = "DELETE FROM student_books WHERE student_id = ? AND book_id = ?";
            try (PreparedStatement returnStmt = connection.prepareStatement(returnSql)) {
                returnStmt.setInt(1, studentId);
                returnStmt.setInt(2, bookId);
                int affectedRows = returnStmt.executeUpdate();
                if (affectedRows == 0) {
                    System.out.println("No borrow record found for this book and student.");
                    return false;
                }
            }

            // Mark the book as not borrowed
            String updateBook = "UPDATE books SET is_borrowed = FALSE WHERE id = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateBook)) {
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            System.out.println("Error returning book: " + e.getMessage());
            return false;
        }
    }

    private void searchBooks() {
        try {
            System.out.println("Search by: 1. Author 2. Keyword in Title 3. Publish Year");
            System.out.print("Choose an option: ");
            int searchOption = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Declare variables outside the switch to ensure they are accessible in the entire method
            String author = "";
            String keyword = "";
            int year = 0;

            String sql = "SELECT * FROM books WHERE ";
            switch (searchOption) {
                case 1:
                    System.out.print("Enter author's name: ");
                    author = scanner.nextLine();
                    sql += "author LIKE ?";
                    break;
                case 2:
                    System.out.print("Enter keyword in title: ");
                    keyword = scanner.nextLine();
                    sql += "title LIKE ?";
                    break;
                case 3:
                    System.out.print("Enter publish year: ");
                    year = scanner.nextInt();
                    scanner.nextLine(); // Consume newline that may follow the number input
                    sql += "publish_year = ?";
                    break;
                default:
                    System.out.println("Invalid search option.");
                    return;
            }

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                // Set the prepared statement parameter based on user input
                if (searchOption == 1) {
                    pstmt.setString(1, "%" + author + "%");
                } else if (searchOption == 2) {
                    pstmt.setString(1, "%" + keyword + "%");
                } else if (searchOption == 3) {
                    pstmt.setInt(1, year);
                }

                ResultSet rs = pstmt.executeQuery();
                System.out.println("Search Results:");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") +
                                       ", Author: " + rs.getString("author") + ", Year: " + rs.getInt("publish_year"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching books: " + e.getMessage());
        }
    }

    public void changeStudentPassword(int studentId, String newPassword) throws SQLException {
        String sql = "UPDATE student_passwords SET password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, studentId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Password changed successfully for student ID: " + studentId);
            } else {
                System.out.println("Failed to change password for student ID: " + studentId);
            }
        }
    }
    
    
public void checkBookStatus(int bookId) {
    String sql = "SELECT is_borrowed FROM books WHERE id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, bookId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            boolean isBorrowed = rs.getBoolean("is_borrowed");
            String status = isBorrowed ? "borrowed" : "available";
            System.out.println("The book with ID " + bookId + " is currently " + status + ".");
        } else {
            System.out.println("No book found with ID " + bookId + ".");
        }
    } catch (SQLException e) {
        System.out.println("Error checking book status: " + e.getMessage());
    }
}

//Check if a book exists in the books table
private boolean bookExists(int bookId,String name) {
  String sql = "SELECT COUNT(*) FROM books WHERE id = ?";
  try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, bookId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
          return rs.getInt(1) > 0; // True if count > 0
      }
  } catch (SQLException e) {
      System.out.println("Error checking book existence: " + e.getMessage());
  }
  return false;
}

//Check if a student exists in the students table
private boolean studentExists(int studentId,String name) {
  String sql = "SELECT COUNT(*) FROM students WHERE id = ?";
  try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, studentId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
          return rs.getInt(1) > 0; // True if count > 0
      }
  } catch (SQLException e) {
      System.out.println("Error checking student existence: " + e.getMessage());
  }
  return false;
}

private int countBooksBorrowedByStudent(int studentId) throws SQLException {
    String sql = "SELECT COUNT(*) AS book_count FROM student_books WHERE student_id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, studentId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("book_count");
        }
    }
    return 0;
}

//private void generateReport() {
//    try (PDDocument document = new PDDocument()) {
//        PDPage page = new PDPage();
//        document.addPage(page);
//
//        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//            contentStream.beginText();
//            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
//            contentStream.setLeading(20f);
//            contentStream.newLineAtOffset(50, 750);
//
//            String[] headers = {"Total number of books", "Books in circulation", "Number of books available"};
//            // Query for total number of books
//            int totalBooks = getTotalNumberOfBooks();
//            // Query for number of available books
//            int availableBooks = getNumberOfAvailableBooks();
//            int booksInCirculation = totalBooks - availableBooks;
//
//            String[] values = {String.valueOf(totalBooks), String.valueOf(booksInCirculation), String.valueOf(availableBooks)};
//
//            // Draw Table Headers
//            contentStream.showText(headers[0]);
//            contentStream.newLineAtOffset(150, 0);
//            contentStream.showText(headers[1]);
//            contentStream.newLineAtOffset(150, 0);
//            contentStream.showText(headers[2]);
//            contentStream.endText();
//
//            // Draw Line under headers
//            contentStream.moveTo(50, 735);
//            contentStream.lineTo(550, 735);
//            contentStream.stroke();
//
//            // Draw Table Rows
//            contentStream.beginText();
//            contentStream.setFont(PDType1Font.HELVETICA, 12);
//            contentStream.newLineAtOffset(-300, -20);
//            contentStream.showText(values[0]);
//            contentStream.newLineAtOffset(150, 0);
//            contentStream.showText(values[1]);
//            contentStream.newLineAtOffset(150, 0);
//            contentStream.showText(values[2]);
//            contentStream.endText();
//
//            // Close the content stream
//            contentStream.close();
//
//            // Save the document
//            String fileName = "LibraryBooksReport.pdf";
//            document.save("C:\\Users\\ashutosh_l\\Desktop\\" + fileName);
//            System.out.println("PDF report generated: " + fileName);
//        }
//    } catch (Exception e) {
//        System.out.println("Error generating report: " + e.getMessage());
//    }
//}
public void generateReport() {
    try (PDDocument document = new PDDocument()) {
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            float margin = 50;
            // Starting y position
            float yStart = page.getMediaBox().getHeight() - margin;
            // Start drawing from here
            float yPosition = yStart;

            // Title
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(margin+180, yPosition);
            contentStream.showText("Books Availability Report");
            contentStream.endText();

            // Headers
            String[] headers = {"Total Number of books", "Number of books in rotation", "Number of books available"};

            // Draw the headers row
            float nextXPosition = margin;
            float cellWidth = (page.getMediaBox().getWidth() - 2 * margin) / headers.length;
            yPosition -= 20; // Move down for headers row

            // Draw header text and boxes
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
            contentStream.setLineWidth(1f);
            for (String header : headers) {
                contentStream.beginText();
                contentStream.newLineAtOffset(nextXPosition + cellWidth/2 - header.length()*3, yPosition-10); // Rough center
                contentStream.showText(header);
                contentStream.endText();
                contentStream.addRect(nextXPosition, yPosition-15, cellWidth, 20);
                contentStream.stroke();
                nextXPosition += cellWidth;
            }

            // Values
            int totalBooks = getTotalNumberOfBooks();
            int availableBooks = getNumberOfAvailableBooks();
            int booksInCirculation = totalBooks - availableBooks;
            String[] values = {String.valueOf(totalBooks), String.valueOf(booksInCirculation), String.valueOf(availableBooks)};

            // Draw the values row
            nextXPosition = margin;
            yPosition -= 30; // Move down for values row

            // Draw value text and boxes
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            for (String value : values) {
                contentStream.beginText();
                contentStream.newLineAtOffset(nextXPosition + cellWidth/2 - value.length()*3, yPosition-10); // Rough center
                contentStream.showText(value);
                contentStream.endText();
                contentStream.addRect(nextXPosition, yPosition-15, cellWidth, 20);
                contentStream.stroke();
                nextXPosition += cellWidth;
            }

            // Close the content stream
            contentStream.close();

             //Save the document
            String fileName = "LibraryBooksReport9.pdf";
            document.save("C:\\Users\\ashutosh_l\\Desktop\\" + fileName); // Adjust the path as necessary
            System.out.println("PDF report generated: " + fileName);
        }
    } catch (Exception e) {
        System.out.println("Error generating report: " + e.getMessage());
    }
}


public List<String> findOverdueBooks() throws SQLException {
    List<String> overdueBooks = new ArrayList<>();
    String sql = "SELECT student_id, book_id, DATEDIFF(CURDATE(), borrow_date) AS days_overdue FROM student_books WHERE DATEDIFF(CURDATE(), borrow_date) > 45";

    try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            int studentId = rs.getInt("student_id");
            int bookId = rs.getInt("book_id");
            int daysOverdue = rs.getInt("days_overdue");
            overdueBooks.add("Student ID: " + studentId + ", Book ID: " + bookId + ", Days Overdue: " + daysOverdue);
        }
    }
    return overdueBooks;
}

public List<String> findAvailableBooks() throws SQLException {
    List<String> availableBooks = new ArrayList<>();
    String sql = "SELECT id, title, author, publish_year FROM books WHERE is_borrowed = FALSE";

    try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            String bookDetails = "ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") +
                                 ", Author: " + rs.getString("author") + ", Year: " + rs.getInt("publish_year");
            availableBooks.add(bookDetails);
        }
    }
    return availableBooks;
}
public boolean removeStudent(int id) throws SQLException {
    String sql = "DELETE FROM students WHERE id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, id);
        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    }
}

public boolean modifyStudent(int id, String newName) throws SQLException {
    String sql = "UPDATE students SET name = ? WHERE id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, newName);
        pstmt.setInt(2, id);
        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    }
}

public boolean setStudentActiveStatus(int id, boolean isActive) throws SQLException {
    String sql = "UPDATE students SET isActive = ? WHERE id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setBoolean(1, isActive);
        pstmt.setInt(2, id);
        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    }
}






public int getTotalNumberOfBooks() throws SQLException {
    try (Statement statement = connection.createStatement()) {
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS total FROM books");
        if (rs.next()) {
            return rs.getInt("total");
        }
    }
    return 0; // In case of query failure
}


public int getNumberOfAvailableBooks() throws SQLException {
    try (Statement statement = connection.createStatement()) {
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS available FROM books WHERE is_borrowed = FALSE");
        if (rs.next()) {
            return rs.getInt("available");
        }
    }
    return 0; // In case of query failure
}

 // Renew a book
public boolean renewBook(int studentId, int bookId) {
    final int MAX_RENEWALS = 3;
    try {
        // Check the current number of renewals
        String checkRenewals = "SELECT renewals FROM student_books WHERE student_id = ? AND book_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkRenewals)) {
            checkStmt.setInt(1, studentId);
            checkStmt.setInt(2, bookId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    int currentRenewals = rs.getInt("renewals");
                    if (currentRenewals >= MAX_RENEWALS) {
                        System.out.println("Maximum renewals reached for student " + studentId + " and book " + bookId + ".");
                        return false;
                    }
                    
                    // Renew the book
                    String renewSql = "UPDATE student_books SET renewals = renewals + 1, borrow_date = ? WHERE student_id = ? AND book_id = ?";
                    try (PreparedStatement renewStmt = connection.prepareStatement(renewSql)) {
                        renewStmt.setDate(1, Date.valueOf(LocalDate.now()));
                        renewStmt.setInt(2, studentId);
                        renewStmt.setInt(3, bookId);
                        int updatedRows = renewStmt.executeUpdate();
                        if (updatedRows > 0) {
                            System.out.println("Book renewed successfully for student " + studentId + " and book " + bookId + ".");
                            return true;
                        } else {
                            System.out.println("Failed to renew the book for student " + studentId + " and book " + bookId + ".");
                            return false;
                        }
                    }
                } else {
                    System.out.println("No borrowing record found for student " + studentId + " and book " + bookId + ".");
                    return false;
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("Error renewing book for student " + studentId + " and book " + bookId + ": " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}



 // The bookExists method should be fine as-is for checking both books and students; no adjustment needed.

 // Main method, constructor, and other methods like close() remain unchanged.


    // The run method for handling user inputs and interactions
  

    @Override
    public void close() {
        try {
            if (scanner != null) {
                scanner.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Failed to close resources: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String dbUrl = "jdbc:mysql://localhost/ashutosh";
        String user = "root";
        String password = "password";
        //generateReport();
        try (LibraryManagementSystem system = new LibraryManagementSystem(dbUrl, user, password)) {
            system.run();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}


