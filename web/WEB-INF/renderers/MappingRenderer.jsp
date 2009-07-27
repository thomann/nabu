<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="ch.unizh.ori.nabu" prefix="nabu"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<nabu:renderer>
	<table>
		<c:forEach items="${r.modeFields}" var="m">
			<tr>
				<td valign="top" align="right"><c:out value="${m.label}" />&nbsp;</td>
				<c:choose>
					<c:when test="${m.image}">
						<td class="questionbox">
							<nabu:image modeField="${m}" var="url">
								<img src="<c:url value="${url}"/>" />
							</nabu:image>
						</td>
					</c:when>
					<c:when test="${!m.asking}">
						<td class="questionbox">
							${r.presentedQuestion[m.key]}
							<nabu:sound modeField="${m}" var="url">
								<embed src="<c:url value="${url}"/>" width="140" height="30"/>
								<span style="font-size: 12pt;">
									<a href="<c:url value="${url}"/>">play</a>
								</span>
							</nabu:sound>
						</td>
					</c:when>
					<c:when test="${r.showSolution}">
						<td class="solutionbox"><c:out value="${r.presentedQuestion[m.key]}" /></td>
					</c:when>
					<c:otherwise>
						<td class="answerbox">
							<input class="inputfield" type="TEXT"
								name="<c:out value="q_${m.key}"/>"
								value="<c:out value="${r.userAnswer[m.key]}"/>" size='30'>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<c:if test="${!m.image && m.asking && r.showSolution && !empty r.userAnswer}">
				<tr>
					<td>Ihre Antwort war:</td>
					<td class="wrongbox"><c:out value="${r.userAnswer[m.key]}" /></td>
				</tr>
			</c:if>
		</c:forEach>
	</table>
</nabu:renderer>

