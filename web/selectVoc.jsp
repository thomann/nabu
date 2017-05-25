<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="ch.unizh.ori.nabu" prefix="nabu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="input" uri="http://jakarta.apache.org/taglibs/input-1.0" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:choose>
<c:when test="${param.myVoc}"><c:set value="${myVoc}" var="voc"/></c:when>
<c:otherwise><c:set value="${central.vocs[param.id]}" var="voc"/></c:otherwise>
</c:choose>
<c:set var="markedQuestions" value="${null}"/>
<nabu:selectVoc voc="voc" show="/showQuestion.jsp">
<html>
<head>
<title><fmt:message key="selectVoc"/> ${voc.name}</title>
<link rel=stylesheet type="text/css" href="nabustyle.css">
</head>
<body class="general">
<table>
<tr><td>
<img src="images/nabulogo8xs.png"></td><td><h1><fmt:message key="selectVoc.voc"/> ${voc.name}</h1></td></tr>
<c:if test="${!empty voc.description}"><tr><td></td><td>${voc.description}</td></tr></c:if>
</table>

<a href='<c:url value="${sub}/overview.jsp"/>'><fmt:message key="overview"/></a>
| <a href="alternate.jsp?id=${param.id}"><fmt:message key="selectVoc.alternate"/></a>
<c:if test="${advanced}"> | <a href='<c:url value="admin/vocabularies.jsp"/>'><fmt:message key="admin"/></a> (<fmt:message key="needsLogin"/>)</c:if>


<form method=GET action='<c:url value="selectVoc.jsp"/>'>
<input type="hidden" name="id" value="${param.id}"/>
<input type="hidden" name="myVoc" value="${param.myVoc}"/>

<p><fmt:message key="selectVoc.preMode"/>:</p>

<ul>
<c:forEach items="${voc.modes}" var="i" varStatus="stat">
  <c:set var="mytest" value="${i.value.id == param.mode}"/>
  <c:if test="${empty param.mode }"><c:set var="mytest" value="${stat.index == 0}"/></c:if>
  <li> <input type=radio name="mode" id="${i.value.id}" value="${i.value.id}"<c:if test="${mytest}"> checked</c:if>>
    <label for="${i.value.id}">${i.value.name}</label>
	<c:if test="${!empty i.value.description}"><br/>${i.value.description}</c:if></li>
</c:forEach>
</ul>

<p><fmt:message key="selectVoc.preLesson"/>:</p>

<ul><%
	ch.unizh.ori.nabu.voc.Vocabulary voc = (ch.unizh.ori.nabu.voc.Vocabulary)pageContext.getAttribute("voc");
	Boolean b = (voc != null && voc.getLections() != null && voc.getLections().size() > 1)?Boolean.TRUE:Boolean.FALSE;
	pageContext.setAttribute("hasLessons", b);
	int index=0;
%>
  <c:if test="${hasLessons}"><li> <input:checkbox name="l-all"  attributesText="id='l-all'" value="on"/> <label for="l-all"><fmt:message key="selectVoc.allVoc"/></label></li></c:if>
<c:forEach items="${voc.lections}" var="i"><c:set var="myid" value="l.${i.id}"/>
  <li> <input:checkbox name='<%= (String)pageContext.getAttribute("myid") %>' value="on"
  	default='<%= (b.booleanValue() && index++==0)?"on":"" %>' attributesText="id='${myid}'"/>
    <label for="${myid}">${i.name} (<fmt:message key="selectVoc.words"><fmt:param value="${i.count}"/></fmt:message>)
	<c:if test="${!empty i.description}"><br/>${i.description}</c:if></label>
    </li>
</c:forEach>
</ul>
<input:hidden name="-play" default="false"/>
<input type="submit" value='<fmt:message key="selectVoc.ok"/>'/>
</form>

</body>
</html>
</nabu:selectVoc>
