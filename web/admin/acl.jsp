<%@page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@page import="ch.unizh.ori.nabu.ui.http.HttpCentral"%>
<c:if test="${!isAdmin}"><c:redirect url="/admin${sub}/vocabularies.jsp"/></c:if>
<c:if test="${!empty param.saveAcl && isAdmin}">
<c:set target="${central}" property="tutorsString" value="${param.tutors}"/>
<c:set target="${central}" property="adminsString" value="${param.admins}"/></c:if>

<html>
<tag:adminHeader title="Access Control"/>

<form action="acl.jsp">
Tutors: <textarea cols="12" rows="8" name="tutors"><c:forEach items="${central.tutors}" var="tutor">${tutor}
</c:forEach></textarea>
<br/>
Admins: <textarea cols="12" rows="8" name="admins"><c:forEach items="${central.admins}" var="admin">${admin}
</c:forEach></textarea>
<br/>
<input type="submit" name="saveAcl" value="ok">
</form>

</body>
</html>
