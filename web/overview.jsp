<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="ch.unizh.ori.nabu" prefix="nabu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="nabu_msg" var="bundle" scope="page"/>
<c:if test="${param['locale'] != null}">
  <fmt:setLocale value="${param['locale']}" scope="session" />
</c:if>
<c:if test="${!empty param.advanced }"><c:set var="advanced" value="${true }" scope="session"/></c:if>
<%@page import="ch.unizh.ori.nabu.ui.http.HttpCentral"%>
<html>
<head>
<title><fmt:message key="overview"/></title>
<link rel=stylesheet type="text/css" href="nabustyle.css">
</head>
<body class="general">

<nabu:header/>

<h1><img src="images/nabulogo8xs.png"><fmt:message key="overview"/></h1>

<a href="overview.jsp?locale=en-US">English</a> -
<a href="overview.jsp?locale=de-DE">Deutsch</a> -
<a href="overview.jsp?locale=la">Lingua Latina</a>
<!-- - <a href="overview.jsp?locale=ja-JP">&#26085;&#26412;</a> -->

<c:if test="${!advanced}">
<br/>
<form><input type="hidden" name="advanced" value="on"/>
<input type="submit" value="<fmt:message key="overview.poweruserbutton"/>"/></form>
</c:if>
<c:if test="${advanced}"><br/><a href="<c:url value="admin/vocabularies.jsp"/>"><fmt:message key="admin"/></a> (<fmt:message key="needsLogin"/>)</c:if>

<c:if test="${!empty param.message }">
<p style="color: red" class="message"><c:out value="${param.message}" escapeXml="false"/></p>
</c:if>

<p><a href="nabu.jnlp">Offline-Version</a></p>

<p><fmt:message key="overview.preList"/>:</p>

<table class="overview">
<c:forEach items="${central.organization}" var="i" varStatus="vs">
	<tr>
<% if(pageContext.getAttribute("i") instanceof String){ %>
<td class="subtitle"> ${i } </td>
<% }else{ %>
  <td>
  <a href="<c:url value="selectVoc.jsp?id=${i.id}" />">${i.name}</a>
  	<c:if test="${i.count > 0}"> - (<fmt:message key="overview.vocitems"><fmt:param value="${i.count}"/></fmt:message>)<c:set var="total" value="${total + i.count}"/></c:if>
	<c:if test="${!empty i.description}"><br/>${i.description}</c:if>
	</td>
	<c:if test="${advanced}"><td>
	  <a href="xrt.jsp?id=${i.id}" title="<fmt:message key="alternate.xrt"/>"><img src="images/xrt.png"/></a>
	  <c:if test="${i.downloadable}">
		  <a href="getJar/Nabu-${i.id}?id=${i.id}" title="<fmt:message key="alternate.download"/>"><img src="images/desktop.png"/></a>
		  <a href="getJar/Nabuttu-${i.id}?id=${i.id}&type=midlet" title="<fmt:message key="alternate.handy"/>"><img src="images/handy.png"/></a>
		  <a href="nabujs.html?id=${i.id}" title="<fmt:message key="alternate.nabujs"/>"><img src="images/iphone.png"/></a>
	  </c:if>
	  <a href="podcast.jsp?id=${i.id}" title="<fmt:message key="alternate.podcast"/>"><img src="images/rss.png"/></a>
	  <a href="cruciverbalismus.jsp?id=${i.id}" title="<fmt:message key="alternate.cruciverbalismus"/>"><img src="images/cruci.png"/></a>
	</td></c:if>
	<% } %>
</tr>	
</c:forEach>
</table>


<c:if test="${total > 0}">(<fmt:message key="overview.total.vocitems"><fmt:param>${total}</fmt:param></fmt:message>)</c:if>

</body>
</html>
