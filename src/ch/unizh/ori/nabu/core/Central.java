/*
 * Created on 08.03.2004
 */
package ch.unizh.ori.nabu.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ch.unizh.ori.common.text.DefaultScript;
import ch.unizh.ori.common.text.PlotterPresentation;
import ch.unizh.ori.common.text.Script;
import ch.unizh.ori.common.text.StringPresentation;
import ch.unizh.ori.nabu.voc.*;

/**
 * Manages the various vocabularies, scripts etc.
 * 
 * @author pht
 */
public class Central extends DefaultDescriptable implements Serializable {
	
	public Map vocs = new TreeMap();
	public Map scripts = new TreeMap();
	public Map preferences = new TreeMap();
	
	public Map sotm = new TreeMap();
	
	private transient Digester digester;
	
	public Central(){
		clear();
	}
	
	public void clear(){
		vocs = new TreeMap();
		scripts = new TreeMap();
		preferences = new TreeMap();
		sotm = new TreeMap();
//		addSotm(new TestSotm("sw"));
	}
	
	public static class TestSotm extends DefaultDescriptable implements Sotm{
		
		private List list;
		
		public TestSotm(String id){
			this(id, "http://orientx.unizh.ch:9080/nabutil/cgi-bin/fr1?text=");
		}
		
		public TestSotm(String id, String prefix){
			setId(id);
			setName("Test Sotm");
			Voice voice = new Voice();
			voice.setPrefix(prefix);
			voice.setName("Default Voice");
			list = Collections.singletonList(voice);
		}

		public String getUtterance(String toSay, Map question) {
			return toSay;
		}

		public List getVoices() {
			return list;
		}
		
	}
	
	public static void configureDigester(Digester digester){
		configureDigester(digester, true);
	}
	public static void configureDigester(Digester digester, boolean putInCentral){
		digester.setValidating( false );
		
		String pat;
		pat = "items/vocabulary";
		digester.addObjectCreate( pat, "class", Vocabulary.class );
		configureDigesterDesc(digester, pat);
		if(putInCentral){
			digester.addSetTop(pat, "setCentral");
			digester.addSetNext(pat, "addVoc");
		}
		
		pat = "items/vocabulary/src";
		configureSource(digester, pat);
		digester.addSetNext(pat, "setSrc");
		
		pat = "items/vocabulary/mapping/col";
		digester.addObjectCreate( pat, "class", StringColumn.class );
		configureDigesterDesc(digester, pat);
		digester.addSetTop(pat, "setVoc");
		digester.addSetNext(pat, "addCol");
		
		pat = "items/vocabulary/mapping/img";
		digester.addObjectCreate( pat, "class", ImgColumn.class );
		configureDigesterDesc(digester, pat);
		digester.addSetTop(pat, "setVoc");
		digester.addSetNext(pat, "addCol");
		
		pat = "items/vocabulary/mapping/snd";
		digester.addObjectCreate( pat, "class", SndColumn.class );
		configureDigesterDesc(digester, pat);
		digester.addSetTop(pat, "setVoc");
		digester.addSetNext(pat, "addSnd");
		
		pat = "items/vocabulary/mapping/snd/voice";
		digester.addObjectCreate( pat, "class", Voice.class );
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addVoice");
		
		pat = "items/vocabulary/modes/mode";
		digester.addObjectCreate( pat, "class", Mode.class );
		configureDigesterDesc(digester, pat);
		digester.addSetTop(pat, "setVoc");
		digester.addSetNext(pat, "addMode");
		
		pat = "items/vocabulary/modes/mode/filter";
		digester.addObjectCreate( pat, "class", Filter.class );
		configureDigesterDesc(digester, pat);
//		digester.addSetTop(pat, "setMode");
		digester.addSetNext(pat, "setFilter");
		
		pat = "items/sotm";
		digester.addObjectCreate( pat, "class", PrefixSotm.class );
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addSotm");
		
		pat = "items/sotm/voice";
		digester.addObjectCreate( pat, "class", Voice.class );
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addVoice");
		
		// Scripts
		pat = "items/script";
		digester.addObjectCreate( pat, "class", DefaultScript.class );
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addScript");
		
		pat = "items/script/transliteration";
		digester.addObjectCreate( pat, "class", DefaultScript.IdentityTransliteration.class );
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addTransliteration");
		digester.addSetTop(pat, "setScript");
		
		pat = "items/script/presentation";
		digester.addObjectCreate( pat, "class", StringPresentation.class );
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addPresentation");
		digester.addSetTop(pat, "setScript");
		
		pat = "items/script/plotter";
		digester.addObjectCreate( pat, "class", PlotterPresentation.class );
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addPresentation");
		digester.addSetTop(pat, "setScript");
		
//		pat = "items/script/sotm";
//		digester.addObjectCreate( pat, "class", PlotterPresentation.class );
//		configureDigesterDesc(digester, pat);
//		digester.addSetNext(pat, "addPresentation");
		
		
	}
	
