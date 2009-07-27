/*
 * Created on 19.07.2007
 *
 */
package ch.unizh.ori.nabu.ui.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.sound.sampled.*;

import ch.unizh.ori.nabu.core.DefaultQuestionIterator;
import ch.unizh.ori.nabu.voc.FieldStream;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.Sotm;
import ch.unizh.ori.nabu.voc.StringColumn;
import ch.unizh.ori.nabu.voc.Vocabulary;
import ch.unizh.ori.nabu.voc.Voice;

public class NabuPodcast extends HttpServlet {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(NabuPodcast.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		HttpCentral central = (HttpCentral) request.getAttribute("central");
		
		String mode = request.getParameter("mode");
		String fillVariables = request.getParameter("fillVariables");
		if(mode == null || "on".equals(fillVariables)){
			getServletContext().getRequestDispatcher("podcast.jsp").forward(request, response);
		}
		
		Vocabulary voc = (Vocabulary) central.getVocs().get(request.getParameter("id"));
			
		List lections = new ArrayList();
		boolean all = isOn(request, "l-all");
		if(voc.getLections() != null){
			for (Iterator iterator = voc.getLections().iterator();
				iterator.hasNext();
				) {
				FieldStream fs = (FieldStream) iterator.next();
				
				if(all || isOn(request, "l."+fs.getId())){
					lections.add(fs);
				}
			}
		}
		
		Mode m = (Mode) voc.getModes().get(mode);
		HttpRenderer r = new HttpMappingRenderer(m);

		DefaultQuestionIterator iter = voc.createIter(lections, r, m.getFilter(), m);
		iter.init();


	    try {
	    	
//	    	AudioFormat format = null;
//			try {
//				format = AudioSystem.getAudioInputStream(new URL("http://nabu.uzh.ch/nabu/sound/thaiForBeginners/1/1_030.wav")).getFormat();
//			} catch (UnsupportedAudioFileException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	    	

	    	List streams = new ArrayList();
	    	List files = new LinkedList();
//	        // Create line
//	        TargetDataLine.Info info2 = new DataLine.Info(
//	        		TargetDataLine.class, format,
//	            (1024*format.getFrameSize()));
//	        TargetDataLine targetLine = null;
//			targetLine = (TargetDataLine) AudioSystem.getLine(info2);
//	        targetLine.open(format);
//
//	        new Copier(targetLine, out).start();
//	        targetLine.start();

	    	AudioFormat format = new AudioFormat(44100f, 16, 1, true, false);
//	    	AudioFormat format = new AudioFormat(16000f, 16, 1, true, false);
	        
	        iter.next();
	        while(iter.getQuestion() != null){
	        	for (Iterator iterator = m.createModeFields().iterator(); iterator.hasNext();) {
	        		ModeField mf = (ModeField) iterator.next();
	        		
	        		appendSotm(iter, mf, streams, format, files);
	        		
	        		if(iterator.hasNext()){
	        			double seconds = .3;
	        			int length = (int) (format.getChannels() * format.getSampleRate() * seconds);
	        			streams.add(new SilenceAudioInputStream(null, format, length));
	        		}
	        	}
	        	iter.next();
	        }

//	        AudioFormat format = new AudioFormat(16000f, 16, 1, true, false);
	        SequenceAudioInputStream seq = new SequenceAudioInputStream(format, streams);
	        
	        if(false){
		        AudioFormat.Encoding mpeg1l3 = new AudioFormat.Encoding("MPEG1L3");
		        AudioInputStream intermediate = AudioSystem.getAudioInputStream(new AudioFormat(
		        		AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels()*2, format.getSampleRate(), false
		        		), seq);
		        AudioInputStream mp3Stream = AudioSystem.getAudioInputStream(mpeg1l3, intermediate);
		        
		        AudioFileFormat.Type mp3 = new AudioFileFormat.Type("MP3", ".mp3");
		        
		        OutputStream out = response.getOutputStream();
		        response.setHeader("Content-Type", "audio/mpeg");
		        
		        AudioSystem.write(mp3Stream, mp3, out);
	        }else{
//		response.setHeader("Content-Length", String.valueOf(outFile.length()));
		        OutputStream out = response.getOutputStream();
		        response.setHeader("Content-Type", "audio/x-wav");
		        long fl = seq.getFrameLength();
		        if(fl != AudioSystem.NOT_SPECIFIED)
		        	response.setHeader("Content-Length", fl * format.getFrameSize()+"");
		        AudioSystem.write(seq, AudioFileFormat.Type.WAVE, out);
	        }
	        
	        for (Iterator iterator = files.iterator(); iterator.hasNext();) {
				File f = (File) iterator.next();
				f.delete();
			}
	        
	    } catch (Throwable e) {
	    	throw new ServletException(e);
	    }
	    
	}
	
