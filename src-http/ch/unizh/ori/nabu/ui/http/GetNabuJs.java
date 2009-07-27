/*
 * Created on 16.05.2006
 *
 */
package ch.unizh.ori.nabu.ui.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import ch.unizh.ori.nabu.voc.FieldStream;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.Vocabulary;

public class GetNabuJs  extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void init() throws ServletException {
		
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

//		PageContext page = JspFactory.getDefaultFactory().getPageContext(this,
//				request, response, "", true, response.getBufferSize(), false);
		
		String id = request.getParameter("id");
		HttpCentral central = (HttpCentral) request.getAttribute("central");
		if(!central.getUploadVocList().contains(id)){
			throw new ServletException("There's no such voc available");
		}
		
		Vocabulary voc = (Vocabulary) central.getVocs().get(id);
		String loc = central.getUploadVocLocation();
		
		StringBuffer vocs = new StringBuffer();
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(loc,id+".txt")), "UTF-8"));
		String line;
		boolean multi = false;
		int maxFrameLen = 0;
		while ((line = in.readLine()) != null) {
			if (multi && line.trim().length() == 0) {
				// D'ont know yet.
			} else if (line.trim().length() == 0) {
				// glue lessons if not multi
			} else {
				line = escapeJs(line);
				line = line.replace("\t", "', '");
				vocs.append("['").append(line).append("'],");
			}
		}
		if(vocs.length()>0)
			vocs.setLength(vocs.length()-1);
		request.setAttribute("vocString", vocs);
		request.setAttribute("vocName", voc.getName());

		StringBuffer modes = new StringBuffer();
		for (Iterator iterator = voc.getModes().values().iterator(); iterator.hasNext();) {
			Mode m = (Mode) iterator.next();
			modes.append("[").append(getModeString(m)).append("],");
		}
		modes.setLength(modes.length()-1);
		request.setAttribute("modesString", modes);
		
		getServletContext().getRequestDispatcher("/WEB-INF/renderers/nabujs.jsp").forward(request, response);
	}

	private static String escapeJs(String line) {
		line = line.replace("'", "\\'");
		return line;
	}
	
	public static String getModeString(Mode m){
		StringBuffer a = new StringBuffer();
		StringBuffer b = new StringBuffer();
		for (Iterator iterator = m.createModeFields().iterator(); iterator.hasNext();) {
			ModeField mf = (ModeField) iterator.next();
			StringBuffer c;
			c = (mf.isAsking())?b:a;
			if(c.length()>0)
				c.append(',');
			c.append(mf.getColumn().getColumn());
		}
		return "'" + escapeJs(m.getName()) +"', ["+ a+"], ["+b+"]";
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
