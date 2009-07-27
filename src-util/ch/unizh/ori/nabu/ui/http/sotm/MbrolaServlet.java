/*
 * Created on 19.07.2007
 *
 */
package ch.unizh.ori.nabu.ui.http.sotm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class MbrolaServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(MbrolaServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// all parameters
		String db = "fr1";
		if(getInitParameter("db") != null){
			db = getInitParameter("db");
		}
		String ini = "french.ini";
		if(getInitParameter("ini") != null){
			ini = getInitParameter("ini");
		}
		String key = "fr3";
		if(getInitParameter("key") != null){
			key = getInitParameter("key");
		}
		String text = "no";
		if(request.getParameter("text") != null)
			text = request.getParameter("text");
		boolean DEBUG=false;
		if(getInitParameter("debug") != null)
			DEBUG = Boolean.parseBoolean(getInitParameter("debug"));


		// now we set up the variables
		File dir = new File(getServletContext().getRealPath("/WEB-INF/mbrola/bin/"));
		String mbrdico = "/mbrdico.linux-gnu";
//		String mbrdico = "/mbrdico.exe";
		String[] cmd = {dir.getAbsolutePath()+mbrdico,ini};
		File tmpDir = new File(dir, "tmp");
		File outFile=File.createTempFile("out."+db, ".wav", tmpDir);
		String[] env = new String[]{key+"="+dir.getAbsolutePath()+"/mbrola -v 1.2 -t 1.5 "+dir.getAbsolutePath()+"/../db/"+db+"/"+db+" - "+outFile.getAbsolutePath()};
		if(DEBUG){
			log.debug(env[0]);
			log.debug(cmd[0]);
		}
		
		// let's do the work
		
		Process p = Runtime.getRuntime().exec(cmd, env, dir);
//		System.out.println("starting");
		PrintStream pOut = new PrintStream(p.getOutputStream());
		pOut.print(text);
		pOut.close();
//		System.out.println("printed text");
		if(DEBUG){
			// FIXME should go elsewhere?
			new Copier(p.getErrorStream(), System.err).start();
			new Copier(p.getInputStream(), System.out).start();
		}
		try {
			p.waitFor();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		
		// now copy everything to the user
		
	    OutputStream out = response.getOutputStream();
	    InputStream in = new BufferedInputStream(new FileInputStream(outFile));
		response.setHeader("Content-Type", "audio/wav");
		response.setHeader("Content-Length", String.valueOf(outFile.length()));

	    copyStream(in, out);
		in.close();
		
		if(!DEBUG)
			outFile.delete();

	}

	private static void copyStream(InputStream in, OutputStream out) throws IOException {
		// Transfer bytes from in to out
		byte[] buf = new byte[10240];
		int len;
		out.flush();
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.flush();
	}
	
	// to copy things in background from one stream to the other
	
	public static class Copier extends Thread{
		
		InputStream in;
		OutputStream out;
		
		public Copier(InputStream in, OutputStream out) {
			super();
			this.in = in;
			this.out = out;
		}

		public void run() {
			try {
				copyStream(in, out);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
