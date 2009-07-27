<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="nabu" uri="ch.unizh.ori.nabu" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<fmt:requestEncoding value="UTF-8"/>

<html>
<head>
<title>Nabu - Markierte Fragen</title>
<link rel=stylesheet type="text/css" href="nabustyle.css">
</head>
<body class="general">
<nabu:header/>
<table>
<tr><td valign="top">
<img heigth="43"width="73" src="images/nabulogo8xs.png">
</td><td valign="center">
<h1>Markierte Fragen</h1>
</td></td>
</table>

<ul>
<c:forEach items="${markedQuestions}" var="q">
<li><c:out value="${q.key }"/></li>
</c:forEach>
</ul>

</body>
</html>
