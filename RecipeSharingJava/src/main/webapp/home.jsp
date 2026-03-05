<%--
  Created by IntelliJ IDEA.
  User: student-4357
  Date: 2/19/26
  Time: 2:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
<h1>Hello, ${sessionScope.user.login} (<a href="${pageContext.request.contextPath}/logout">logout</a>)</h1>
<c:if test="${sessionScope.role.name == 'admin'}">
    <a href="${pageContext.request.contextPath}/users">Admin dashboard</a>
</c:if>

<h2>Your posts</h2>
<table border="1px">
    <th>Title</th><th>Content</th><th>Creation date</th><th>Update date</th>
    <c:forEach items="${sessionScope.posts}" var="post">
        <c:if test="${post.user.id == sessionScope.user.id}">
            <tr>
                <td>${post.title}</td>
                <td>${post.content}</td>
                <td>${post.creationDate}</td>
                <td>${post.updateDate}</td>
            </tr>
        </c:if>
    </c:forEach>
</table>
</body>
</html>
