/*
 * FontMapper.java
 *
 * Created on 26. Juli 2003, 14:42
 */

package ch.unizh.ori.tuppu.hieroglyph;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ch.unizh.ori.tuppu.Box;

/**
 *
 * @author  pht
 */
public class FontMapper {
    
    private static final boolean DEBUG = Box.DEBUG;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(FontMapper.class);
    
    private static final String FONT_BASE = "HieroFonte";
    private URL base;
    private Map baseFonts = new HashMap();
    public  final float SUB_SIZE = 0.5F;
    private int size = 30;
    private FontRenderContext frc;
    private Map fonts = new HashMap();

    /** Creates a new instance of FontMapper */
//    public FontMapper(FontRenderContext frc, int size, boolean isSub){
//    	this(frc, size, isSub, null);
//    }
    
    public FontMapper(FontRenderContext frc, int size, boolean isSub, URL base){
        this.frc = frc;
        this.base = base;
    	if(isSub){
            this.size = (int)(size*SUB_SIZE);
        }else{
            this.size = size;
        }
    }
    
    public Font getBaseFont(String f){
        Font baseFont = (Font)baseFonts.get(f);
        if(baseFont == null){
            InputStream in = null;
            try{
                URL url = new URL(base,"hieroFontes/"+FONT_BASE+f+".ttf");
                if(DEBUG) log.debug(url);
                in = new BufferedInputStream(url.openStream());
                baseFont = Font.createFont(Font.TRUETYPE_FONT, in);
            }catch(Exception ex){
                ex.printStackTrace();
                baseFont = new Font(FONT_BASE+f, Font.PLAIN, 1);
            }finally{
                if(in != null){
                    try{
                        in.close();
                    }catch(Exception ex){}
                }
            }
            baseFonts.put(f, baseFont);
        }
        return baseFont;
    }
    
    private void initFont(String f){
         Font font = getBaseFont(f).deriveFont((float)size);
        fonts.put(f, font);
    }
    
    public Font getFont(HieroglyphicSigns.Donne d){
        return getFont(d.font);
    }
    public Font getFont(String f){
        Font font = (Font)fonts.get(f);
        if(font == null){
            initFont(f);
            font = (Font)fonts.get(f);
        }
        return font;
    }
    
	public FontRenderContext getFrc() {
		return frc;
	}
	public void setFrc(FontRenderContext frc) {
		this.frc = frc;
	}
}
