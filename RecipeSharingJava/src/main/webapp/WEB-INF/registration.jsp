<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>

<h2>Register</h2>

<form action="register" method="POST">

Login: <input type="text" name="login"><br>
Email: <input type="text" name="email"><br>
Password: <input type="password" name="password"><br>
Country: <input type="text" name="country"><br>

<input type="submit" value="Register">

</form>

<p style="color:red">${error}</p>

</body>
</html>
