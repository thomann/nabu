<%@page contentType="text/html" import="nabu.*, nabu.lang.*"
%><jsp:useBean id="r" type="nabu.ui.http.DefaultRenderer" scope='session'/><%
%><%@ taglib uri="/WEB-INF/tlds/Nabu.tld" prefix="nabu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<table>
  <tr>
    <td>?</td>
        <td style="font-size: 48pt;">
            <nabu:text value="<%=r.getQuestionText()%>"/>
        </td>
  </tr>
  <tr>
    <td>!</td>
    <td colspan='2'><%
if(r.isShowSolution()){
    %><b><jsp:getProperty name='r' property='answerString' /></b><%
}else{
    %><input type=TEXT name="ans" value="<c:out value="${r.answer}"/>" size='50'><%
}
%></td>
  </tr><%
String answer = (String)r.getAnswer();
if(r.isShowSolution() && answer != null && answer.length() >= 1){
    %><tr>
    <td></td>
    <td colspan='2'><jsp:getProperty name='r' property='answer' /> war Ihre Antwort.</td>
  </tr><%
}
%>
</table>
