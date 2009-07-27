/*
 * Created on 01.04.2004
 */
package ch.unizh.ori.nabu.ui.http.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import ch.unizh.ori.nabu.core.DefaultQuestionIterator;
import ch.unizh.ori.nabu.ui.http.HttpMappingRenderer;
import ch.unizh.ori.nabu.ui.http.HttpRenderer;
import ch.unizh.ori.nabu.voc.FieldStream;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.Vocabulary;

/**
 * @author pht
 */
public class SelectVocTag extends TagSupport {
	
	private Vocabulary voc;
	private String show;
	
	public int doStartTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		ServletResponse response = pageContext.getResponse();
		HttpSession session = pageContext.getSession();
		
		String mode = request.getParameter("mode");
		String fillVariables = request.getParameter("fillVariables");
		if(mode != null && !"on".equals(fillVariables)){
			
			List lections = new ArrayList();
			boolean all = isOn(request, "l-all");
			if(voc.getLections() != null){
				for (Iterator iterator = voc.getLections().iterator();
					iterator.hasNext();
					) {
					FieldStream fs = (FieldStream) iterator.next();
					
					if(all || isOn(request, "l."+fs.getId())){
						lections.add(fs);
					}
				}
			}
			
			Mode m = (Mode) voc.getModes().get(mode);
			HttpRenderer r = new HttpMappingRenderer(m);

			DefaultQuestionIterator iter = voc.createIter(lections, r, m.getFilter(), m);
			iter.init();
			session.setAttribute("iter", iter);
			
			// this is for QuestionFormTag.doStartTag()
			session.setAttribute("first", "yes");

	        session.setAttribute("newVocUrl", getOriginalUrl((HttpServletRequest) request, (String)request.getAttribute("sub"))+"&fillVariables=on");
	        session.setAttribute("newVocName", voc.getName());
	        
	        
	        int interval = 120;
			try {
				interval = Integer.parseInt(pageContext.getServletContext().getInitParameter("invocsession.maxinactivetimeinterval"));
			} catch (NumberFormatException e1) {
				pageContext.getServletContext().log("Interval not set", e1);
			}
			session.setMaxInactiveInterval(interval);
			
			iter.next();
			
			try {
				pageContext.getServletContext().getRequestDispatcher(show).forward(request, response);
				return SKIP_PAGE;
			} catch (ServletException e) {
				throw new JspException(e);
			} catch (IOException e) {
				throw new JspException(e);
			}
		}else {
			return EVAL_BODY_INCLUDE;
		}
		
	}
	
	public static String getOriginalUrl(HttpServletRequest request, String sub) {
		String serverName = request.getServerName(); // hostname.com
		int serverPort = request.getServerPort(); // 80
		String contextPath = request.getContextPath(); // /mywebapp
		if(sub == null || sub.length() == 0)
			sub = "";
		String servletPath = request.getServletPath(); // /servlet/MyServlet
		if(servletPath == null)
			servletPath = "";
		String pathInfo = request.getPathInfo(); // /a/b;c=123
		if(pathInfo == null)
			pathInfo = "";
		String queryString = request.getQueryString(); // d=789
		if(queryString == null)
			queryString = "";
		return sub+servletPath+pathInfo+"?"+queryString;
	}

	public static boolean isOn(ServletRequest request, String param){
		String s = request.getParameter(param);
		return s!=null;
	}


	/**
	 * @return
	 */
	public String getVoc() {
		return voc.getId();
	}

	/**
	 * @param vocabulary
	 */
	public void setVoc(String vocabulary) {
		voc = (Vocabulary)pageContext.findAttribute(vocabulary);
	}

	/**
	 * @return
	 */
	public String getShow() {
		return show;
	}

	/**
	 * @param string
	 */
	public void setShow(String string) {
		show = string;
	}

}
