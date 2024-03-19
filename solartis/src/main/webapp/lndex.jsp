<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Library Management System</title>
<style>
    /* Ensuring the html and body cover the full height of the screen */
    html {
        height: 100%;
    }
    body {
        margin: 0;
        padding: 0;
        font-family: Arial, sans-serif;
        background-image: url('https://gameost.net/wp-content/uploads/2017/07/bungo-and-alchemist-music-collection.jpg');
        background-position: center center;
        background-repeat: no-repeat;
        background-size: cover; /* Cover the entire page */
        height: 100%;
    }
    .form-container {
        border: 2px solid #000;
        padding: 20px;
        width: 500px;
        margin: auto;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        background-color: rgba(255, 255, 255, 0.8);
        position: relative;
        top: 50%;
        transform: translateY(-50%);
        text-align: center;
    }
    .input-field, .submit-button {
        display: block;
        width: 90%;
        margin: 10px auto;
        padding: 10px;
    }
    .submit-button {
        background-color: #FFA500;
        color: white;
        border: none;
        cursor: pointer;
    }
    .failure-message {
            color: red;
            margin-top: 10px;
    }
</style>
</head>
<body>

<div class="form-container">
    <h1>Welcome to the Library Management System</h1>
    <form action="LibraryManagementServlet" method="post">
        <input type="text" name="username" placeholder="Username" required class="input-field">
        <input type="password" name="password" placeholder="Password" required class="input-field">
        <input type="submit" value="Login" class="submit-button">
        
    </form>
    
    <div class="failure-message">
        <% String failureMessage = (String) request.getAttribute("failureMessage"); %>
        <% if (failureMessage != null) { %>
            <%= failureMessage %>
        <% } %>
    </div>
</div>


</body>
</html>
