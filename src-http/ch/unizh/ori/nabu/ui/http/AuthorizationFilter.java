package ch.unizh.ori.nabu.ui.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationFilter implements Filter {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(AuthorizationFilter.class);
	
	FilterConfig filterConfig;
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		log.trace("AuthorizationFilter.doFilter()");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		HttpCentral central = (HttpCentral) request.getAttribute("central");
		
		// Implementing the WWW-Basic Authentication scheme
		doAuthorization(req, res, central);
		req = new HttpServletRequestWrapper(req);
		chain.doFilter(req, res);
	}

	protected boolean doAuthorization(HttpServletRequest req,
			HttpServletResponse res, HttpCentral central) throws IOException {
		String auth = req.getHeader("authorization");
		if(auth == null){
			res.setHeader("WWW-Authenticate", "Basic realm=\"Nabu "+central.getSubName()+"\"");
			res.sendError(401);
			return false;
		}
		auth = new String((new sun.misc.BASE64Decoder()).decodeBuffer(auth.substring("Basic ".length())));
		int i=auth.indexOf(":");
		String user = auth.substring(0,i);
		filterConfig.getServletContext().log("auth: "+user);
		
		// now we lookup and set the area, the user belongs to
		log.trace(req.getUserPrincipal());
		boolean isAdmin = (central.getAdmins().contains(user) || req.isUserInRole("nabu-admin"));
		boolean isTutor = central.getTutors().contains(user);
		log.debug(String.format("admin: %s, tutor %s", isAdmin, isTutor));
		if(!(isAdmin || isTutor)){
			res.sendError(401, "Not authorized for NABU "+central.getName()+"!");
			return false;
		}
		
		log.trace("ServletPath: "+req.getServletPath());
		
		req.getSession().setAttribute("user", user);
		req.getSession().setAttribute("isAdmin", isAdmin);
		req.getSession().setAttribute("isTutor", isTutor);
		return true;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	
}
