<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!-- <script>
$(function(){
    $("#search").autocomplete({
        source: "<c:url value='/autocomplete/course' />",
        select: function(event, ui) {
            if( ui.item )
                window.location.href = "view?id=" + ui.item.id;
        }
    });
});
</script> -->

<%-- <ul id="title">
<li>Job</li>
<security:authorize access="authenticated and principal.admin">
<li class="align_right"><a href="create"><img title="Create Course" alt="[Create Course]"
    src="<c:url value='/img/icons/table_add.png' />" /></a></li>
</security:authorize>
</ul> --%>

<form action="search.html" method="get">
<p>Job:<input id="search" name="sbox" type="text" class="forminput" size="40"
  value="${param.sbox}" />
<input name="search" type="submit" value="Search" /></p>
</form>

<c:if test="${not empty jobs}">
<table class="viewtable autowidth">
<tr><th>id</th><th>Title</th></tr>
<c:forEach items="${jobs}" var="job">
<tr>
  <td>${job.id}</td>
  <td><a href="<c:url value='/job/view.html?id=${job.id}' />">${job.title}</a></td>
</tr>
</c:forEach>
</table>
</c:if>