	private void writeStream(AudioInputStream stream, OutputStream out)
	throws IOException, LineUnavailableException {
//		AudioSystem.write(stream, AudioFileFormat.Type.WAVE, out);
        // At present, ALAW and ULAW encodings must be converted
        // to PCM_SIGNED before it can be played
//        AudioFormat format = stream.getFormat();
//        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
//            format = new AudioFormat(
//                    AudioFormat.Encoding.PCM_SIGNED,
//                    format.getSampleRate(),
//                    format.getSampleSizeInBits()*2,
//                    format.getChannels(),
//                    format.getFrameSize()*2,
//                    format.getFrameRate(),
//                    true);        // big endian
//            stream = AudioSystem.getAudioInputStream(format, stream);
//        }
//    
//        // Create line
//        SourceDataLine.Info info = new DataLine.Info(
//            SourceDataLine.class, stream.getFormat(),
//            ((int)stream.getFrameLength()*format.getFrameSize()));
//        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
//        line.open(stream.getFormat());
//        line.start();
//        line.drain();
//        line.stop();
	}

	private void appendSotm(DefaultQuestionIterator iter,
			ModeField mf, List streams, AudioFormat format, List files) throws IOException, MalformedURLException,
			ServletException {
		Sotm sotm = mf.getColumn().getVoc().getSound(mf.getKey());
		boolean hasSotm = sotm != null && sotm.getVoices().size()>0;
		
		if(hasSotm){
			int i = (int)(Math.random()*sotm.getVoices().size());
			Voice v = (Voice) sotm.getVoices().get(i);
			Map q = (Map)iter.getQuestion();
			String toSay = null;
			if (mf.getColumn() instanceof StringColumn) {
				StringColumn sc = (StringColumn) mf.getColumn();
				toSay = (String) q.get(mf.getColumn().getId());
			}
			String name = sotm.getUtterance(toSay, q);
			if(name == null || name.trim().length() == 0)
				return;
			String prefix = v.getPrefix();
			
			if(name != null){
				String s = prefix + URLEncoder.encode(name, "UTF-8");
				AudioInputStream stream;
				try {
					log.debug("trying: "+s);
					URL url = new URL(s);
					File f = downloadTemp(url);
					log.debug(f+": ex-lesbar"+f.exists()+f.canRead());
//					if(true)
//						throw new ServletException(f+": "+f.exists()+f.canRead());
					files.add(f);
					stream = AudioSystem.getAudioInputStream(f);
					streams.add(stream);
	        		
	        		if(!mf.isAsking()){
	        			double seconds = 1.0;
	        			long length = stream.getFrameLength();
						if(length == AudioSystem.NOT_SPECIFIED)
							length = 0;
						length += format.getChannels() * format.getSampleRate() * seconds;
	        			streams.add(new SilenceAudioInputStream(null, format, length));
	        			streams.add(AudioSystem.getAudioInputStream(f));
	        		}
	        		
//					writeStream(stream, out);
				} catch (IOException e) {
					log.warn(e);
					log("Loading "+s, e);
				} catch (Throwable e) {
					throw new ServletException(e);
				}
			}
		}
	}

