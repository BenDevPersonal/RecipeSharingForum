<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>User List</title>
</head>
<body>

<h2>Users</h2>

<table border="1">

<tr>
    <th>ID</th>
    <th>Login</th>
    <th>Email</th>
    <th>Country</th>
</tr>

<c:forEach var="user" items="${users}">

<tr>
    <td>${user.id}</td>
    <td>${user.login}</td>
    <td>${user.email}</td>
    <td>${user.country}</td>
</tr>

</c:forEach>

</table>

</body>
</html>