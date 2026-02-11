<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Countries</title>
</head>
<body>
<h2>Country list</h2>

<table>
    <tr>
        <th>Code</th>
        <th>Name</th>
    </tr>

    <c:forEach var="country" items="${countries}">
        <tr>
            <td>${country.getCode()}</td>
            <td>${country.getName()}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>