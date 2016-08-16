<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CS Jobs - Reviewer</title>

<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
</head>
<body>

<h2>CS Jobs</h2>

<p>You are logged in as <em>${authenticatedUser.firstName} ${authenticatedUser.lastName}</em>.


<h3>Applications</h3>
<form action="applicationsr3.html" method = "post">

<table border="1">
<c:forEach items="${applications2}" var="apps">
<tr><td>${apps.applicant.email}</td>
</c:forEach>

<c:forEach items="${applications}" var="apps" end="0">
<tr>

<td>${apps.applicant.email}</td><td>
<select name='rank' >
<c:forEach items="${dropdown}" var="d">
	<option value="${d}">${d}</option>
</c:forEach>
</select></td>

</tr>
		<input type="hidden" name="appId" value="${apps.id}"/>
		<input type="hidden" name="applicantId" value="${apps.applicant.id}"/>
		<input type="hidden" name="id" value="${id}"/>
		<input type="hidden" name="_csrf" value="${_csrf.token}" />
		<input type="hidden" name="roundId" value="${roundId}"/>
		<tr><td><input type="submit" name="submit" value="Submit"></td></tr>
		</c:forEach>
		</table></form>

<form action="/csjobs-exam/logout" method="POST">
  <input name="_csrf" type="hidden" 
		value="${_csrf.token}" />
  <input name="submit" type="submit" value="Logout" />
</form>

</body>
</html>
