<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Password Changed</title>
    <link rel="stylesheet" href="styles.css"> <!-- Assuming you have a CSS file named styles.css -->
</head>
<style>
.container {
    border: 2px solid #000;
    padding: 40px;
    margin: 20px auto;
    width: 60%;
    max-width: 600px;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
    text-align: center;
    background-color: #f8f9fa;
}
 
.form-container {
    margin-top: 20px; /* Adjust the margin between form and other content */
}
 
input[type="text"],
input[type="password"] {
    margin-bottom: 10px;
    padding: 5px;
    width: calc(100% - 10px); /* Adjust for padding */
}
 
.submit-button {
    padding: 10px;
    background-color: #FFA500;
    color: white;
    text-decoration: none;
    border-radius: 5px;
    border: none;
    cursor: pointer;
}
 
 
</style>
<body>
    <!-- Centering the content -->
    <div class="container">
        <h1 style="color: red">Password Update</h1>
        <!-- Form for entering ID and new password -->
        <form action="ChangePasswordServlet" method="post" class="form-container">
            <label for="studentId">Your ID:</label>
            <input type="text" id="studentId" name="your_Id" required><br>
            <label for="newPassword">New Password:</label>
            <input type="password" id="newPassword" name="newPassword" required><br>
            <input type="submit" value="Change Password" class="submit-button">
        </form>
        <!-- Success message div -->
        <div id="successMessage"></div>
        <!-- Back to Home link -->
        <p><a href="lndex.jsp">Back to Home</a></p>
    </div>
 
    <!-- JavaScript to display success message -->
    <script>
        // Check if the URL contains a success parameter
        const urlParams = new URLSearchParams(window.location.search);
        const successMessage = urlParams.get('success');
        if (successMessage === 'true') {
            // Display the success message below the button
            const successDiv = document.getElementById('successMessage');
            successDiv.innerHTML = '<p style="color: green">Password changed successfully.</p>';
        }
    </script>
</body>
</html>