/*
 * Created on 01.04.2004
 */
package ch.unizh.ori.nabu.ui.http.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import ch.unizh.ori.nabu.core.QuestionIterator;

/**
 * @author pht
 * @deprecated
 */
public class ShowQuestionTag extends TagSupport {
	
	public static final String SHOW_QUESTION_KEY = "showQuestion.jsp.path";

	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) pageContext.getSession().getAttribute("iter");
		pageContext.setAttribute("r", iter.getRenderer());
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) pageContext.getSession().getAttribute("iter");
		if(iter.getRenderer().isShowSolution()){
			iter.next();
		}
		return EVAL_PAGE;
	}



}
