<%--
  Created by IntelliJ IDEA.
  User: student-4357
  Date: 2/18/26
  Time: 6:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form action="login" method="POST">
    <table>
        <th>Login</th>
        <tr>
            <td><label htmlFor="user">User: </label></td>
            <td><input type="text" name="user"></td>
        </tr>
        <tr>
            <td><label htmlFor="password">Password: </label></td>
            <td><input type="password" name="password"></td>
        </tr>
        <tr>
            <td><input type="submit" value="login"></td>
        </tr>
    </table>
</form>
</body>
</html>
