<%@page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="nabu" uri="ch.unizh.ori.nabu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>New Vocabulary</title></head>
<body><%
	ch.unizh.ori.nabu.ui.http.HttpCentral c = (ch.unizh.ori.nabu.ui.http.HttpCentral) pageContext.findAttribute("central");
	Object props = c.loadProps(request.getParameter("what"));
	pageContext.setAttribute("props", props);
%>
<a href="<c:url value="vocabularies.jsp" />">Vocabularies</a>
<a href="<c:url value="${sub}/overview.jsp" />">Overview</a>
<a href="<c:url value="reload.jsp" />">Reload</a>

<h1>New Vocabulary</h1>

<form action="makeVoc.jsp" method="post" enctype="multipart/form-data">
<table border="0">
<tr><td class="label"></td><td class="field">Your Input:</td><td class="example">Example</td></tr>
<tr><td class="label">Name:</td><td class="field"><input type="text" name="name" value="${props.name}"></td><td class="example">Arabic</td></tr>
<tr><td class="label">Id:</td><td class="field"><input type="text" name="id" value="${props.id}"></td><td class="example">ar.foo (<a href="http://www.w3.org/WAI/ER/IG/ert/iso639.htm">ISO Codes</a>)</td></tr>
<tr><td class="label">Description:</td><td class="field"><textarea cols="50" rows="3" name="description">${props.description}</textarea></td><td class="example"></td></tr>

<tr><td class="label">Columns:</td><td class="field"><textarea cols="50" rows="5" name="columns">${props.columns}</textarea></td><td class="example"><code>id:name:script:del</code>
<div><b>Scripts:</b></div>
<table>
<tr>
<c:forEach items="${central.scripts}" var="s">
<td>${s.value.name}</td>
</c:forEach>
</tr><tr>
<c:forEach items="${central.scripts}" var="s">
<td><code>${s.value.id}</code></td>
</c:forEach>
</tr>
<tr>
<c:forEach items="${central.scripts}" var="s">
<td><code>${s.value.example}</code></td>
</c:forEach>
</tr>
</table>
</td></td></tr>
<tr><td class="label">Modes:</td><td class="field"><textarea cols="50" rows="5" name="modes">${props.modes}</textarea></td><td class="example"><code>short:name</code><br/>
e.g.: <code>en=?de</code> <br/> <code>en=?de:Englisch nach Deutsch</code> <br/> <code>en=?de=desc</code></td></tr>
<tr><td class="label"></td><td class="field"><input name="emptyline" id="emptyline" type="checkbox"<c:if test="props.emptyline"> checked</c:if>><label for="emptyline">Lections are divided by empty lines</label></td><td class="example"></td></tr>
<tr><td class="label">Vocabulary:</td><td class="field"><input type="file" name="voc"></td><td class="example"></td></tr>
<tr><td class="label">Encoding:</td><td class="field"><input type="text" name="enc" value="<c:out value="${props.enc}" default="UTF-8"/>"></td><td class="example">UTF-8</td></tr>
<tr><td class="label">Lesson-Format:</td><td class="field"><input type="text" name="lfmt" value="${props.lfmt}"></td><td class="example">Excercise,Lesson {0}</td></tr>
<tr><td class="label">Separator in the Tables:</td><td class="field"><input type="text" name="columnSep" value="<c:out value="${props.columnSep}" default=":"/>"></td><td class="example">:</td></tr>

</table>
<input type=SUBMIT><input type=RESET>
</form>

</body>
</html>
