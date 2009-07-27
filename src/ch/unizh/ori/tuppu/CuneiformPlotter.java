/*
 * Created on 05.01.2005
 *
 */
package ch.unizh.ori.tuppu;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.FontRenderContext;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author pht
 *  
 */
public class CuneiformPlotter extends Plotter {
	
	private static final String DEFAULT_NAME_TO_POSITION_FILENAME = "D:\\data\\workspace\\Nabu\\resources\\cuneiform\\akkadian.properties";
	private static final String DEFAULT_FONTBASE_URL = "file:/D:/download/TeX/naaktds/fonts/type1/public/cuneiform/naakaa.pfb";

	private Map nameToPosition = new HashMap();
	private URL fontbase = null;
	
	public void setTestFiles(){
		loadNameMappings(DEFAULT_NAME_TO_POSITION_FILENAME);
		try {
			fontbase =  new java.net.URL(DEFAULT_FONTBASE_URL);
		} catch (MalformedURLException ex1) {
			ex1.printStackTrace();
		}
	}

	private void loadNameMappings(String filename) {
		Properties p = new Properties();
		try {
			p.load(new BufferedInputStream(new FileInputStream(filename)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		for (Iterator iter = p.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			nameToPosition.put(key, new Position(p.getProperty(key)));
		}
	}
	
	public void init() {
		super.init();
		loadNameMappings((String)getInitParam("nameToPosition.filename", DEFAULT_NAME_TO_POSITION_FILENAME));
		try {
			fontbase =  new java.net.URL((String)getInitParam("fontbase.url",DEFAULT_FONTBASE_URL));
		} catch (MalformedURLException ex1) {
			ex1.printStackTrace();
		}
	}
	
	private char fontstyle = 'a';
	public static final int A = (int)'a';
	private int size = 100;

	public Plottable createPlottable(String text, Map param) throws IOException, FontFormatException {
		
		VectorBox ret = new VectorBox();
		FontRenderContext frc = new FontRenderContext(null, true, false);
		
//		StringBox.VISUAL = true;

		ret.direction = VectorBox.LEFT_TO_RIGHT;
		StringTokenizer tok = new StringTokenizer(text, " ");
		Font[] fonts = fonts(fontstyle, size);
		while(tok.hasMoreTokens()){
			String name = tok.nextToken();
			if(nameToPosition.containsKey(name)){
				Position d = (Position)nameToPosition.get(name);
				StringBox stringBox = new StringBox(String.valueOf((char)d.ch), fonts[d.font], frc);
				ret.add(stringBox);
			}else{
				Position d = new Position(name);
				ret.add(new StringBox(String.valueOf((char)d.ch), fonts[d.font], frc));
			}
		}
		ret.getGraphicsProperties().configure(param, "");
		return ret;
	}
	
	protected Font[] fonts(char fontstyle, int size) throws IOException, FontFormatException{
		Font[] ret = new Font[3];
		for(int i=0; i<ret.length; i++){
			//			ret[i] = new Font("NeoAssyrianClassicType1"+(char)(A+i), Font.PLAIN, size);
			URL url = new URL(fontbase, "naak"+fontstyle+(char)(A+i)+".pfb");
			Font font = Font.createFont(1, new BufferedInputStream(url.openStream()));
			ret[i] = font.deriveFont((float)size);
		}
		return ret;
	}
	
	public static class Position{
		public int font;
		public int ch;

		public Position(){}
		
		public Position(String s){
			font = s.charAt(0) - A;
			ch = Integer.parseInt(s.substring(1));
		}
	}

}
