<%@page contentType="text/html"%>
<%@taglib uri="/WEB-INF/tlds/Nabu.tld" prefix="nabu" %>
<%@ taglib uri="/WEB-INF/tlds/dbtags.tld" prefix="sql" %>
<%-- open a database connection --%>
<sql:connection id="conn" jndiName="java:/comp/env/jdbc/Nabu"/><%
  nabu.db.http.DBServlet.VocInfo vocInfo = (nabu.db.http.DBServlet.VocInfo)request.getAttribute("vocInfo");
  nabu.db.http.DBServlet.ColumnInfo[] columnInfo = vocInfo.getColumnInfos();
  int vid = -1;
  try{vid = Integer.parseInt(request.getParameter(vocInfo.getVidInfo().getName()));}catch(Exception ex){};
  String[] strs = new String[columnInfo.length];
  Object prevIn = request.getAttribute("prevIn");
  if(vid >= 0){
%>
<sql:statement id="stmt" conn="conn">
 
  <sql:query>
    select <%=vocInfo.getColNames()%> from <%=vocInfo.getTableName()%>
    where vid=<%=vid%>
  </sql:query>
  
  <%-- loop through the rows of your query --%>
  <sql:resultSet id="rset"><%
    for(int i=0; i<strs.length; i++){
        strs[i] = rset.getString(i+1);
    }
  %></sql:resultSet>
</sql:statement>
<% //}else if(prevIn != null && prevIn instanceof String[]){
     //strs = (String[])prevIn;
   }else{
        for(int i=0; i<strs.length; i++)
            strs[i] = "";
        System.out.println("Hello World");
        if(prevIn != null && prevIn instanceof String[] && "lection".equals(columnInfo[columnInfo.length-1].getName())){
            strs[columnInfo.length-1] = ((String[])prevIn)[columnInfo.length-1];
        }
   } %>

<html>
<head><title>JSP Page</title></head>
<body>

<nabu:header/>

<% if(request.getAttribute("message")!=null){ %>
<h3><%=request.getAttribute("message")%></h3>
<% } %>
<form action="enter.jsp" method=GET name='edit_form'>
<table>
<% for(int i=0; i<strs.length; i++) { %>
<tr>
  <td class="label"><%=columnInfo[i].getLabel()%></td>
  <td class="field"><input type=TEXT name='<%=columnInfo[i].getName()%>' value='<%=strs[i]%>'></td>
</tr>
<% } %>
</table>
<% if(vid >= 1) { %><INPUT type=HIDDEN name='<%=vocInfo.getVidInfo().getName()%>' value='<%=vid%>'><% } %>
<INPUT type=SUBMIT name="ok" value='OK'> <INPUT type=RESET>
</form>

<a href="show.jsp">Index</a>

<%
    String focus = columnInfo[0].getName();
%><script language="JavaScript">
<!--
if (document.edit_form && document.edit_form.<%= focus %>) document.edit_form.<%= focus %>.focus();
//-->
</script>

</body>
</html>
<sql:closeConnection conn="conn"/>
