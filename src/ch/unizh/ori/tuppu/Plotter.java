/*
 * Created on 03.01.2005
 *
 */
package ch.unizh.ori.tuppu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.unizh.ori.nabu.core.DefaultDescriptable;

/**
 * @author pht
 *  
 */
public abstract class Plotter extends DefaultDescriptable {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(Plotter.class);
	
	private static Map plotters = new HashMap();

	protected GraphicsProperties graphicsProperties = new GraphicsProperties();
	
	private Map initParams = new HashMap();
	
	public void init(){
	}
	
	public void setId(String string) {
		super.setId(string);
		plotters.put(getId(), this);
	}
	
	public static Plotter getPlotter(String plotterId){
		return (Plotter) plotters.get(plotterId);
	}

	public abstract Plottable createPlottable(String text, Map param) throws IOException, FontFormatException;

	public Collection getCodes() {
		return null;
	}

	public BufferedImage plotToImage(String text, Map params) throws IOException, FontFormatException {
		Plottable p = createPlottable(text, params);
		Dimension size = p.getSize();
		size.width = Math.max(size.width,1);
		size.height = Math.max(size.height,1);
		BufferedImage img = new BufferedImage(size.width, size.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		plot(g, p);
		g.dispose();
		return img;
	}

	public static void plot(Graphics2D g, Plottable p) {
		Dimension size = p.getSize();
		GraphicsProperties gp = p.getGraphicsProperties();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(gp != null)
			gp.fillIn(g);
		g.clearRect(0,0,size.width, size.height);
		p.plot(g);
	}

	public static void paint(Graphics2D g, Box r) {
		paint(g, r, 0, 0);
	}

	public static void paint(Graphics2D g, Box r, int x, int y) {
		Point pen = new Point(x, y + r.getSize().height);
		r.paint(g, pen);
	}
	
	public Object getInitParam(String key){
		return initParams.get(key);
	}
	
	public Object getInitParam(String key, String defaultValue){
		Object val = initParams.get(key);
		return (val!=null && !val.equals("")) ? val:defaultValue;
	}
	
	public void setInitParam(String key, Object value){
		initParams.put(key, value);
	}
	
	public void putInitParams(Map params){
		initParams.putAll(params);
	}
	
	public Object getParameter(Map params, String key){
		if(params.containsKey(key)){
			return params.get(key);
		}
		return getInitParam(key);
	}
	public Object getParameter(Map params, String key, Object def){
		Object val = getParameter(params, key);
		if(val == null)
			return def;
		return val;
	}
	public int getIntegerParameter(Map params, String key, int defaultValue){
		Object val = getParameter(params, key);
		if(val != null){
			try{
				return Integer.parseInt((String)val);
			}catch (Exception e) {
			}
		}
		return defaultValue;
	}
	public boolean getBooleanParameter(Map params, String key, boolean defaultValue){
		Object val = getParameter(params,key);
		if(val == null)
			return defaultValue;
		if (val instanceof Boolean) {
			Boolean b = (Boolean) val;
			return b.booleanValue();
		}
		if (val instanceof String) {
			String s = (String) val;
			s = s.toLowerCase();
			if(s.equals("true")||s.equals("on"))
				return true;
			else
				return false;
		}
		return defaultValue;
	}
	public Color getColorParameter(Map param, String key, Color defaultValue){
		Object val = getParameter(param, key);
		if(val == null){
			return defaultValue;
		}
		if (val instanceof Color) {
			Color col = (Color) val;
			return col;
		}
		try{
		return Color.decode(val.toString());
		}catch(Exception ex){
			log.warn(ex);
			return defaultValue;
		}
	}

}