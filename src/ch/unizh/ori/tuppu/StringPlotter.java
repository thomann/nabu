/*
 * Created on 04.01.2005
 *
 */
package ch.unizh.ori.tuppu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Insets;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author pht
 *  
 */
public class StringPlotter extends Plotter {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(StringPlotter.class);
	
	public static final String FONTDIR_PROPERTY = "ch.uzh.ori.tuppu.fontdir";

	private String fontname = "GenR102.TTF";
	private Font font = null;
	
	private int fontsize = 36;
	
	@Override
	public void init() {
		super.init();
		fontname = (String) getInitParam("fontname", fontname);

		String fs = (String)getInitParam("fontsize");
		if(fs != null){
			try {
				fontsize = Integer.parseInt(fs);
			} catch (NumberFormatException e) {
			}
		}
	}

	public Plottable createPlottable(String text, Map param) {
		String fontname = this.fontname;
		if(fontname == null)
			fontname = (String) getParameter(param, "fontname");
		int fontsize = getIntegerParameter(param, "fontsize", this.fontsize);
		if( font == null || (fontname != null&&this.fontname != fontname) || fontsize != this.fontsize){
			font = createFont(fontname, fontsize);
			this.fontname = fontname;
			this.fontsize = fontsize;
		}else{
			font = getFont();
		}
		boolean antialias = getBooleanParameter(param, "antialias", true);
		
		FontRenderContext frc = new FontRenderContext(new AffineTransform(),
				antialias, false);
		if(text.equals("hello"))
			text = "<\u0627\u0644\u064A\u0639\u0633\u0647 \u0639 \u0639\u0634\u0633>";
		StringBox sb = new StringBox(text, font, frc);
		
		int i=getIntegerParameter(param, "insets", 5);
		GraphicsProperties gp = sb.getGraphicsProperties();
		gp.setInsets(new Insets(i,i,i,i));
		gp.setFont(font);
		gp.setAntialias(antialias);
		gp.setBackground(getColorParameter(param, "background", Color.white));
		gp.setColor(getColorParameter(param, "color", Color.black));
		
		return sb;
	}

	public String getFontname() {
		return fontname;
	}

	public void setFontname(String fontname) {
		if(fontname != null || !fontname.equals(fontname))
			font = null;
		this.fontname = fontname;
		
	}

	private Font getFont() {
		if(font == null && fontname != null){
			createFont(fontname, fontsize);
		}
		return font;
	}

	private static Font createFont(String fontname, int fontsize) {
		Font font = null;
		try {
			File fontfile = new File(System.getProperty(FONTDIR_PROPERTY), fontname).getCanonicalFile();
			log.debug(fontfile+ " @"+fontsize);
			if(fontfile.exists()){
				font = Font.createFont(Font.TRUETYPE_FONT, fontfile);
				font = font.deriveFont((float)fontsize);
			}
		} catch (FontFormatException e) {
			log.debug("Problem getting: "+fontname+" @"+fontsize, e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(font == null){
			font = new Font(fontname, Font.PLAIN, fontsize);
		}
		return font;
	}

}