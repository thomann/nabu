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

  String[] in = new String[columnInfo.length];
  for(int i=0; i<in.length; i++){
    in[i] = request.getParameter(columnInfo[i].getName());
  }
  if(vid >= 0){
%>
<sql:preparedStatement id="stmt" conn="conn">
 
  <sql:query>
    UPDATE <%=vocInfo.getTableName()%> SET <%=vocInfo.getColUpdateNames()%>
    where vid=?
  </sql:query>
  
  <sql:execute>
    <% stmt = (java.sql.PreparedStatement)pageContext.findAttribute("stmt");
       for(int i=0; i<in.length; i++){
        stmt.setString(i+1, in[i]);
       }
       stmt.setInt(in.length+1, vid);
    %>
  </sql:execute>
</sql:preparedStatement>
<% } else { %>
<sql:preparedStatement id="stmt" conn="conn">
 
  <sql:query>
    INSERT INTO <%=vocInfo.getTableName()%> SET <%=vocInfo.getColUpdateNames()%>
  </sql:query>
  
  <sql:execute>
    <% stmt = (java.sql.PreparedStatement)pageContext.findAttribute("stmt");
       for(int i=0; i<in.length; i++){
         stmt.setString(i+1, in[i]);
       } %>
  </sql:execute>
</sql:preparedStatement>
<% } %>
<sql:closeConnection conn="conn"/>
<%
  request.setAttribute("message", "Enter OK. Do You want to enter new");
  request.setAttribute("prevIn", in);
  String path = "edit.jsp?"+vocInfo.getVidInfo().getName()+"=-1";
%>
<jsp:forward page="<%=path%>"/>

