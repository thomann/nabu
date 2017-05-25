<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="java.io.File"%>
<%@taglib uri="ch.unizh.ori.nabu" prefix="nabu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${!empty param.what && param.doit}" var="test">
<%
  ch.unizh.ori.nabu.ui.http.HttpCentral c = (ch.unizh.ori.nabu.ui.http.HttpCentral) pageContext.findAttribute("central");
  
  String id = request.getParameter("what");
  String from = c.fixLocation(c.getUploadVocLocation(), id);
  String to = c.fixLocation(c.getUploadVocLocation()+java.io.File.separator+"old", id);
  if(!c.canWrite(pageContext, id)){
	  throw new JspException(String.format("User %s may not remove vocabulary %s", request.getRemoteUser(), id));
  }
  // System.out.println(from+" -> "+to);
  
  File todir = new File(c.getUploadVocLocation()+java.io.File.separator+"old");
  if(!todir.exists())
	  todir.mkdir();
  new java.io.File(from+".properties").renameTo(new java.io.File(to+".properties"));
  new java.io.File(from+".txt").renameTo(new java.io.File(to+".txt"));
  new java.io.File(from+".xml").renameTo(new java.io.File(to+".xml"));
  
  c.clear();
  c.read();
%>
</c:if>
<tag:adminHeader title="Remove ${param.what}"/>

<c:if test="${!test}">
Do you want to remove really?
<a href='<c:url value="remove.jsp?what=${param.what}&doit=true" />'>Yes</a>
<a href='<c:url value="vocabularies.jsp" />'>No</a>
</c:if>
<c:if test="${test}">
${param.what} removed!
</c:if>


</body>
</html>
