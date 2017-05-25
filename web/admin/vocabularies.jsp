<%@page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@page import="ch.unizh.ori.nabu.ui.http.HttpCentral"%>
<jsp:useBean id="central" scope="request" type="ch.unizh.ori.nabu.ui.http.HttpCentral"></jsp:useBean>
<c:if test="${!empty param.up}"><c:set target="${central}" property="organizationUp" value="${param.up}"/></c:if>
<c:if test="${!empty param.down}"><c:set target="${central}" property="organizationDown" value="${param.down}"/></c:if>
<c:if test="${!empty param.addTitle}"><% ((HttpCentral)pageContext.findAttribute("central")).organizationAddTitle("New Title", Integer.parseInt(request.getParameter("addTitle"))); %></c:if>
<c:if test="${!empty param.setTitle}"><% ((HttpCentral)pageContext.findAttribute("central")).organizationSetTitle((String)request.getParameter("setTitle"), Integer.parseInt(request.getParameter("i"))); %></c:if>
<c:if test="${!empty param.removeTitle}"><c:set target="${central}" property="organizationRemoveTitle" value="${param.removeTitle}"/></c:if>

<html>
<tag:adminHeader title="Vocabularies"/>

<table class="vocabularies"><c:forEach items="${central.organization}" var="voc" varStatus="vs">
<% if(pageContext.getAttribute("voc") instanceof String){ %>
<tr class="subtitle">
<td colspan="4"><form action="vocabularies.jsp"><input type="text" name="setTitle" value="${voc}"/>
<input type="hidden" name="i" value="${vs.index}"/></form>
</td><td><a href='<c:url value="vocabularies.jsp?removeTitle=${vs.index}"/>'><img alt="Remove" src='<c:url value="${sub}/images/actions/process-stop.png"/>'/></a></td>
<% }else{ %>
<tr class="voc">
<c:set var="id">${voc.id}</c:set>
<tr><td><a href='<c:url value="${sub}/selectVoc.jsp?id=${id}"/>'>${voc.name}</a></td>
<% if(central.canWrite(pageContext, (String)pageContext.findAttribute("id"))){ %>
<td>
<c:if test="${voc.configurable}">
	<a href='<c:url value="newVocabulary.jsp?what=${id}"/>'><img title="Reconfigure" src='<c:url value="${sub}/images/actions/document-properties.png"/>'/></a>
</c:if>
</td><td>
 	<c:if test="${voc.fileSrc }">
		<a href='<c:url value="data.jsp?what=${id}"/>'><img title="Data" src='<c:url value="${sub}/images/actions/accessories-text-editor.png"/>'/></a>
	</c:if>
</td><td>
 	<a href='<c:url value="reload.jsp?what=${id}"/>'><img title="Reload" src='<c:url value="${sub}/images/actions/view-refresh.png"/>'/></a>
</td><td>
 	<a href='<c:url value="remove.jsp?what=${id}"/>'><img title="Remove" src='<c:url value="${sub}/images/actions/process-stop.png"/>'/></a>
</td>
<% } else {%>
<td colspan='4'></td>
<% } %>
<% }; %>
<td>	<a href="vocabularies.jsp?up=${vs.index }"><img title="Up" src='<c:url value="${sub}/images/actions/go-up.png"/>'/></a>
	<a href="vocabularies.jsp?down=${vs.index }"><img title="Down" src='<c:url value="${sub}/images/actions/go-down.png"/>'/></a>
	<a href="vocabularies.jsp?addTitle=${vs.index }">New Title</a>
</td>
</tr>
</c:forEach></table>

</body>
</html>
