/*
 * StringPlotServlet.java
 *
 * Created on 21. September 2002, 11:26
 */

package ch.unizh.ori.nabu.ui.http.tuppu;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.unizh.ori.tuppu.Plotter;

/**
 * 
 * @author pht
 * @version
 */
public class PlotterServlet extends HttpServlet {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(PlotterServlet.class);

	private Plotter plotter;

	/**
	 * Initializes the servlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String className = config.getInitParameter("plotter");
		try {
			plotter = (Plotter) Class.forName(className).newInstance();
		} catch (Exception ex) {
			throw new ServletException("Problem with Plotter " + className, ex);
		}
		for (Enumeration enumeration = config.getInitParameterNames(); enumeration
				.hasMoreElements();) {
			String param = (String) enumeration.nextElement();
			if ("plotter".equals(param) || "overwriteable".equals(param))
				continue;
			String value = config.getInitParameter(param);
			if(param.equals("plotterId")){
				plotter.setId(value);
			}else if (param.startsWith("realPath:")) {
				plotter.setInitParam(param.substring("realPath:".length()),
						getServletContext().getRealPath(value));
			} else if (param.startsWith("realPathUrl:")) {
				log.debug(param.substring("realPathUrl:".length()) +
						"file:"+getServletContext().getRealPath(value));
				plotter.setInitParam(param.substring("realPathUrl:".length()),
						"file:"+getServletContext().getRealPath(value));
			} else {
				plotter.setInitParam(param, value);
			}
		}
		plotter.init();
	}

	/**
	 * Destroys the servlet.
	 */
	public void destroy() {
		plotter = null;
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			java.io.IOException {
		request.setCharacterEncoding("UTF-8");
		boolean png = request.getHeader("Accept").indexOf("image/png") >= 0;
		response.setContentType((png) ? "image/png" : "image/jpeg");
		String text = request.getParameter("text");

		Map params = Collections.EMPTY_MAP;
		String v = getInitParameter("overwriteable");
		if (v != null) {
			Enumeration enumeration = ("*".equals(v)) ? request
					.getParameterNames() : new StringTokenizer((String) v, ",");
			params = new HashMap();
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement();
				key = key.trim();
				if (request.getParameter(key) != null)
					params.put(key, request.getParameter(key));
			}
		}
		BufferedImage img;
		try {
			img = plotter.plotToImage(text, params);
		} catch (Exception ex) {
			throw new ServletException(ex);
		}

		ImageIO.write(img, (png) ? "png" : "jpeg", response.getOutputStream());

	}

	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			java.io.IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			java.io.IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Plots a Plottable created from the plotter " + plotter;
	}

}