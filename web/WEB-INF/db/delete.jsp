<%@page contentType="text/html"%>
<%@taglib uri="/WEB-INF/tlds/Nabu.tld" prefix="nabu" %>
<%@ taglib uri="/WEB-INF/tlds/dbtags.tld" prefix="sql" %>
<%-- open a database connection --%>
<sql:connection id="conn" jndiName="java:/comp/env/jdbc/Nabu"/>
<%
  nabu.db.http.DBServlet.VocInfo vocInfo = (nabu.db.http.DBServlet.VocInfo)request.getAttribute("vocInfo");
  nabu.db.http.DBServlet.ColumnInfo[] columnInfo = vocInfo.getColumnInfos();
  int vid = -1;
  try{vid = Integer.parseInt(request.getParameter(vocInfo.getVidInfo().getName()));}catch(Exception ex){};
  if(vid >= 0){
%>
<sql:preparedStatement id="stmt" conn="conn">
 
  <sql:query>
    DELETE FROM <%=vocInfo.getTableName()%> where vid=?
  </sql:query>
  
  <sql:execute>
    <sql:setColumn position="1"><%=vid%></sql:setColumn>
  </sql:execute>
</sql:preparedStatement>
<% } else { %>
<jsp:forward page="show.jsp"/>
<% } %>
<html>
<head><title>Delete</title></head>
<body>

<nabu:header/>

Delete complete.
<hr>
<a href="show.jsp">Index</a> --
<a href="edit.jsp">New</a>

</body>
</html>
<sql:closeConnection conn="conn"/>
