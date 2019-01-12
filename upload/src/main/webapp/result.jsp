<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Result</title>
</head>
<body>
<%--@elvariable id="users" type="java.util.Set"--%>
<%--@elvariable id="user" type="ru.javaops.masterjava.dto.UserDto"--%>
<c:set var="users" value="${requestScope.users}"/>
<table>
    <tr>
        <th>Name</th>
        <th>Email</th>
        <th>Flag</th>
    </tr>
    <c:forEach items="${users}" var="user">
        <tr>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.flagType}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
