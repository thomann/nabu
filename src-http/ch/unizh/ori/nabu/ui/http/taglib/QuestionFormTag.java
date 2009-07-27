/*
 * Created on 01.04.2004
 */
package ch.unizh.ori.nabu.ui.http.taglib;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.ui.http.HttpRenderer;

/**
 * @author pht
 */
public class QuestionFormTag extends TagSupport {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(QuestionFormTag.class);
	
	private String next;
	
	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) pageContext.getSession().getAttribute("iter");
		if(iter == null){
			String message = "Sie haben keine laufende Sitzung (mehr?) :-(<br/>W&auml;hlen Sie doch eine neue aus.";
//			pageContext.getRequest().getParameterMap().put("message", message);
			try {
				pageContext.forward("overview.jsp?message="+URLEncoder.encode(message, "UTF-8"));
				return SKIP_BODY;
			} catch (ServletException ex) {
				throw new JspException("Problem forwarding to overview.jsp", ex);
			} catch (IOException ex) {
				throw new JspException("Problem forwarding to overview.jsp", ex);
			}
		}
		iter.setProblemsOnly(pageContext.getRequest().getParameter("problemsOnly") != null);
		if(iter != null && iter.getRenderer() != null && iter.getRenderer().isShowSolution()){
			iter.next();
			pageContext.getSession().setAttribute("first", "on");
		}
        
		
		if(iter==null){
			return goNext();
		}
		
		if(pageContext.getSession().getAttribute("first") == null){
			evaluate(iter);
		}else{
			pageContext.getSession().setAttribute("first", null);
		}

		if(iter.getQuestion() == null){
			if(iter.isProblemsOnly()){
				iter.setProblemsOnly(false);
				iter.next();
			}
			if(iter.getQuestion() == null){
//				return goNext();
				try {
					HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
					HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
					String url = getNext();
				    if (url.startsWith("/"))
						url = request.getContextPath() + url;
					response.sendRedirect(response.encodeRedirectURL(url));
					log.info(response.encodeRedirectURL(next));
					log.info(response.encodeURL(next));
				} catch (IOException ex) {
					throw new JspException(ex);
				}
				return SKIP_PAGE;
			}
		}

		HttpRenderer r = (HttpRenderer)iter.getRenderer();
		pageContext.setAttribute("r", r);
		
		return EVAL_BODY_INCLUDE;
	}


	private int goNext() throws JspException {
		try {
			RequestDispatcher rd = pageContext.getServletContext().getRequestDispatcher(next);
			rd.forward(pageContext.getRequest(), pageContext.getResponse());
			return SKIP_PAGE;
		} catch (ServletException e) {
			throw new JspException(e);
		} catch (IOException e) {
			throw new JspException(e);
		}
	}


	private void evaluate(QuestionIterator iter) {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpRenderer r = (HttpRenderer)iter.getRenderer();
		boolean showSolution = request.getParameter("fail")!= null;
		if(r != null){
			r.processRequest(request, showSolution);
		}
		iter.doEvaluate();
	}


	public int doEndTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) pageContext.getSession().getAttribute("iter");
//		if(iter != null && iter.getRenderer() != null && iter.getRenderer().isShowSolution()){
//			iter.next();
//			pageContext.getSession().setAttribute("first", "on");
//		}
		return EVAL_PAGE;
	}

	/**
	 * @return
	 */
	public String getNext() {
		return next;
	}

	/**
	 * @param string
	 */
	public void setNext(String string) {
		next = string;
	}

}
