<%@page contentType="text/html" import="nabu.*, nabu.lang.*"
%><jsp:useBean id="r" type="nabu.morph.CartesianHttpRenderer$Analyse" scope='session'/><%
%><table>
<tr><td valign=top>?</td><td><FONT size='18'>
<jsp:getProperty name='r' property='form' />
</FONT></td></tr>
<tr><td valign=top>!</td><td><%
if(r.isShowSolution()){
%><%-- <jsp:getProperty name='r' property='ansRoot' />, 
<table border=1>
<tr>
<%
    for(int i=0; i<r.getCoordinates().length; i++){
        %><td> <b><%= r.getCoordinateName(i) %> </b></td><%
    }
%>
</tr>
<tr>
<%
    for(int i=0; i<r.getCoordinates().length; i++){
        %><td> <font color='green'><%= r.getCoordinate(i) %></font> </td><%
    }
%>
</tr>
</table>
<% --%><%
%><b><jsp:getProperty name='r' property='ansRoot' /></b>
<table><tr><%
    for(int i=0; i<r.getCoordinateNames().length; i++){
        String n = r.getCoordinateName(i);
        String oa = r.getAnsCoord(n);
        %><td valign='top'><table><%
        %><tr><td><b><%= r.getCoordinateLabel(i) %></b></td></tr>
<%      for(int j=0; j<r.getSpace()[i].length; j++){
            if( r.getSpace()[i][j].equals(r.getCoordinate(i)) ){
                %><tr><td> <b><font color='green'><%= r.getValueLabel(i,j) %></font></b> </td></tr><%
            }else{
                %><tr><td> <%= r.getValueLabel(i,j) %> </td></tr><%
            }
        }
        %></table></td><%
    }
%></tr></table><%
}else{
%><input type=TEXT name="ansRoot" value="<%= r.getAnsRoot() %>" size='50'>
 <table><tr>
<%
    for(int i=0; i<r.getCoordinateNames().length; i++){
        String n = r.getCoordinateName(i);
        String oa = r.getAnsCoord(n);
System.out.println(n+": "+oa);
        %>    <td valign='top'><table><tr><td><b><%= r.getCoordinateLabel(i) %></b></td>
<%
        for(int j=0; j<r.getSpace()[i].length; j++){
            String v = r.getSpace()[i][j];
            String checked = ((oa==null && j==0) || v.equals(oa))?" checked":"";
            %>      <tr><td> <input type=RADIO name='ans_<%= n %>' value='<%= v %>'<%= checked %> > <%= 
            r.getValueLabel(i,j) %> </td></tr>
<%
        }
        %>    </table></td>
<%
    }
%></tr></table><%
}
%></td></tr>
</table>
<%-- if(r.isFail()){ %><INPUT type=HIDDEN name='showSolution' value='on'><% } --%>
