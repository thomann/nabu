/*
 * Created on 16.05.2006
 *
 */
package ch.unizh.ori.nabu.ui.http;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

import ch.unizh.ori.nabu.voc.Vocabulary;

public class DataServlet  extends HttpServlet {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(DataServlet.class);
	
	public void init() throws ServletException {
		
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpCentral central = (HttpCentral) request.getAttribute("central");
		if(request.getParameter("download") != null){
			Vocabulary voc = (Vocabulary) central.getVocs().get(request.getParameter("download"));
			if(!voc.isFileSrc()){
				response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Text of vocabulary %s cannot be downloaded. Contact the administrator", voc));
				return;
			}
			response.setContentType("text/plain; charset="+voc.getSrc().getEnc());
			String filename = voc.getSrc().getSrc();
			response.setHeader("Content-disposition", "attachment; filename="+filename);
			URL url = new URL(voc.getBase(), filename);
			log.trace(url.getFile());
			URLConnection urlConnection = url.openConnection();
			response.setContentLength(urlConnection.getContentLength());
			InputStream in = urlConnection.getInputStream();
			OutputStream out = response.getOutputStream();
			byte[] buffer = new byte[1024];
			int length = 0;
			do{
				length = in.read(buffer);
				out.write(buffer);
			}while(length >= 0);
			in.close();
			out.flush();
		}else if(request.getParameter("downloadProperties") != null){
			Vocabulary voc = (Vocabulary) central.getVocs().get(request.getParameter("download"));
			if(!voc.isConfigurable()){
				response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Vocabulary %s has no simple configuration. Contact the administrator", voc));
				return;
			}
			response.setContentType("text/plain");
			String filename = voc.getPropertiesFilename();
			response.setHeader("Content-disposition", "attachment; filename="+filename);
			URL url = new URL(voc.getBase(), filename);
			log.trace(url.getFile());
			URLConnection urlConnection = url.openConnection();
			response.setContentLength(urlConnection.getContentLength());
			InputStream in = urlConnection.getInputStream();
			OutputStream out = response.getOutputStream();
			byte[] buffer = new byte[1024];
			int length = 0;
			do{
				length = in.read(buffer);
				out.write(buffer);
			}while(length >= 0);
			in.close();
			out.flush();
		}else if(request.getParameter("upload") != null){
			DiskFileUpload upload = new DiskFileUpload();
			try {

				FileItem voc = null;
				String vocName = null;

				if(request.getContentType().contains("multipart")){
					List items = upload.parseRequest((HttpServletRequest) request);
					Properties vars = new Properties();

					for (Iterator iter = items.iterator(); iter.hasNext();) {
						FileItem fi = (FileItem) iter.next();
						if(fi.isFormField()){
							vars.put(fi.getFieldName(), fi.getString("UTF-8"));
						}else{
							voc = fi;
							vocName = voc.getName();
						}
					}

					String id = vars.getProperty("upload");
					central.assertCanWrite(request, id);
					Vocabulary theVoc = (Vocabulary) central.getVocs().get(id);
					if(!theVoc.isFileSrc()){
						response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("Vocabulary %s cannot be uploaded this way. Contact the administrator", voc));
						return;
					}
					File vocFile = new File(theVoc.getBase().getFile(), theVoc.getSrc().getSrc());

					if(voc != null && voc.getSize()>0){
						voc.write(vocFile);
						voc = null;
					} else {
						String textAsString = vars.getProperty("file");
						if(textAsString != null){
							BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(vocFile),"UTF-8"));
							out.write(textAsString);
							out.close();
						}
					}
				}
				
			}catch(Exception ex){
				log("Problem in uploading", ex);
			}
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
