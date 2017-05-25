<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="nabu" uri="ch.unizh.ori.nabu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql2" uri="http://java.sun.com/jsp/jstl/sql" %>
<fmt:requestEncoding value="UTF-8"/>
<nabu:makeVoc central="${central}" separator="${(empty param.columnSep)?':':param.columnSep}"
dontWrite="true"><?xml version="1.0" encoding="UTF-8" ?>
<items>
    <vocabulary id="${vars.id}">
        <name>${vars.name}</name>
        <description>${vars.description}</description>
        <mapping>
	<c:forEach items="${columns}" var="i">
	<% request.setAttribute("len", new Integer( ((String[])pageContext.getAttribute("i")).length )); %>
            <col id="${i[0]}" name="${i[1]}"<c:if test="${len>=3}"> script="${i[2]}"</c:if><c:if test="${len>=4}"> del="${i[3]}"</c:if>/>
	</c:forEach>
        </mapping>
	<c:set var="srcXml"><src src="${vars.id}.txt" enc='<c:out value="${vars.enc}" default="UTF-8"/>'
		<c:if test="${!empty vars.lfmt}">label="${vars.lfmt}"</c:if>
		<c:if test="emptyline" >class="ch.unizh.ori.nabu.voc.EmptyLineSource"</c:if>
	/></c:set><c:out value="${srcXml}" escapeXml="false"/>
	<c:if test="${vars.db}">
            <src table="gen_<c:out value="${vars.id}"/>" lesson="lesson" res="jdbc/Nabu"
		class="ch.unizh.ori.nabu.input.db.NamingDBSource"/>
        </c:if>
	<modes>
	<c:forEach items="${modes}" var="i" varStatus="status">
	<% request.setAttribute("len", new Integer( ((String[])pageContext.getAttribute("i")).length )); %>
	    <mode id="${status.index}" short="${i[0]}"<c:if test="${len>=2}"> name="${i[1]}"</c:if>/>
	</c:forEach>
	</modes>
    </vocabulary>
</items>
<c:if test="${false && vars.db}">
<nabu:readSource central="${central}" base="${vocUrl}" sourceXml="${srcXml}"><!--
    <sql:update>
        DROP TABLE gen_${vars.id} IF EXISTS;
    </sql:update>
    <sql:update>
        CREATE TABLE gen_${vars.id}(
        	INTEGER vocID INDEX,
            <c:forEach items="${columns}" var="i" varStatus="info">
                ${i[0]} VARCHAR(254),
            </c:forEach>
        lesson VARCHAR(254) INDEX);
    </sql:update>
    <c:set var="mycount" value="0"/>
    <c:forEach items="${lessons}" var="l">
        <c:forEach items="${l.collection}" var="row">
            <sql:update>
                INSERT INTO gen_${vars.id} (
                    <c:forEach items="${columns}" var="i">
                        ${i[0]},
                    </c:forEach>
                lesson) VALUES (
                    <c:forEach items="${columns}" varStatus="i">
                        ?,
                    </c:forEach>
                ?,?)
                <c:forEach items="${columns}" varStatus="i">
                    <sql:param value="${row[i.index]}"/>
                </c:forEach>
                <sql:param value="${l.id}"/>
                <sql:param value="${mycount}"/>
                <c:set var="mycount" value="${mycount+1}"/>
            </sql:update>
        </c:forEach>
    </c:forEach>	-->
</nabu:readSource></c:if>
</nabu:makeVoc>
<c:if test="${showConfig eq 'on'}">
  <c:redirect url="/admin${subs}/newVocabulary.jsp?what=${id}"/>
</c:if>
<c:if test="${showConfig eq 'lucky'}">
  <c:redirect url="${sub}/selectVoc.jsp?id=${id}"/>
</c:if>
<html>
<head><title>Adding ${name}</title>
<meta http-equiv="Refresh" content="0,<c:url value="selectVoc.jsp?myVoc=true" />"/>
</head>
<body>
<a href='<c:url value="overview.jsp" />'>Overview</a>

<h1>Adding ${name}</h2>
The vocabulary ${name} has been successfully updated.
<br/>
<a href='<c:url value="selectVoc.jsp?myVoc=on" />'>${name}</a>
</body>
</html>
	
