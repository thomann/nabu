<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="ch.unizh.ori.nabu" prefix="nabu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="input" uri="http://jakarta.apache.org/taglibs/input-1.0" %>
<c:set value="${central.vocs[param.id]}" var="voc"/>
<% session.setAttribute("userInput", null); %>
<jsp:useBean id="userInput" class="ch.unizh.ori.cruciverbalismus.VocabularyConfigBean" scope="session"/>
<c:set target="${userInput}" property="voc" value="${voc}"/>
<c:if test="${!empty param.mode }"><c:set target="${userInput}" property="mode" value="${voc.modes[param.mode]}"/></c:if>
<% userInput.getLessons().clear(); %>
<c:forEach items="${voc.lections}" var="i">
	<c:set var="myid" value="l.${i.id}"/>
	<c:if test="${!empty param[myid] || !empty param['l-all']}">
	<% userInput.getLessons().add(pageContext.getAttribute("i")); %>
	<c:set var="hasLessons" value="${true }"/>
	</c:if>
</c:forEach>
<c:if test="${param.toCruci eq 'true' && hasLessons}"><c:redirect url="cruciverbalismus/webcrossword.jsp"/></c:if>
<html>
<head>
<title>Cruciverbalismus Voc ${voc.name}</title>
<link rel=stylesheet type="text/css" href="nabustyle.css">
</head>
<body class="general">-${userInput }-
<table>
<tr><td>
<img src="images/nabulogo8xs.png"></td><td><h1>Cruciverbalismus ${voc.name}</h1></td></tr>
<c:if test="${!empty voc.description}"><tr><td></td><td>${voc.description}</td></tr></c:if>
</table>

<a href='<c:url value="${sub}/overview.jsp"/>'>&Uuml;bersicht</a>
<c:if test="${advanced}"> | <a href='<c:url value="admin/vocabularies.jsp"/>'>Verwaltung</a> (braucht Berechtigung)</c:if>

<c:if test="${param.toCruci eq 'true' }">
<p style="color: red" class="message">Keine Lektionen Ausgew&auml;hlt!</p>
</c:if>

<c:url value="cruciverbalismus.jsp" var="theUrl"/>
<input:form bean="xrt" action='<%=(String)pageContext.getAttribute("theUrl")%>'>
<input type="hidden" name="id" value="${param.id}"/>

<p>W&auml;hlen Sie den Modus:</p>

<ul>
<c:forEach items="${voc.modes}" var="i" varStatus="stat">
  <c:set var="mytest" value="${i.value.id == param.mode}"/>
  <c:if test="${empty param.mode }"><c:set var="mytest" value="${stat.index == 0}"/></c:if>
  <li> <input type=radio name="mode" id="${i.value.id}" value="${i.value.id}"<c:if test="${mytest}"> checked</c:if>>
    <label for="${i.value.id}">${i.value.name}</label>
	<c:if test="${!empty i.value.description}"><br/>${i.value.description}</c:if></li>
</c:forEach>
</ul>

<p>W&auml;hlen Sie die Lektionen:</p>

	<ul>
		<li><input type=checkbox name="l-all" id="l-all" /> <label for="l-all">Alle Vokabeln</label></li>
		<c:forEach items="${voc.lections}" var="i">
			<c:set var="myid" value="l.${i.id}" />
			<li><input:checkbox attributesText="id='${myid}'"
				name='<%= (String)pageContext.getAttribute("myid") %>' value="on" />
			<label for="${myid}">${i.name} (${i.count} W&ouml;rter)</label>
			<c:if test="${!empty i.description}">
				<br />
				${i.description}
			</c:if></li>
		</c:forEach>
	</ul>

<input type="hidden" name="toCruci" value="true"/>
<input type="submit" />
</input:form>

</body>
</html>
