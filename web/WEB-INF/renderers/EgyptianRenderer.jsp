<%@page contentType="text/html" import="nabu.*, nabu.lang.*, nabu.voc.*"%><%
%><%@ taglib uri="/WEB-INF/tlds/Nabu.tld" prefix="nabu" %>
<jsp:useBean id="r" type="nabu.ui.http.DefaultRenderer" scope='session'/><%
    Text text = r.getQuestionText();
    String path = text.getImageURL();
    path = ((HttpServletRequest)pageContext.getRequest()).getContextPath() + path;
    path = ((HttpServletResponse)pageContext.getResponse()).encodeURL(path);
%>
<table>
<tr><td>?</td><td><img src="<%= path %>"></td></tr>
<tr><td></td><td><%= text.getTransliteration().getUnicodeString() %></td></tr>
<tr><td>!</td><td><%
if(r.isShowSolution()){
%><b><jsp:getProperty name='r' property='answerString' /></b><%
}else{
%><input type=TEXT name="ans" size='50' value="<jsp:getProperty name='r' property='answer' />"><%
}
%></td></tr>
</table>