	public static void configureSource(Digester digester, String pat) {
		digester.addObjectCreate( pat, "class", EmptyLineSource.class );
		configureDigesterDesc(digester, pat);
	}

	public static void configureDigesterDesc(Digester digester, String prefix){
		digester.addSetProperties( prefix );
		digester.addBeanPropertySetter(prefix + "/id");
		digester.addBeanPropertySetter(prefix + "/name");
		digester.addBeanPropertySetter(prefix + "/description");
	}
	
	private URL readerUrl;
	
	public void digestXML(String filename) throws SAXException, IOException{
		URL base = new File(filename).toURL();
		digestXML(base);
	}
	
	public void digestXML(URL base) throws SAXException, IOException{
		if(base == null){
			log.error("null base: "+base);
			return;
		}
		getDigester();
		readerUrl = base;
		digester.parse(base.openStream());
		readerUrl = null;
	}
	
	public Object parseObject(InputSource input) throws IOException, SAXException{
		Digester digester = new Digester();
		configureDigester(digester, false);
		digester.parse(input);
		return digester.getRoot();
	}

	public Digester getDigester() throws MalformedURLException {
		if(digester == null){
			digester = new Digester();
			configureDigester(digester);
		}
		digester.clear();
		digester.push(this);
		return digester;
	}
	
	public void addVoc(Vocabulary voc){
		vocs.put(voc.getId(), voc);
		voc.setBase(readerUrl);
	}
	
	public void addScript(Script script){
		getScripts().put(script.getId(), script);
	}

	/**
	 * @return
	 */
	public Map getPreferences() {
		return preferences;
	}

	/**
	 * @return
	 */
	public Map getScripts() {
		return scripts;
	}

	/**
	 * @return
	 */
	public Map getVocs() {
		return vocs;
	}

	/**
	 * @param map
	 */
	public void setPreferences(Map map) {
		preferences = map;
	}

	/**
	 * @param map
	 */
	public void setScripts(Map map) {
		scripts = map;
	}

	/**
	 * @param map
	 */
	public void setVocs(Map map) {
		vocs = map;
	}

	public Map getSotm() {
		return sotm;
	}
	
	public Sotm getSotm(String key){
		return (Sotm)sotm.get(key);
	}
	
	public Script getScript(String id){
		if(id == null)
			return null;
		return (Script)getScripts().get(id);
	}
	
	public void setSotm(Map sotm) {
		this.sotm = sotm;
	}
	
	public void addSotm(Sotm aSotm){
		this.sotm.put(aSotm.getId(), aSotm);
	}
	
	public void readDirs(String dirs) {
		String[] s = dirs.split(System.getProperty("path.separator"));
		for (int i = 0; i < s.length; i++) {
			readDir(s[i]);
		}
	}
	
	public void readDir(String dir) {
		log.info("Reading dir: "+dir);
		File[] fs = new File(dir).listFiles();
		if(fs == null){
			log("There are no files in: "+dir);
			return;
		}
		for(int i=0; i<fs.length; i++){
			File f = fs[i];
			if(f.getName().endsWith(".xml")){
				try {
					digestXML(f.getAbsolutePath());
				} catch (SAXException e) {
					log("readDir "+f, e);
				} catch (IOException e) {
					log("readDir "+f, e);
				}
			}
		}
	}

	public void readResource() {
		readResource("/etc/voc.xml");
		InputStream inStr = Central.class.getResourceAsStream("/index.txt");
		if(inStr != null){
			BufferedReader in = new BufferedReader(new InputStreamReader(inStr));
			try {
				for(String line; (line=in.readLine())!=null; )
					readResource(line);
			} catch (IOException ex) {
				log("readResource()", ex);
			}
		}
	}

	public void readResource(String name) {
		try {
			digestXML(Central.class.getResource(name));
		} catch (SAXException e) {
			log("readResource", e);
		} catch (IOException e) {
			log("readResource", e);
		}
	}
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(Central.class);

	public void log(String str, Throwable t) {
		log.error(str, t);
	}
	
	public void log(String str){
		log.info(str);
	}
	
/*	public static String doBase64(Reader reader) throws IOException {
		
		StringBuffer tmp = new StringBuffer();
		char[] buff = new char[1024];
		int len;
		while ((len = reader.read(buff)) > 0) {
			tmp.append(buff, 0, len);
		}
		String string = tmp.toString();
		byte[] s = string.getBytes("UTF-8");
		
		s = Base64.encodeBase64Chunked(s);
		
		return new String(s, "ASCII");
		

	}*/
}
