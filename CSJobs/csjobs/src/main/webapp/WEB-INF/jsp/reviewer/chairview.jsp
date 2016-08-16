<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CS Jobs - Reviewer</title>


</head>
<body>

<h2>CS Jobs</h2>

<p>You are logged in as <em>${authenticatedUser.firstName} ${authenticatedUser.lastName}</em>.

<h3>Applications</h3>
<table>
<c:forEach items="${applications}" var="apps">
<tr>
<td>${apps.applicant.email}</td>
<td><a href="<c:url value='/reviewer/viewcomments.html?id=${apps.id}&roundId=0' />">View Comments</a></td>
</tr>
		</c:forEach></table>
<form action="/csjobs-exam/logout" method="POST">
  <input name="_csrf" type="hidden" 
		value="${_csrf.token}" />
  <input name="submit" type="submit" value="Logout" />
</form>


</body>
</html>
