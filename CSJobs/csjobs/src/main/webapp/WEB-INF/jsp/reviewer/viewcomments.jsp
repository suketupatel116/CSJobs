<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CS Jobs - Reviewer</title>
</head>
<body>
<h2>CS Jobs (Exam Edition)</h2>

<p>You are logged in as <em><security:authentication property="principal.name" /></em>.</p>
<form action="<c:url value='/logout' />" method="post">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
  <input name="submit" type="submit" value="Logout" />
</form>
<p>
  <security:authorize access="hasRole('ROLE_ADMIN')">
    [<a href="<c:url value='/admin.html' />">Admin Home</a>]
  </security:authorize>
  <security:authorize access="hasRole('ROLE_REVIEWER')">
    [Reviewer Home]
  </security:authorize>
    [<a href="<c:url value='/applicant.html' />">Applicant Home</a>]
</p>

<table border='1'>

<c:forEach items="${reviews}" var="review">
<tr><td>${review.reviewer.email}</td><td>${review.comments}</td></tr>
		</c:forEach></table>
	
<form action="tonextround.html" method="post">
<input type="hidden" name="id" value="${application.id}"/>
<input type="hidden" name="roundId" value="${roundId}"/>
<table>
<tr><td>Send to next Round? -> <input type="submit" name="yes" value="Yes" /></td>
<td><input type="hidden" name="_csrf" value="${_csrf.token}" /></td>
</form>

<form action="no.html" method="post">

<td>/<input type="submit" name="no" value="No" /></td></tr>
</table><input type="hidden" name="_csrf" value="${_csrf.token}" /></form>
</body>
</html>
