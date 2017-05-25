<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="nabu" uri="ch.unizh.ori.nabu" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:requestEncoding value="UTF-8"/>
<jsp:useBean id="markedQuestions" class="java.util.HashMap" scope="session"/>
<c:if test="${!empty param.markQuestion}">
	<c:set target="${markedQuestions}" property="${iter.question}" value="${iter.question}"/>
</c:if>

<nabu:questionForm next="statistics.jsp">
<html>
<head>
<title><fmt:message key="showQuestion.title"/></title>
<link rel=stylesheet type="text/css" href="nabustyle.css">
<script type="text/javascript">
<!--
	time = -1;
	
	function keyHandler(e){
		if(!e) e = window.event;
		var code;
		if(e.which){
			code = e.which;
		}else if(e.keyCode){
			code = e.keyCode;
		}
		if(code != 32){
			time = -1;
			return true;
		}
		var now = new Date().getTime();
		if(time == -1){
			time = now;
			return true;
		}
		var diff = now - time;
		if(diff > 500){
			time = now;
			return true;
		}else{
			var text = e.target;
			if(!text) text = e.srcElement;
			deleteTwoSpaces(text);
			text.form.submit();
			return false;
		}
	}
	document.onkeyup = keyHandler;
	
	function deleteTwoSpaces(input){
		  // Kennt der Browser das TextRange-Objekt?
		  if (document.selection) {
		    var range = document.selection.createRange();
		    range.moveStart("character",-2);
		    range.text = "";
		 }else if(input.setSelectionRange > '') {
		    var start = input.selectionEnd-2;
		    var end = input.selectionEnd;
		    input.value = input.value.substr(0, start) + input.value.substr(end);
		  }
	}
	
//-->
</script>
</head>
<body class="general">
<nabu:header/>
<table>
<tr><td valign="top">
<img heigth="43"width="73" src="images/nabulogo8xs.png">
</td><td valign="center">
<h1>&nbsp;</h1>
</td></td>
</table>

<c:set var="autocomplete" value="${(empty param.disableAutocomplete) ? 'ON' : 'OFF'}"/>
<form method=POST action='<c:url value="showQuestion.jsp"/>' name="ques" autoComplete="${autocomplete}"><%-- *********** action is self *********** --%>
	<nabu:showRenderer />
	<input type=SUBMIT name="ok" value='<fmt:message key="showQuestion.ok"/>'>
	<c:if test="${!r.showSolution}"> <input type=SUBMIT value='<fmt:message key="showQuestion.showSol"/>' name="fail"> </c:if>
	<br>
	<c:set var="po" value="${iter.problemsOnly}"/>
	<!-- this is a dummy value, so browsers like Safari won't get the page from the cache -->
	<input type=hidden name=dummy value="${param.dummy +1}"/>
	<c:if test="${param.maxProblems > 0}"><c:set target="${iter}" property="problemsOnly" value="${ iter.statistics.problems >= param.maxProblems }"/></c:if>
	<nabu:checkbox name="problemsOnly" value="${iter.problemsOnly}" /> <label for="problemsOnly"><fmt:message key="showQuestion.onlyRep"/></label>

 <c:if test="${hasSotm}">
 <input type=checkbox name="play" id="play" <c:if test="${!empty param.play}">checked</c:if>> <label for="play"><fmt:message key="showQuestion.sotm"/></label>
 </c:if>
	
	<c:if test="${advanced}"><br><fmt:message key="showQuestion.maxNumOfProblems"/>: <input type=TEXT name="maxProblems" value="${param.maxProblems}" size="4">
	<c:set var="questionString">${iter.question}</c:set><input type="checkbox" name="markQuestion" id="markQuestion"<c:if test="${!empty markedQuestions[questionString]}"> checked</c:if>> <label for="markQuestion"><fmt:message key="showQuestion.markItem"/></label>
	<nabu:checkbox name="disableAutocomplete" value="${!empty param.disableAutocomplete}" /> <label for="disableAutocomplete"><fmt:message key="showQuestion.disableAutocomplete"/></label>
	</c:if>
</form>

<c:if test="${!empty message}">
<hr>
<fmt:message key="showQuestion."/>Fehler: <font color='red'>${message}</font>
</c:if>

<nabu:stats>

<table width="50%" bgcolor="black" class="statusbar" line-heigth="30pt">
<tr class="statusbar" ; height="6pt" >
<td class="finished" width="${2+98*stats.completed/stats.total}%">&nbsp;</td><td  class="problem" width="${2+98*stats.problems/stats.total}%">&nbsp;</td><td  class="left">&nbsp;</td>
</tr>
</table>

<table width="50%" class="numbers">
<tr height="20pt">
<td class="numbers" align="center" width="${2+98*stats.completed/stats.total}%"> ${stats.completed}</td>
<td class="numbers" align="center" width="${2+98*stats.problems/stats.total}%"> ${stats.problems}</td>
<td class="numbers" align="right">${stats.total}</td>
</tr>
</table>
<br>
<c:if test="${hasProblems}"><fmt:message key="showQuestion.numOfCorr"/>: ${times}</c:if>
</nabu:stats>

<nabu:focus ques="ques"/><br>
<a href='<c:url value="${newVocUrl}"/>'><fmt:message key="showQuestion.newSameVoc"/> ${newVocName}</a>
| <a href='<c:url value="${sub}/overview.jsp"/>'><fmt:message key="overview"/></a>
<c:if test="${advanced}"> | <a href='<c:url value="admin/vocabularies.jsp"/>'><fmt:message key="admin"/></a> (<fmt:message key="needsLogin"/>)</c:if>

<br/>

</body>
</html>
</nabu:questionForm>
