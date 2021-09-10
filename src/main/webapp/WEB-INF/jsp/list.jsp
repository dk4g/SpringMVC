<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>List</title>
</head>
<h2> User List</h2>
<body>
    <table>
        <tr>
            <th>First Name</th>
            <th>Last Name</th>
            <th>City</th>

        </tr>
        <c:forEach var="user" items="${userList}">
            <tr>
               <td> <c:out value="${user.firstName}" /> </td>

               <td> <c:out value="${user.lastName}" /></td>

               <td> <c:out value="${user.city}" /></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>