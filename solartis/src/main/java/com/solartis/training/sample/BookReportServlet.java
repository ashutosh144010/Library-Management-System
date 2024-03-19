package com.solartis.training.sample;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/BookReportServlet")
public class BookReportServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/pdf");
        
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
                String dbUrl = "jdbc:mysql://localhost:3306/ashutosh";
                String user = "root";
                String password = "password";
                LibraryManagementSystem system = new LibraryManagementSystem(dbUrl,user,password);

                // Values
                int totalBooks = system.getTotalNumberOfBooks();
                int availableBooks = system.getNumberOfAvailableBooks();
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
//                String fileName = "LibraryBooksReport9.pdf";
//                document.save("C:\\Users\\ashutosh_l\\Desktop\\" + fileName); // Adjust the path as necessary
//                System.out.println("PDF report generated: " + fileName);
            }
            
            // Write the PDF to the response's output stream
            document.save(response.getOutputStream());
        } catch (Exception e) {
            throw new ServletException("Exception while generating PDF", e);
        }
    }
}
