<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="ch.unizh.ori.nabu" prefix="nabu" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  ch.unizh.ori.nabu.ui.http.HttpCentral c = (ch.unizh.ori.nabu.ui.http.HttpCentral) pageContext.findAttribute("central");
  if(request.getParameter("what") != null){
	  c.reloadVoc(request.getParameter("what"));
  }else{
	  c.clear();
	  c.read();
  }
%><c:if test="${!empty param.what}"><c:set var="voc" value="${central.vocs[param.what]}"/></c:if>
<tags:adminHeader title="Reload ${voc.name}"/>

<a href='<c:url value="${sub}/overview.jsp"/>'>overview</a>

<c:if test="${!empty param.what}"><a href='<c:url value="${sub}/selectVoc.jsp?id=${param.what}"/>'>${voc.name}</a></c:if>


</body>
</html>
