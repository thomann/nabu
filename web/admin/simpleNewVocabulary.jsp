<%@page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="nabu" uri="ch.unizh.ori.nabu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="input" uri="http://jakarta.apache.org/taglibs/input-1.0" %>
<html>
<head><title>Simple Vocabulary</title>
<link rel=stylesheet type="text/css" href="nabuadminstyle.css">
<script type="text/javascript">
<!--
// Der Name wird automatisch neu generiert.
scriptNames = [	<c:forEach items="${central.scripts}" var="i">
		<c:set var="key" value="${i.key }" scope="page"/>
			<c:choose>
				<c:when test="${!empty i.value.title}">"${i.value.title}"</c:when>
				<c:otherwise>"${i.value.name}"</c:otherwise>
			</c:choose>,
	</c:forEach> ];

function selectLang1(val, selectObj){
 // alert("test "+val);
  selectObj.form.name.value = scriptNames[selectObj.selectedIndex];
}
//-->
</script>
</head>
<body class="general">
<%
	ch.unizh.ori.nabu.ui.http.HttpCentral c = (ch.unizh.ori.nabu.ui.http.HttpCentral) pageContext.findAttribute("central");
	Object props = c.loadProps(request.getParameter("what"));
	pageContext.setAttribute("props", props);
%>
<a href="<c:url value="vocabularies.jsp" />">Vocabularies</a>
<a href="<c:url value="${sub}/overview.jsp" />">Overview</a>
<a href="<c:url value="reload.jsp" />">Reload</a>
<h1>Simple Vocabulary</h1>

<form action="makeVoc.jsp" method="POST" enctype="multipart/form-data" id="theform">
<table><tr><td>
Foreign language</td><td>Known language</td></tr><tr><td>
<input:select name="lang1" size="1" default="ar" attributesText='onchange="selectLang1(this.value,this);"'>
	<c:forEach items="${central.scripts}" var="i">
		<c:set var="key" value="${i.key }" scope="page"/>
		<input:option value="<%= (String)pageContext.getAttribute("key") %>">
			<c:choose>
				<c:when test="${!empty i.value.title}">${i.value.title}</c:when>
				<c:otherwise>${i.value.name}</c:otherwise>
			</c:choose>
		</input:option> 
	</c:forEach>
<!--	<option value="ar" selected>عربي</option> -->
<!--	<option value="zh" >中国</option> -->
<!--	<option value="de" >Deutsch</option> -->
<!--	<option value="en" >English</option> -->
<!--	<option value="fr" >Français</option> -->
</input:select>
</td><td>
<input:select name="lang2" size="1" default="de">
	<c:forEach items="${central.scripts}" var="i">
		<c:set var="key" value="${i.key }" scope="page"/>
		<input:option value="<%= (String)pageContext.getAttribute("key") %>">
			<c:choose>
				<c:when test="${!empty i.value.title}">${i.value.title}</c:when>
				<c:otherwise>${i.value.name}</c:otherwise>
			</c:choose>
		</input:option> 
	</c:forEach>
<!--	<option value="ar">عربي</option> -->
<!--	<option value="zh" >中国</option> -->
<!--	<option value="de" selected>Deutsch</option> -->
<!--	<option value="en" >English</option> -->
<!--	<option value="fr" >Français</option> -->
</input:select> 
</td></tr></table>
<br>Your textfile:<input type="file" name="voc">
<br>Name your vocabulary:<input type="text" name="name">
<br><hr>
<input type=HIDDEN name="simple" value="on">
<input type=HIDDEN name="showConfig" value="lucky">
<input type=SUBMIT value="I'm feeling happy"><input type=SUBMIT value="Submit and edit configuration" onclick="showConfig.value = 'on'; submit();">
</form>

</body>
</html>
