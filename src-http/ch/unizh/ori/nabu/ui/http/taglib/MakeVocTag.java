/*
 * Created on 01.04.2004
 */
package ch.unizh.ori.nabu.ui.http.taglib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.xml.sax.InputSource;

import ch.unizh.ori.common.text.Script;
import ch.unizh.ori.nabu.ui.http.HttpCentral;
import ch.unizh.ori.nabu.voc.StringSource;
import ch.unizh.ori.nabu.voc.Vocabulary;

/**
 * @author pht
 */
public class MakeVocTag extends BodyTagSupport {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(MakeVocTag.class);
	
	private HttpCentral central;
	
	private String location = null;
	
	private String filename = null; 
	
	private Properties vars;

	private String separator;
	
	private boolean dontWrite = false;
	
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		DiskFileUpload upload = new DiskFileUpload();
		try {

			FileItem voc = null;
			String vocName = null;
			vars = new Properties();
			
			if(request.getContentType().contains("multipart")){
				List items = upload.parseRequest((HttpServletRequest) request);
				
				for (Iterator iter = items.iterator(); iter.hasNext();) {
					FileItem fi = (FileItem) iter.next();
					if(fi.isFormField()){
						vars.put(fi.getFieldName(), fi.getString("UTF-8"));
					}else{
						voc = fi;
						vocName = voc.getName();
					}
				}
			}else{
				for (Iterator iter = request.getParameterMap().keySet().iterator(); iter.hasNext();) {
					String par = (String) iter.next();
					vars.put(par, request.getParameter(par));
				}
			}
			boolean vocInVars = vars.getProperty("voc")!=null;
			if(voc == null && vocInVars){
				vocName = vars.getProperty("vocName");
			}
			// Check if user is allowed:
			String user = (String) pageContext.findAttribute("user");
			if(!central.canWrite(pageContext, vars.getProperty("id"))){
				throw new JspException(String.format("User %s not allowed to access vocabulary with id %s", user, vars.getProperty("id")));
			}
			if(user != null)
				vars.put("change.user", user);
			vars.put("change.date", new Date().toString());
			vars.put("change.ip", request.getRemoteHost());
			
			if("on".equalsIgnoreCase(vars.getProperty("simple"))){
				String lang1 = vars.getProperty("lang1");
				String lang2 = vars.getProperty("lang2");
				vars.remove("lang1");
				vars.remove("lang2");
				
				String namePart = "";
				if(vocName != null){
					String s = vocName;
					int len = Math.min(2, s.length());
					s = s.substring(0,len);
					for(int i=0; i<len; i++){
						if(!Character.isLetter(s.charAt(i))){
							len = i;
						}
					}
					if(len > 0)
						namePart = "_" + s.substring(0,len);
				}
				String id;
				String idRoot = id = lang1 + namePart;
				int i=1;
				while(central.vocs.containsKey(id)){
					id = idRoot + "_" + (i++);
				}
				vars.setProperty("id", id);
				
				Script s1 = central.getScript(lang1);
				Script s2 = central.getScript(lang2);
				if(vars.getProperty("name") == null)
					vars.setProperty("name", s1.getName());
				vars.setProperty("description", "");
				
//				String[][] cols = {
//						{lang1,s1.getName(),s1.getId()},
//						{lang2,s2.getName(),s2.getId()},
//				};
				String[] colLines = {
						lang1 + separator + s1.getName() + separator + s1.getId(),
						lang2 + separator + s2.getName() + separator + s2.getId()
				};
				String[] colIds = { lang1, lang2 };
				BufferedReader in = null;
				if(vocInVars){
					in = new BufferedReader(new StringReader(vars.getProperty("voc")));
				}else{
					in = new BufferedReader(new InputStreamReader(voc.getInputStream(),"UTF-8"));
				}
				String line = in.readLine();
				if(line != null && line.length()>0){
					if(line.charAt(0)=='\uFEFF'){
						line = line.substring(1);
					}
					if(line.startsWith("##")){
						log.debug(line);
						line = line.substring(2);
						line = line.trim();
						colLines = line.split("\t");
						colIds = new String[colLines.length];
						for(int j=0; j<colLines.length; j++){
							colLines[j] = colLines[j];
							int k = colLines[j].indexOf(':');
							colIds[j] = (k<0)?colLines[j]:colLines[j].substring(0,k);
						}
					}
				}
				in.close();
				
				StringBuffer columns = new StringBuffer();
				for (int j = 0; j < colLines.length; j++) {
					columns.append(colLines[j]).append('\n');
				}
				vars.setProperty("columns", columns.substring(0, columns.length()-1));
				
				StringBuffer modes = new StringBuffer();
				for(int j=colLines.length-1;j>0; j--){
					for(int k=0; k<j;k++)
						modes.append(colIds[k]).append("=");
					modes.append('?').append(colIds[j]).append('\n');
				}
				modes.append('?').append(colIds[0]);
				for(int k=1; k<colIds.length;k++)
					modes.append("=").append(colIds[k]);
				vars.setProperty("modes", modes.toString());
				
				vars.setProperty("emptyline", "on");
				vars.setProperty("enc", "UTF-8");
				vars.setProperty("lfmt", "");
				vars.setProperty("columnSep", separator);
				pageContext.setAttribute("showConfig", vars.getProperty("showConfig"));
			}

			pageContext.setAttribute("vars", vars);
			pageContext.setAttribute("columns", readTable(vars.getProperty("columns")));
			pageContext.setAttribute("modes", readTable(vars.getProperty("modes")));
			
			String id = vars.getProperty("id");
			filename = HttpCentral.fixLocation(getLocation(), id);
			File vocFile = new File(filename+".txt");
			pageContext.setAttribute("vocUrl", vocFile.toURL());

			if(voc != null && voc.getSize()>0 && !isDontWrite()){
				voc.write(vocFile);
				voc = null;
			}

			pageContext.setAttribute("name", vars.getProperty("name"));
			pageContext.setAttribute("id", vars.getProperty("id"));
		} catch (Exception e) {
			throw new JspException(e);
		}
		
