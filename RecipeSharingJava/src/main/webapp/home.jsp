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
    <a href="${pageContext.request.contextPath}/dashboard">Admin dashboard</a>
</c:if>

<h2>Your posts</h2>
</body>
</html>
