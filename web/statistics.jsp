<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Nabu - <fmt:message key="statistics"/></title>
<link rel=stylesheet type="text/css" href="nabustyle.css"></head>
<body class="general">
<nabu:header/>
<table>
<tr><td valign="top">
<img heigth="43"width="73" src="images/nabulogo8xs.png">
</td><td valign="center">
<h1><fmt:message key="statistics"/></h1>
</td></td>
</table>

<fmt:message>statistics.preTable</fmt:message>

<table border=1>
<tr><td><fmt:message key="statistics.asked"/></td><td>${iter.statistics.asked}</td></tr>
<tr><td><fmt:message key="statistics.completed"/></td><td>${iter.statistics.completed}</td></tr>
<tr><td><fmt:message key="statistics.timePerAsked"/></td><td>${iter.statistics.timePerAsked / 1000} s</td></tr>
<tr><td><fmt:message key="statistics.timePerCompleted"/></td><td>${iter.statistics.timePerCompleted / 1000} s</td></tr>
<tr><td><fmt:message key="statistics.totalTime"/></td><td>${iter.statistics.totalTime / 1000} s</td></tr>
<tr><td><fmt:message key="statistics.tries"/></td><td>${iter.statistics.tries}</td></tr>
</table>

<a href='<c:url value="${newVocUrl}"/>'><fmt:message key="showQuestion.newSameVoc"/> <c:out value="${newVocName}"/></a>
| <a href='<c:url value="${sub}/overview.jsp"/>'><fmt:message key="overview"/></a>
<c:if test="${advanced}"> | <a href='<c:url value="admin/vocabularies.jsp"/>'><fmt:message key="admin"/></a> (<fmt:message key="needsLogin"/>)</c:if>

<c:if test="${advanced}">
<form action="makeMyVoc.jsp" method="POST">
<textarea cols="100" rows="10" name="voc">
<c:set var="vocString"><c:forEach var="i" items="${iter.problemsList}"><c:set var="line"><c:forEach var="c" items="${iter.lastRenderer.mode.voc.columns}"
>${i[c.id] }	</c:forEach></c:set>${fn:substring(line,0,-1)}
</c:forEach></c:set>${ fn:substring(vocString,0,-1)
}</textarea>
<input type=hidden name="vocName" value="${newVocName} <fmt:message key="statistics.problemsSuffix"/>"/>
<input type=hidden name="name" value="${newVocName} <fmt:message key="statistics.problemsSuffix"/>"/>
<input type=hidden name="id" value="myVoc"/>
<c:set var="modes"><c:forEach items="${iter.lastRenderer.mode.voc.modes}" var="m">${m.value.short}:${m.value.name}
</c:forEach></c:set>
<input type=hidden name="modes" value="${fn:substring(modes,0,-1)}"/>

<c:set var="columns"><c:forEach var="c" items="${iter.lastRenderer.mode.voc.columns}">${c.id}:${c.name }:${c.script}
</c:forEach></c:set>
<input type=hidden name="columns" value="${fn:substring(columns,0,-1) }"/>

<input type=submit value='<fmt:message key="statistics.askProblems"/>'/>
</form>
<hr>
<form action="makeMyVoc.jsp" method="POST">
<textarea cols="100" rows="10" name="voc">
<c:set var="vocString"><c:forEach var="i" items="${markedQuestions}"><c:set var="line"><c:forEach var="c" items="${iter.lastRenderer.mode.voc.columns}"
>${i.value[c.id] }	</c:forEach></c:set>${fn:substring(line,0,-1)}
</c:forEach></c:set>${ fn:substring(vocString,0,-1)
}</textarea>
<input type=hidden name="vocName" value="${newVocName} <fmt:message key="statistics.markedSuffix"/>"/>
<input type=hidden name="name" value="${newVocName} <fmt:message key="statistics.markedSuffix"/>"/>
<input type=hidden name="id" value="myVoc"/>
<c:set var="modes"><c:forEach items="${iter.lastRenderer.mode.voc.modes}" var="m">${m.value.short}:${m.value.name}
</c:forEach></c:set>
<input type=hidden name="modes" value="${fn:substring(modes,0,-1)}"/>

<c:set var="columns"><c:forEach var="c" items="${iter.lastRenderer.mode.voc.columns}">${c.id}:${c.name }:${c.script}
</c:forEach></c:set>
<input type=hidden name="columns" value="${fn:substring(columns,0,-1) }"/>

<input type=submit value='<fmt:message key="statistics.askMarked"/>'/>
</form>
</c:if>
</body>
</html>
