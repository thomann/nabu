/*
 * Created on Sep 5, 2004
 *
 */
package ch.unizh.ori.nabu.ui.http;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import org.apache.fop.apps.Options;
import org.w3c.dom.Document;

import ch.unizh.ori.nabu.ui.VocabularyXMLExporter;

/**
 * @author pht
 * 
 */
public class FOPServlet extends HttpServlet {
	
	public void init() throws ServletException {
		
		try{
			// workaround for bug in Hyphenation reading
			// mechanism of Apache FOP
			String name = org.xml.sax.helpers.XMLReaderFactory.createXMLReader()
					.getClass().getName();
			System.setProperty("org.xml.sax.parser", name);
			new Options(new File(getServletContext().getRealPath("/WEB-INF/lib/fontmetrics/userconfig.xml")));
			VocabularyXMLExporter.loadHyphenation(getServletContext().getRealPath("WEB-INF/lib/hyphenation"));
			org.apache.fop.configuration.Configuration.put("fontBaseDir", getServletContext().getRealPath("/WEB-INF/lib/fontmetrics/"));
		}catch(Exception ex){
			throw new ServletException("Problem reading FOP-configurations", ex);
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		PageContext page = JspFactory.getDefaultFactory().getPageContext(this,
				request, response, "", true, response.getBufferSize(), false);

		Document xml;
		try {
			xml = (Document) page.getExpressionEvaluator().evaluate(
					getInitParameter("xml"), Document.class,
					page.getVariableResolver(), null);
		} catch (ELException ex1) {
			throw new ServletException(ex1);
		}
		try {
			VocabularyXMLExporter.writeXmlFile(xml, getServletContext().getRealPath("/WEB-INF/xslt/out.xml"));
		} catch (TransformerException ex1) {
			ex1.printStackTrace();
		}
		
		String xsl = getInitParameter("xsl");
		File foXml = new File(getServletContext().getRealPath("/WEB-INF/xslt/out.fo.xml"));
		VocabularyXMLExporter.xsl(xml, getServletContext().getRealPath(xsl), new StreamResult(foXml.toURI().getPath()));
		try {
			response.setContentType("application/pdf");
			VocabularyXMLExporter.convertXML2PDF(xml, getServletContext().getRealPath(xsl),
					response.getOutputStream());
		} catch (Exception ex) {
			response.setContentType("text/html");
			throw new ServletException(ex);
		}
		response.getOutputStream().close();
	}
}
