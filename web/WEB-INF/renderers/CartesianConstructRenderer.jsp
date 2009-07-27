<%@page contentType="text/html" import="nabu.*, nabu.lang.*"
%><jsp:useBean id="r" type="nabu.morph.CartesianHttpRenderer$Construct" scope='session'/><%
%><table border=2>
<tr><td valign=top>?</td><td>
<table><tr><td>
<font size='18'>
<jsp:getProperty name='r' property='root' />,
</font>
</td></tr><tr><td>
<table border=1>
<tr>
<%
    for(int i=0; i<r.getCoordinateNames().length; i++){
        String n = r.getCoordinateName(i);
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
%>
</tr>
</table>
</td></tr></table>
</td></tr>
<tr><td valign=top>!</td><td><%
if(r.isShowSolution()){
%><b><jsp:getProperty name='r' property='answer' /></b><%
}else{
%><input type=TEXT name="ans" value="<jsp:getProperty name='r' property='answer' />" size='50'><%
}
%></td></tr>
</table>
