<%@page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Vocabularies</title></head>
<body>
<a href="<c:url value="vocabularies.jsp" />">Vocabularies</a>
<a href="<c:url value="${sub}/overview.jsp" />">Overview</a>
<a href="<c:url value="reload.jsp" />">Reload</a>

<h1>Vocabularies</h1>

<a href="<c:url value="simpleNewVocabulary.jsp" />">New Simple Vocabulary</a>
<a href="<c:url value="newVocabulary.jsp" />">New Complex Vocabulary</a>

<ul><c:forEach items="${central.uploadVocList}" var="i">
 <li>${i}:
 	<a href="<c:url value="newVocabulary.jsp?what=${i}"/>">Edit</a>
 	<a href="<c:url value="${sub}/selectVoc.jsp?id=${i}"/>">View</a>
 	<a href="<c:url value="remove.jsp?what=${i}"/>">Remove</a>
 	<a href="<c:url value="reload.jsp?what=${i}"/>">Reload</a>
 </li>
</c:forEach></ul>
</body>
</html>
