<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CS Jobs - Admin</title>
</head>
<body>
<form action="email.html" method="post">
<table>
<tr>
	<td>From</td><td><input type="text" name="from"></td>
</tr>
<tr>
	<td>To</td><td><input type="text" name="to"></td>
</tr>
<tr>
	<td>Subject</td><td><input type="text" name="subject"></td>
</tr>
<tr><td><textarea name="content" rows="5" cols="60"></textarea></td></tr>
<tr><td><input type="submit" name="send" value="Send" /></td></tr>
</table>
 <input type="hidden" name="_csrf" value="${_csrf.token}" />
</form>
</body>