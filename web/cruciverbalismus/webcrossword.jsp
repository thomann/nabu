
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:requestEncoding value="UTF-8" />


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cruciverbalismus</title>

</head>
<body>
<jsp:useBean id="userInput" class="ch.unizh.ori.cruciverbalismus.ConfigBean" scope="session"/>
<jsp:setProperty name="userInput" property="size"/>
<jsp:setProperty name="userInput" property="crossings"/>
<jsp:setProperty name="userInput" property="blocks"/>
<jsp:setProperty name="userInput" property="unequity"/>
<jsp:setProperty name="userInput" property="iterations"/>
<c:set target="${userInput}" property="rtol" value="${'on' eq param.rtol}"/>
<c:set target="${userInput}" property="xcols" value="${'on' eq param.xcols}"/>
<jsp:setProperty name="userInput" property="title"/>
<jsp:setProperty name="userInput" property="lexicon"/>
<jsp:setProperty name="userInput" property="replacePatterns"/>
<jsp:setProperty name="userInput" property="deletePatterns"/>

<h1>
<c:choose>
<c:when test="${empty userInput.title}">
Cruciverbalismus
</c:when>
<c:otherwise>
${userInput.title}
</c:otherwise>
</c:choose>
</h1>
<c:out value="test"/>
<form action="webcrossword.jsp" method="POST">
Size
<c:choose>
<c:when test="${empty userInput.size}">
<input type="text" name="size" value="4" size="2"/>
</c:when>
<c:otherwise>
<input type="text" name="size" value='<c:out value="${userInput.size}"/>' size="2"/>
</c:otherwise>
</c:choose>

Crossings<input type="text" name="crossings" value="${userInput.crossings}" size="4"/>
Blocks<input type="text" name="blocks" value="0.5" size="4"/>
Unequity<input type="text" name="unequity" value="0.4" size="4"/>
Iterations<input type="text" name="iterations" value="1000" size="8"/>
R-to-L<input type="checkbox" name="rtol" <c:if test="${userInput.rtol }">checked</c:if>/>
Exchange Columns<input type="checkbox" name="xcols" <c:if test="${userInput.xcols }">checked</c:if>/>

title<input type="text" name="title" value="${userInput.title}"/>
<br/>
Lexicon<textarea rows="3" cols="50" name="lexicon">
${userInput.lexicon}
</textarea>
Replace<textarea rows="3" cols="20" name="replacePatterns">
${userInput.replacePatterns}
</textarea>
Delete<textarea rows="3" cols="10" name="deletePatterns">
${userInput.deletePatterns}
</textarea>
<input type="hidden" name="job" value="1"/>
<input type="submit" value="->"/>
</form>
<form action="riddle.jsp" method="POST">
<input type="hidden" name="crossword" value="${userInput.crosswordHTML}"/>
<input type="submit" value="-->?"/>
</form>
<form action="solution.jsp"  method="POST">
<input type="hidden" name="solution" value="${userInput.solutionHTML}"/>
<input type="submit" value="-->!" "/>
</form>

<hr/>
${userInput.newCrosswordHTML}
<hr/>
${userInput.solutionHTML}
</body>
</html>
