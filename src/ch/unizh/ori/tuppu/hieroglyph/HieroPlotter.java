/*
 * HieroPlotter.java
 *
 * Created on 26. September 2002, 21:24
 */

package ch.unizh.ori.tuppu.hieroglyph;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.util.Map;
import java.util.StringTokenizer;

import ch.unizh.ori.tuppu.Box;
import ch.unizh.ori.tuppu.GraphicsProperties;
import ch.unizh.ori.tuppu.Plotter;
import ch.unizh.ori.tuppu.Plottable;
import ch.unizh.ori.tuppu.StringBox;
import ch.unizh.ori.tuppu.VectorBox;

/**
 *
 * @author  pht
 */
public class HieroPlotter extends Plotter {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(HieroPlotter.class);
    
    static final double BLA = 1.3;

	public static final String PREFIX="hiero.", SMALL=PREFIX+"small.";
    
    private int size = 20;
    private HieroglyphicSigns hgSigns;
    private FontMapper fontMapper;
    private FontMapper subFontMapper;
    
    public HieroPlotter(){
    	
    }
    
    public HieroPlotter(Graphics2D fontGraphics, int size){
        if(Box.DEBUG) log.debug("HieroPlotter: size="+size);
        this.size = size;
//        FontRenderContext frc = fontGraphics.getFontRenderContext();
//        initMappers(size, frc);
     }
    
    public void init() {
		super.init();
		
		// hgSigns has to be inited before initMappers!!
		String base = (String) getInitParam(PREFIX+"base");
		if(base != null){
			hgSigns = new HieroglyphicSigns(base);
		}else
			hgSigns = HieroglyphicSigns.getDefault();
		Object size = getInitParam(PREFIX+"size");
		if(size != null)
			try{
				this.size = Integer.parseInt((String) size);
			}catch (NumberFormatException e) {
				e.printStackTrace();
			}
		FontRenderContext frc = new FontRenderContext(null, true, false);
		initMappers(this.size, frc);
	}
    
	/**
	 * @param size
	 * @param frc
	 */
	private void initMappers(int size, FontRenderContext frc) {
		fontMapper = new FontMapper(frc, size, false, hgSigns.getBase());
        subFontMapper = new FontMapper(frc, size, true, hgSigns.getBase());
	}

	public Plottable createPlottable(String text, Map param){
		Font normalFont = GraphicsProperties.configureFont(param, PREFIX);
		Font subNormalFont;
		if(param.containsKey(SMALL+GraphicsProperties.FONT_NAME)){
			subNormalFont = GraphicsProperties.configureFont(param, SMALL);
		}else{
			subNormalFont = normalFont.deriveFont((float)normalFont.getSize() * subFontMapper.SUB_SIZE);
		}
		Box ret = constructBox(text, normalFont, subNormalFont);
		ret.getGraphicsProperties().configure(param, "");
		return ret;
	}
	
	public Box constructBox(Graphics2D g2, String code){
        return constructBox(g2, code, null, null);
    }
    
    public Box constructBox(Graphics2D g2, String code, Font normalFont,
            Font subNormalFont){
        if(normalFont == null){
            normalFont = g2.getFont();
        }
        if(subNormalFont == null){
            subNormalFont = normalFont.deriveFont((float)normalFont.getSize() * subFontMapper.SUB_SIZE);
        }
        return constructBox(code, normalFont, subNormalFont);
    }
    
    public Box constructBox(String code, Font normalFont,
            Font subNormalFont){
        VectorBox ret = new VectorBox();
        ret.direction = VectorBox.LEFT_TO_RIGHT;
        
        StringTokenizer caratST = new StringTokenizer(code, "-");
        while(caratST.hasMoreTokens()){
            String carat = caratST.nextToken();
            boolean lonely = (carat.indexOf(':') < 0);
            if(lonely){
                HieroglyphicSigns.Donne d = hgSigns.getDonne(carat.trim());
                if(d==null){
                    ret.add(new StringBox(carat, normalFont, fontMapper.getFrc()));
                }else{
                    ret.add(new HieroPlotter.HieroBox(d, fontMapper));
                }
            }else{
                VectorBox column = new VectorBox();
                column.direction = VectorBox.TOP_TO_DOWN;
                ret.add(column);
                
                StringTokenizer subRowST = new StringTokenizer(carat, ":");
                while(subRowST.hasMoreTokens()){
                    String subRow = subRowST.nextToken();
                    if(Box.DEBUG) log.debug("subRow: "+subRow);
                    VectorBox row = new VectorBox();
                    row.direction = VectorBox.LEFT_TO_RIGHT;
                    column.add(row);
                    
                    StringTokenizer subST = new StringTokenizer(subRow, "*");
                    while(subST.hasMoreTokens()){
                        String sub = subST.nextToken();
                        if(Box.DEBUG) log.debug("sub: "+sub);
                        
                        HieroglyphicSigns.Donne d = hgSigns.getDonne(sub.trim());
                        if(d==null){
                            row.add(new StringBox(sub, subNormalFont, subFontMapper.getFrc()));
                        }else{
                            row.add(new HieroPlotter.HieroBox(d, subFontMapper));
                        }
                        
                    }
                }
            }
        }
        return ret;
    }
	
    public static class HieroBox extends StringBox{
        public HieroBox(HieroglyphicSigns.Donne d, FontMapper fm){
            super(String.valueOf(d.ch), fm.getFont(d.font), fm.getFrc());
        }
        public void calcSize(Dimension d){
            super.calcSize(d);
            d.height *= HieroPlotter.BLA;
        }
    }
    
}
