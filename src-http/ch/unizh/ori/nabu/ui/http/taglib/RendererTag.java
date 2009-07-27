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
public class RendererTag extends TagSupport {
	
	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) pageContext.getSession().getAttribute("iter");
		pageContext.setAttribute("r", iter.getRenderer());
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}



}
