/*
 * Created on 01.04.2004
 */
package ch.unizh.ori.nabu.ui.http.taglib;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.ui.http.HttpRenderer;

/**
 * @author pht
 */
public class ShowRendererTag extends TagSupport {
	
	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) pageContext.getSession().getAttribute("iter");
		HttpRenderer r = (HttpRenderer)iter.getRenderer();
		
		ServletRequest request = pageContext.getRequest();
		ServletResponse response = pageContext.getResponse();
		if(r==null){
			pageContext.getServletContext().log("ShowRendererTag: Renderer is null");
			return SKIP_BODY;
		}
		String jspPath = r.getJspPath();
		try {
			RequestDispatcher requestDispatcher = pageContext.getServletContext().getRequestDispatcher(jspPath);
//			requestDispatcher.forward(request, response);
			pageContext.getOut().flush();
			requestDispatcher.include(request, response);
		} catch (ServletException e) {
			throw new JspException(e);
		} catch (IOException e) {
			throw new JspException(e);
		}
		
		return SKIP_BODY;
	}


}
