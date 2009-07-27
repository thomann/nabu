<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="ch.unizh.ori.nabu.ui.http.Base64"%>
<%@taglib uri="ch.unizh.ori.nabu" prefix="nabu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import var="theJS" url="/nabujs.html?id=${param.id}"/><%
pageContext.setAttribute("theJS", new String(Base64.encodeBase64Chunked(((String)pageContext.getAttribute("theJS")).getBytes("UTF-8")),"ASCII") );
%>
<html><head><meta http-equiv="Refresh" content="0; URL=data:text/html;base64,${theJS}">
</head><body>
</body></html>
