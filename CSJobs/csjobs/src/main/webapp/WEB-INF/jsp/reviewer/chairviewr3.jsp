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
<tr><td><table border='1'><td></td><c:forEach items="${reviewers}" var="review1"><td>${review1.firstName}</td></c:forEach></tr>

<c:forEach items="${applications}" var="apps">
<tr>
<td>${apps.applicant.email}</td>
<c:forEach items="${reviewers}" var="reviewer1">

<td>
<c:forEach items="${apps.rounds}" var="round">

<c:forEach items="${round.reviews}" var="review">

<c:if test="${review.reviewer.id == reviewer1.id}">

${review.rank}
</c:if>
</c:forEach>
</c:forEach>
</td>
</c:forEach>
</tr>
		</c:forEach></td></table>
		<td><table border='1'><tr><td>Total</td></tr>
		<c:forEach items="${ttotal}" var="t">
		<tr><td>${t}</td></tr>
		</c:forEach>		
		</table><td>
		
		<td><table border='1'><tr><td>Final Ranking</td></tr>
		<c:forEach items="${tfinalranking}" var="h">
		<tr><td>${h}</td></tr>
			</c:forEach>
		</table><td>
		</table>
<form action="/csjobs-exam/logout" method="POST">
  <input name="_csrf" type="hidden" 
		value="${_csrf.token}" />
  <input name="submit" type="submit" value="Logout" />
</form>


</body>
</html>
