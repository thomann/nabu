/*
 * Created on 01.04.2004
 */
package ch.unizh.ori.nabu.ui.http.taglib;

import java.io.StringReader;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.digester.Digester;

import ch.unizh.ori.nabu.core.Central;
import ch.unizh.ori.nabu.ui.http.HttpCentral;
import ch.unizh.ori.nabu.voc.Source;

/**
 * @author pht
 */
public class ReadSourceTag extends BodyTagSupport {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ReadSourceTag.class);
	
	private HttpCentral central;
	
	private String sourceXml;
	
	private URL base;
		
	public int doStartTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		try {
			if(base == null){
				base = new URL(central.getUploadVocLocation());
			}
			
			sourceXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+sourceXml;
			log.debug(sourceXml);
			Digester digester = new Digester();
			Central.configureSource(digester, "src");
			Source src = (Source) digester.parse(new StringReader(sourceXml));
			List lessons = src.readLections(base);
			pageContext.setAttribute("lessons", lessons);
		} catch (Exception e) {
			throw new JspException(e);
		}
		
		return EVAL_BODY_BUFFERED;
	}

	public int doEndTag() throws JspException {
//		try {
//			pageContext.getOut().write(getBodyContent().getString());
//		} catch (IOException ex) {
//			throw new JspException(ex);
//		}
		init();
		return EVAL_PAGE;
	}
	
	private void init() {
		central = null;
		sourceXml = null;
		base = null;
	}

	/**
	 * @param central
	 */
	public void setCentral(HttpCentral central) {
		this.central = central;
	}

	public String getSourceXml() {
		return sourceXml;
	}
	public void setSourceXml(String xml) {
		this.sourceXml = xml;
	}
	public URL getBase() {
		return base;
	}
	public void setBase(URL base) {
		this.base = base;
	}
}
