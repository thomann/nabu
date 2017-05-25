<%@ attribute name="title" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="input" uri="http://jakarta.apache.org/taglibs/input-1.0" %>
<head>
<title>${title}</title>
<link rel=stylesheet type="text/css" href="adminstyle.css">
</head>
<body>
<a href='<c:url value="vocabularies.jsp" />'>Vocabularies</a>
<a href='<c:url value="${sub}/overview.jsp" />'>Overview</a>
<a href='<c:url value="reload.jsp" />'>Reload</a><br/>
<a href='<c:url value="simpleNewVocabulary.jsp" />'>New Simple Vocabulary</a>
<a href='<c:url value="newVocabulary.jsp" />'>New Complex Vocabulary</a>
<c:if test="${isAdmin}"><a href='<c:url value="acl.jsp" />'>Access Control</a></c:if>

<h1>${title}</h1>

