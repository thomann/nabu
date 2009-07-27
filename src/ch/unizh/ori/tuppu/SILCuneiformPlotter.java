/*
 * Created on 05.01.2005
 *
 */
package ch.unizh.ori.tuppu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.FontRenderContext;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author pht
 *  
 */
public class SILCuneiformPlotter extends Plotter {
	
	private static final String DEFAULT_NAME_TO_POSITION_FILENAME = "D:\\data\\workspace\\Nabu\\web\\WEB-INF\\config\\SILnames.properties";
	private static final String DEFAULT_FONTBASE_URL = "file:/D:/data/workspace/Nabu/web/WEB-INF/fonts/NEOASS.TTF";
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(SILCuneiformPlotter.class);

	private Map nameToPosition = new HashMap();
	private URL fontbase = null;
	
	public SILCuneiformPlotter() {
//		setTestFiles();
	}
	
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
		nameToPosition = new HashMap(p);
	}
	
	public void init() {
		super.init();
		loadNameMappings((String)getInitParam("nameToUnicode.filename", DEFAULT_NAME_TO_POSITION_FILENAME));
		try {
			fontbase =  new java.net.URL((String)getInitParam("fontbase.url",DEFAULT_FONTBASE_URL));
			log.debug(fontbase);
		} catch (MalformedURLException ex1) {
			ex1.printStackTrace();
		}
	}
	
	private int fontSize = 74;
	private Font font;
	
	private Font getFont() throws FontFormatException, IOException{
		if (font == null) {
			font = Font.createFont(Font.TRUETYPE_FONT, fontbase.openStream());
			font = font.deriveFont((float)fontSize);
		}
		return font;
	}

	public Plottable createPlottable(String text, Map param) throws IOException, FontFormatException {
		
		VectorBox ret = new VectorBox();
		FontRenderContext frc = new FontRenderContext(null, true, false);
		
//		StringBox.VISUAL = true;

		ret.direction = VectorBox.LEFT_TO_RIGHT;
		StringTokenizer tok = new StringTokenizer(text, " ");
		Font font = getFont(); //new Font("NeoAssyrianRAI", Font.PLAIN, fontSize);
		while(tok.hasMoreTokens()){
			String name = tok.nextToken();
			if(nameToPosition.containsKey(name)){
				StringBox stringBox = new StringBox((String)nameToPosition.get(name), font, frc);
				ret.add(stringBox);
				ret.add(new StrutBox(new Dimension(20,0)));
			}else{
				ret.add(new StringBox(name, font, frc));
			}
		}
		ret.getGraphicsProperties().configure(param, "");
		return ret;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int size) {
		this.fontSize = size;
	}
	

}
