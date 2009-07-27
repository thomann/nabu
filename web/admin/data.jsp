<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="java.io.File"%>
<%@ taglib uri="ch.unizh.ori.nabu" prefix="nabu" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@page import="java.net.URL"%>
<%@page import="ch.unizh.ori.nabu.voc.*"%>
<c:set var="voc" value="${central.vocs[param.what]}"/>
<tag:adminHeader title="Data of ${voc.name}"/>

<a href="data.txt?download=${param.what}">Download Text</a>

<form action="data.txt" method="POST" enctype="multipart/form-data">
Upload Text:
<input type="hidden" name="upload" value="${param.what}"/>
<input type="file" name="file"/>
<input type="submit" value="Upload"/>

</form>

Copy Text:<br/>
<form action="data.txt?upload=${param.what}" method="POST" enctype="multipart/form-data">
<textarea rows="30" cols="80" name="file"><% ch.unizh.ori.nabu.ui.http.HttpCentral c = (ch.unizh.ori.nabu.ui.http.HttpCentral) pageContext.findAttribute("central");
Vocabulary voc = (Vocabulary)pageContext.getAttribute("voc");
URL url = null;
if(voc.isFileSrc()){
	url = new URL(voc.getBase(), voc.getSrc().getSrc());
}
pageContext.setAttribute("theUrl", url);
%><c:import url="${theUrl}" charEncoding="UTF-8"></c:import>
</textarea><br/>
<input type="hidden" name="upload" value="${param.what}"/>
<input type="submit" value="Upload"/>
</form>

</body>
</html>
