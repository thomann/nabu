<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="ch.unizh.ori.nabu" prefix="nabu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="input" uri="http://jakarta.apache.org/taglibs/input-1.0" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:choose>
<c:when test="${param.myVoc}"><c:set value="${myVoc}" var="voc"/></c:when>
<c:otherwise><c:set value="${central.vocs[param.id]}" var="voc"/></c:otherwise>
</c:choose>
<html>
<head>
<title><fmt:message key="alternate.title"/> ${voc.name}</title>
<link rel=stylesheet type="text/css" href="nabustyle.css">
<link rel="shortcut icon" type="image/vnd.microsoft.icon" href="favicon.ico">
</head>
<body class="general">
<table>
<tr><td>
<img src="images/nabulogo8xs.png"></td><td><h1><fmt:message key="alternate.title"/> ${voc.name}</h1></td></tr>
<c:if test="${!empty voc.description}"><tr><td></td><td>${voc.description}</td></tr></c:if>
</table>

<a href='<c:url value="${sub}/overview.jsp"/>'><fmt:message key="overview"/></a>
| <a href="selectVoc.jsp?id=${param.id}"><fmt:message key="alternate.selectVoc"/></a>
<c:if test="${advanced}"> | <a href='<c:url value="admin/vocabularies.jsp"/>'><fmt:message key="admin"/></a> (<fmt:message key="needsLogin"/>)</c:if>

<p class="versions">
<a href='<c:url value="xrt.jsp?id=${param.id}" />'><fmt:message key="alternate.xrt"/></a>: <fmt:message key="alternate.xrt.desc"/>
</p>
<c:set var="uvl" value="${central.uploadVocList}"/><%
	java.util.List uvl = (java.util.List) pageContext.getAttribute("uvl");
	if( uvl.contains(request.getParameter("id")) ){
%>
<p class="versions">
<a href='<c:url value="getJar/Nabu-${param.id }.jar?id=${param.id}" />'><fmt:message key="alternate.download"/></a>: <fmt:message key="alternate.download.desc"/>
</p>
<p class="versions">
<a href='<c:url value="getJar/Nabuttu-${param.id }.jar?id=${param.id}&type=midlet" />'><fmt:message key="alternate.handy"/></a>: <fmt:message key="alternate.handy.desc"/>
</p>
<%--<p class="versions">
<a href='<c:url value="getJar/NabuttuOld-${param.id }.jar?id=${param.id}&type=midletOld" />'><fmt:message key="alternate.handyOld"/></a>: <fmt:message key="alternate.handyOld.desc"/>
</p>--%>
<p class="versions">
<a href='<c:url value="nabujs.html?id=${param.id}" />'><fmt:message key="alternate.nabujs"/></a>: <fmt:message key="alternate.nabujs.desc"/>
</p>
<%} %>
<h2>Beta-Versionen</h2>
<p class="versions">
<a href='<c:url value="podcast.jsp?id=${param.id}" />'><fmt:message key="alternate.podcast"/></a>: <fmt:message key="alternate.podcast.desc"/>
</p>
<p class="versions">
<a href='<c:url value="cruciverbalismus.jsp?id=${param.id}" />'><fmt:message key="alternate.cruciverbalismus"/></a>: <fmt:message key="alternate.cruciverbalismus.desc"/>
</p>

</body>
</html>
