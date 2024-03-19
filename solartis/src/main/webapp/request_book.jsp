<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Request a Book</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .form-container {
            border: 2px solid #000;
            padding: 40px;
            margin: 20px auto;
            width: 60%;
            max-width: 600px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            text-align: center;
            background-color: #f8f9fa;
        }
        .input-field {
            margin-bottom: 10px;
            padding: 5px;
            width: calc(100% - 10px); /* Adjusted to include padding */
        }
        .submit-button {
            padding: 10px;
            background-color: #FFA500;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            width: 100%; /* Makes the button expand to the full width */
        }
        .logout, .main-menu {
		    position: fixed;
		    top: 10px;
		    padding: 10px;
		    background-color: #FFA500;
		    color: white;
		    text-decoration: none;
		    border-radius: 5px;
		}
		
		.logout {
		    right: 70px; /* Adjust the distance from right for "Logout" button */
		}
		
		.main-menu {
		    right: 10px; /* Adjust the distance from right for "Main Menu" button */
		}
    </style>
</head>
<body>
    <h1 style="text-align: center;">Request a Book</h1>
    <div class="form-container">
        <form action="BookRequestServlet" method="post">
            <div>
                <label for="studentId">Student ID:</label><br>
                <input type="number" id="studentId" name="studentId" required class="input-field">
            </div>
            <div>
                <label for="bookId">Book ID:</label><br>
                <input type="number" id="bookId" name="bookId" required class="input-field">
            </div>
            <input type="submit" value="Request Book" class="submit-button">
        </form>
        <a href="student.html" class="main-menu">Main Menu</a>
    </div>
    <div class="failure-message"> 
    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
    <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
        <%= errorMessage %>
    <% } %>
</div>
    
    
    
</body>
</html>
