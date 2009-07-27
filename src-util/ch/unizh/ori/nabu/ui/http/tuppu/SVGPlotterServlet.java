/*
 * StringPlotServlet.java
 *
 * Created on 21. September 2002, 11:26
 */

package ch.unizh.ori.nabu.ui.http.tuppu;

import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ch.unizh.ori.nabu.ui.VocabularyXMLExporter;
import ch.unizh.ori.tuppu.Plottable;
import ch.unizh.ori.tuppu.Plotter;

/**
 * 
 * @author pht
 * @version
 */
public class SVGPlotterServlet extends HttpServlet {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(SVGPlotterServlet.class);

	private Plotter plotter;

	/**
	 * Initializes the servlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String className = config.getInitParameter("plotter");
		System.setProperty("ch.uzh.ori.tuppu.fontdir", config.getServletContext().getRealPath("/WEB-INF/fonts/"));
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
		response.setContentType("image/svg");
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

		
		try {
			Document document = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder().newDocument();
//        Document document = domImpl.createDocument(null, "svg", null);

			// Create an instance of the SVG Generator
			SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
			ctx.setEmbeddedFontsOn(true);
			SVGGraphics2D svgGenerator = new SVGGraphics2D(ctx, true);

			// Ask the test to render into the SVG Graphics2D implementation
			Plottable plottable = plotter.createPlottable(text, new HashMap());
			plottable.plot(svgGenerator);

			// Finally, stream out SVG to the standard output using UTF-8
			// character to byte encoding
        boolean useCSS = true; // we want to use CSS style attribute
//        Writer out = new OutputStreamWriter(new FileOutputStream("/Users/pht/Desktop/arabout.svg"), "UTF-8");
//        svgGenerator.stream(out, useCSS);
        svgGenerator.stream(new OutputStreamWriter(response.getOutputStream(), "UTF-8"), useCSS);
//			VocabularyXMLExporter.writeXmlFile(document, "/Users/pht/Desktop/arabout.svg");
//			Element ret = svgGenerator.getRoot();
//			Dimension size = plottable.getSize();
//			ret.setAttribute("width", size.getWidth()+"");
//			ret.setAttribute("height", size.getHeight()+"");
			
			
			// Now dump it out
//			Source source = new DOMSource(document);
//
//			// Prepare the output file
//			Result result = new StreamResult(response.getOutputStream());
//
//			// Write the DOM document to the file
//			Transformer xformer = TransformerFactory.newInstance().newTransformer();
//			xformer.transform(source, result);
		} catch (Exception e) {
			throw new ServletException(e);
		}

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