	private static File downloadTemp(URL url) throws IOException{
		File ret = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			String extension = ".wav";
			if(url.getFile().endsWith(".mp3") || url.getFile().contains(".mp3?"))
				extension = ".mp3";
			ret = File.createTempFile("nabuPodcast.", extension);
			boolean onNabu = InetAddress.getLocalHost().getHostName().equals("idnabu");
			if(onNabu && url.getHost().equals("nabu.uzh.ch"))
				url = new URL(url.getProtocol(), url.getHost(), 8080, url.getFile());
			log.trace("NabuPodcast.downloadTemp(): "+url+" --> "+ret);
			ret.deleteOnExit();
			out = new BufferedOutputStream(new FileOutputStream(ret), 1024);
			URLConnection uc = url.openConnection();
			uc.setConnectTimeout(30000);
			in = new BufferedInputStream(uc.getInputStream(), 1024);
			byte[] buffer = new byte[1024];
			int len = 0;
			while( (len=in.read(buffer)) >= 0)
				out.write(buffer, 0, len);
			in.close();
			out.close();
		} catch (IOException e) {
			if(ret != null)
				ret.delete();
			if(out != null)
				out.close();
			if(in != null)
				in.close();
			throw e;
		}
		
		return ret;
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
	
	private static void copyStream2(TargetDataLine in, OutputStream out) throws IOException {
		// Transfer bytes from in to out
		byte[] buf = new byte[10240];
		int len;
		out.flush();
		while ((len = in.read(buf, 0,buf.length)) >= 0) {
			out.write(buf, 0, len);
		}
		out.flush();
	}
	
	private static class SilenceAudioInputStream extends AudioInputStream {
		private long toRead;

		private SilenceAudioInputStream(InputStream stream, AudioFormat format,
				long length) {
			super(stream, format, length);
			toRead = length * format.getFrameSize();
		}

		@Override
		public int read(byte[] b, int off, int len)
				throws IOException {
			if(toRead < 1)
				return -1;
			int write = (int)Math.min(toRead, len);
			for(int i=0; i<write; i++)
				b[off+i] = 0;
			toRead -= write;
			return write;
		}

		@Override
		public int read() throws IOException {
			if(toRead <= 0)
				return -1;
			toRead--;
			return 0;
		}
	}

	// to copy things in background from one stream to the other
	
	public static class Copier extends Thread{
		
		TargetDataLine in;
		OutputStream out;
		
		public Copier(TargetDataLine in, OutputStream out) {
			super();
			this.in = in;
			this.out = out;
		}

		public void run() {
			try {
				copyStream2(in, out);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static boolean isOn(ServletRequest request, String param){
		String s = request.getParameter(param);
		return s!=null;
	}
	
	public static class JoinerAudioInputStream extends AudioInputStream {

		public JoinerAudioInputStream(InputStream stream, AudioFormat format,
				long length) {
			super(stream, format, length);
			this.format = format;
		}
		
		private Iterator streams;
		private AudioFormat format;
		private AudioInputStream current = this;
		private URL currentURL;
		
		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			int ret = -1;
			if(current != null)
				ret = current.read(b, off, len);
			try {
				while(ret < 0 && streams.hasNext()){
					currentURL = (URL) streams.next();
					current = AudioSystem.getAudioInputStream(currentURL);
					// TODO convert format
					ret = current.read(b, off, len);
				}
			} catch (UnsupportedAudioFileException e) {
				throw new RuntimeException("Reading "+currentURL, e);
			}
			return ret;
		}

		public Iterator getStreams() {
			return streams;
		}

		public void setStreams(Iterator streams) {
			this.streams = streams;
		}
		
	}
	
//	public static void test(){
//		AudioInputStream in = AudioSystem.getAudioInputStream(new URL("http://nabu.uzh.ch/nabu/sound/thaiForBeginners/1/1_032.wav"))
//		JoinerAudioInputStream aip = new JoinerAudioInputStream(in, in.getFormat(), );
//	}


}
