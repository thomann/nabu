/*
 * Created on 16.05.2006
 *
 */
package ch.unizh.ori.nabu.ui.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.unizh.ori.nabu.voc.Vocabulary;

public class GetJar  extends HttpServlet {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(GetJar.class);
	
	public void init() throws ServletException {
		
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

//		PageContext page = JspFactory.getDefaultFactory().getPageContext(this,
//				request, response, "", true, response.getBufferSize(), false);
		
		Map files = new HashMap();
		String srcFilename;
		String id = request.getParameter("id");
		HttpCentral central = (HttpCentral) request.getAttribute("central");
		if(!central.getUploadVocList().contains(id)){
			throw new ServletException("There's no such voc available");
		}
		
		Vocabulary voc = (Vocabulary) central.getVocs().get(id);
		String loc = central.getUploadVocLocation();
		String midletName = null;
		if ("midlet".equals(request.getParameter("type"))) {
			srcFilename = "/WEB-INF/Nabuttu-template.jar";
			try {
				files.putAll(new NabuttuCreator().gatherData(id, new File(loc,id+".txt"), voc));
				midletName = "Nabuttu "+voc.getName();
			} catch (Exception ex) {
				throw new ServletException("Problem producing MIDlet", ex);
			}
		}else if ("midletOld".equals(request.getParameter("type"))) {
				srcFilename = "/WEB-INF/NabuttuOld-template.jar";
				try {
					files.putAll(new NabuttuCreator().gatherData(id, new File(loc,id+".txt"), voc));
					midletName = "Nabuttu "+voc.getName();
				} catch (Exception ex) {
					throw new ServletException("Problem producing MIDlet", ex);
				}
		} else if("plain".equals(request.getParameter("type"))) {
			srcFilename = "/WEB-INF/Nabu-plain.jar";
			for(String file : voc.getFilenames()){
				files.put(file, new File(loc, file));
			}
			log.debug(files);
//			files.put(""+id+".xml", new File(loc,id+".xml"));
//			files.put(""+id+".txt", new File(loc,id+".txt"));
//			files.put(""+id+".properties", new File(loc,id+".properties"));
		} else {
			srcFilename = "/WEB-INF/Nabu-template.jar";
			files.put("etc/voc.xml", new File(loc,id+".xml"));
			files.put("etc/"+id+".txt", new File(loc,id+".txt"));
		}
		
		srcFilename = getServletContext().getRealPath(srcFilename);
		BufferedInputStream src = new BufferedInputStream(
							new FileInputStream(srcFilename));

		response.setContentType("application/java-archive");
		copyJar(src, files, response.getOutputStream(), midletName, true);
	}

	private transient byte[] buf = new byte[10240]; 

	public void copyJar(InputStream src, Map files, OutputStream dest, String midletName, boolean excludeJava) throws IOException, FileNotFoundException {
		JarInputStream in = null;
		JarOutputStream out = null;
		try {
			in = new JarInputStream(src);
			Manifest manifest = in.getManifest();
			if(midletName != null){
				manifest.getMainAttributes().putValue("MIDlet-Name", midletName);
			}
			out = new JarOutputStream(dest, manifest);
			JarEntry entry;
			for (Iterator iter = files.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				entry = new JarEntry(name);
				InputStream eIn = null;
				try {
					Object val = files.get(name);
					if (val instanceof File) {
						entry.setTime(((File) val).lastModified());
						eIn = new BufferedInputStream(new FileInputStream(
								(File) val));
					} else {
						eIn = (InputStream) val;
					}
					out.putNextEntry(entry);
					int len;
					while ((len = eIn.read(buf, 0, buf.length)) > 0) {
						out.write(buf, 0, len);
					}
					out.closeEntry();
				}catch (Exception e) {
					e.printStackTrace();
				} finally {
					if(eIn != null)
						eIn.close();
				}
			}
			
			while ((entry = in.getNextJarEntry()) != null) {
				if(files.containsKey(entry.getName()) || (excludeJava && entry.getName().endsWith(".java"))){
					in.closeEntry();
					continue;
				}
				entry = new JarEntry(entry.getName());
				out.putNextEntry(entry);
				int len;
				while ((len = in.read(buf, 0, buf.length)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.closeEntry();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(in != null)
					in.close();
			}finally{
				if(out != null)
					out.close();
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
