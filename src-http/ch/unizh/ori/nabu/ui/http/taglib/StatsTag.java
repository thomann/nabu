/*
 * Created on 01.04.2004
 */
package ch.unizh.ori.nabu.ui.http.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import ch.unizh.ori.nabu.core.QuestionIterator;

/**
 * @author pht
 */
public class StatsTag extends TagSupport {
	
	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) pageContext.getSession().getAttribute("iter");
		pageContext.setAttribute("stats", iter.getStatistics());
		Object q = iter.getQuestion();
		int times = iter.getTimesForProblem(q);
		boolean hasProblems = (times > 0);
		pageContext.setAttribute("hasProblems", new Boolean(hasProblems));
		if(hasProblems){
			pageContext.setAttribute("times", new Integer(times));
		}
		
		return EVAL_BODY_INCLUDE;
	}


}