		return EVAL_BODY_BUFFERED;
	}

	private List readTable(String string) throws IOException {
		if(string==null || string.length()==0){
			return Collections.EMPTY_LIST;
		}
		List ret = new ArrayList();
		BufferedReader in = new BufferedReader(new StringReader(string));
		for(String l = in.readLine(); l != null; l = in.readLine()){
			if(l.length()==0)
				continue;
			String[] arr = l.split(separator);
			ret.add(arr);
		}
		return ret;
	}

	public int doEndTag() throws JspException {
		String vocXml = getBodyContent().getString();
		
			
		if(!isDontWrite()){
			try {
				writeOtherVoc(filename, vocXml, vars);
			} catch (IOException e) {
				throw new JspException(e);
			}
		}else{
			Vocabulary v;
			try {
				v = (Vocabulary) central.parseObject(new InputSource(new StringReader(vocXml)));
				v.setCentral(central);
				v.setSrc(new StringSource(vars.getProperty("voc")));
			} catch (Exception e) {
				throw new JspException(e);
			}
			pageContext.getSession().setAttribute("myVoc", v);
		}
		
//		try {
//			pageContext.getOut().println("<textarea cols=\"200\" rows='20'>");
//			pageContext.getOut().write(vocXml);
////			pageContext.getOut().println(filename);
////			pageContext.getOut().println(vars);
//			pageContext.getOut().println("</textarea>");
//		} catch (Exception e) {
//			 throw new JspException(e);
//		}
		
		pageContext.removeAttribute("vars");
		pageContext.removeAttribute("columns");
		pageContext.removeAttribute("modes");
		// we don't remove attribute name and id
		
		if(central != null && !dontWrite){
//			central.clear();
//			central.readUploadVocs();
			central.reloadVoc(vars.getProperty("id"));
		}
		
		filename = null;
		vars = null;
		central = null;
//		centralEL = null;
		location = null;
//		separatorEL = "\t";
		separator = null;
		dontWrite = false;
		return EVAL_PAGE;
	}

	private void writeOtherVoc(String file, String vocXml, Properties vars) throws IOException {
		PrintWriter out;
		log.debug("UploadVoc: "+file);
		
		out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file+".xml"), "UTF-8"));
		out.write(vocXml);
		out.close();
		
		vars.store(new FileOutputStream(file+".properties"), "the variables for this voc");
	}


	/**
	 * @return
	 */
	public String getLocation() {
		if(location == null && central != null){
			return central.getUploadVocLocation();
		}
		return location;
	}

	/**
	 * @param string
	 */
	public void setLocation(String string) {
		location = string;
	}

	/**
	 * @param string
	 */
	public void setCentral(HttpCentral central) {
		this.central = central;
	}

	/**
	 * @return
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * @param string
	 */
	public void setSeparator(String string) {
		this.separator = string;
	}

	public boolean isDontWrite() {
		return dontWrite;
	}

	public void setDontWrite(boolean dontWrite) {
		this.dontWrite = dontWrite;
	}

}
