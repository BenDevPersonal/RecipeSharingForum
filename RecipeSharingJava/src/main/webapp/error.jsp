<%--
  Created by IntelliJ IDEA.
  User: student-4357
  Date: 2/23/26
  Time: 1:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
<a href="index.jsp">Go back to index page</a>
<h1>Title: ${requestScope.errorTitle}</h1>
<h2>Content: ${requestScope.errorMessage}</h2>
</body>
</html>
