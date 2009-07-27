<%@page contentType="text/html;charset=UTF-8" import="nabu.lang.*"%>
<%@ taglib uri="/WEB-INF/tlds/dbtags.tld" prefix="sql" %>
<%@ taglib uri="/WEB-INF/tlds/Nabu.tld" prefix="nabu" %>
<%-- open a database connection --%>
<sql:connection id="conn" jndiName="java:/comp/env/jdbc/Nabu"/>
<%
  /*String table = "Accadian";
  String colA = "lexem", colB = "english";
  String colAEnc = nabu.lang.Conversion.ACCADIAN, langBEnc = null;*/
  nabu.db.http.DBServlet.VocInfo vocInfo = (nabu.db.http.DBServlet.VocInfo)request.getAttribute("vocInfo");
  nabu.db.http.DBServlet.ColumnInfo[] columnInfo = vocInfo.getColumnInfos();
  /*String colA = vocInfo.colAName, colB = vocInfo.colBName;
  String colAEnc = vocInfo.colAEnc, langBEnc = vocInfo.colBEnc;*/

  int pos = 0, len=100;
  try{pos = Integer.parseInt(request.getParameter("pos"));}catch(Exception ex){};
  try{len = Integer.parseInt(request.getParameter("len"));}catch(Exception ex){};
%>

<html>
<head>
<title>Show <%= vocInfo.getTableName() %></title>

<STYLE type='text/css'>
  .vid {background: lightgray;}
  .lang {background: #DDDDFF;}
  .action {color: red; font-size: 80%;}
</STYLE>

</head>
<body>

<nabu:header/>

<!--
contextPath: <%= request.getContextPath() %> <br>
pathInfo: <%= request.getPathInfo() %> <br>
pathTranslated: <%= request.getPathTranslated() %> <br>
QueryString: <%= request.getQueryString() %> <br>
getRequestURI: <%= request.getRequestURI() %> <br>
getServletPath: <%= request.getServletPath() %> <br>
<%        String path = request.getServletPath().substring("/db".length());
        path = path.substring(0,path.lastIndexOf('/')); %>
bla<%= path %>bla<br>
-->

<a href="show.jsp?pos=<%=Math.max(pos-len,0)%>&len=<%=len%>">prev</a> --
<a href="edit.jsp">new</a> --
<a href="show.jsp?pos=<%=Math.max(pos+len,0)%>&len=<%=len%>">next</a>

<%-- print the rows in an HTML table --%>
<table>
<sql:statement id="stmt" conn="conn">
 
  <sql:query>
    select <%=vocInfo.getVidInfo().getName()%>,<%=vocInfo.getColNames()%>
    from <%=vocInfo.getTableName()%>
    order by vid LIMIT <%=pos%>, <%=len%>
  </sql:query>
  
  <%-- loop through the rows of your query --%>
  <sql:resultSet id="rset">
    <tr>
      <td class="vid"><sql:getColumn position="1"/></td>
      <% for(int i=0; i<columnInfo.length; i++) { 
            nabu.Text t = columnInfo[i].getEnc().getText(rset, i+2);%>
      <%--<td class="lang"><%= Conversion.getColumn(rset, i+2, columnInfo[i].getEnc()) %></td>--%>
      <%--<td class="lang"><%=columnInfo[i].getEnc().getName()%></td>--%>
      <td class="lang"><nabu:text value="<%=columnInfo[i].getEnc().getText(rset, i+2)%>"/></td>
      <%--<td class="lang"><%=t.getUnicodeString()%>, <%=t.getImageURL()%></td>--%>
      <% } %>
      <td class="action"><a href='edit.jsp?vid=<sql:getColumn position="1"/>'>edit</a></td>
      <td class="action"><a href='delete.jsp?vid=<sql:getColumn position="1"/>'>delete</a></td>
    </tr>
  </sql:resultSet>
  
</sql:statement>
</table>

<a href="show.jsp?pos=<%=Math.max(pos-len,0)%>&len=<%=len%>">prev</a> --
<a href="edit.jsp">new</a> --
<a href="show.jsp?pos=<%=Math.max(pos+len,0)%>&len=<%=len%>">next</a>

</body>
</html>
<sql:closeConnection conn="conn"/>
