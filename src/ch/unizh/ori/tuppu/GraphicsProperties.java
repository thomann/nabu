/*
 * Created on 04.01.2005
 *
 */
package ch.unizh.ori.tuppu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author pht
 *  
 */
public class GraphicsProperties implements Cloneable, Serializable {

	private Color background = Color.white;

	private Color color = Color.black;

	private Font font;

	private AffineTransform transform;

	private boolean antialias = false;

	private Insets insets = null;

	public static final String BACKGROUND = "background";

	public static final String COLOR = "color";

	public static final String FONT = "font";

	public static final String FONT_NAME = "fontname";

	public static final String FONT_SIZE = "fontsize";

	public static final String ANTIALIAS = "antialias";

	public static final String SCALE = "scale";

	public static final String INSETS = "insets";

	public GraphicsProperties() {
	}

	public GraphicsProperties(Graphics2D g2) {
		override(g2);
	}

	public void copy(Graphics2D g2) {
		if (background == null)
			background = g2.getBackground();
		if (color == null)
			color = g2.getColor();
		if (font == null)
			font = g2.getFont();
		antialias = g2.getFontRenderContext().isAntiAliased();
		transform = g2.getFontRenderContext().getTransform();
	}

	public void copyFrom(GraphicsProperties gp) {
		background = gp.getBackground();
		color = gp.getColor();
		font = gp.getFont();
		antialias = gp.isAntialias();
		transform = gp.getTransform();
		insets = gp.getInsets();
	}

	public void clear() {
		background = null;
		color = null;
		font = null;
		antialias = false;
		insets = null;
		transform = null;
	}

	public void override(Graphics2D g2) {
		clear();
		if (g2 != null)
			copy(g2);
	}

	public void fillIn(Graphics2D g2) {
		if (background != null)
			g2.setBackground(background);
		if (color != null)
			g2.setColor(color);
		if (font != null)
			g2.setFont(font);
		if (transform != null)
			g2.transform(transform);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				(antialias) ? RenderingHints.VALUE_ANTIALIAS_ON
						: RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	public void configure(Map m, String prefix) {
		Object v;

		v = m.get(prefix + BACKGROUND);
		if (v != null)
			background = Color.decode((String) v);

		v = m.get(prefix + COLOR);
		if (v != null)
			color = Color.decode((String) v);

		font = configureFont(m, prefix);

		v = m.get(prefix + ANTIALIAS);
		if (v != null)
			antialias = ("true".equalsIgnoreCase((String) v)
					|| "yes".equalsIgnoreCase((String) v) || "on"
					.equalsIgnoreCase((String) v));

		v = m.get(prefix + SCALE);
		if (v != null) {
			double d = Double.parseDouble((String) v);
			transform = AffineTransform.getScaleInstance(d, d);
		}

		v = m.get(prefix + INSETS);
		if (v != null) {
			try {
				StringTokenizer tok = new StringTokenizer((String) v, ", ");
				insets = new Insets(Integer.parseInt(tok.nextToken()),
						Integer.parseInt(tok.nextToken()), Integer.parseInt(tok
								.nextToken()), Integer
								.parseInt(tok.nextToken()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param m
	 * @param prefix
	 */
	public static Font configureFont(Map m, String prefix) {
		Font ret;
		Object v;
		v = m.get(prefix + FONT);
		if (v != null)
			ret = (Font) v;
		else {
			String name = "Dialog";
			int size = 14;
			
			v = m.get(prefix + FONT_NAME);
			if (v != null)
				name = (String) v;
			v = m.get(prefix + FONT_SIZE);
			if (v != null)
				try {
					size = Integer.parseInt((String) v);
				} catch (Exception e) {}
			ret = new Font(name, Font.PLAIN, size);
		}
		return ret;
	}

	public FontRenderContext getFrc(){
		return new FontRenderContext(transform, antialias, false);
	}
	
	public boolean isAntialias() {
		return antialias;
	}
	public void setAntialias(boolean antialias) {
		this.antialias = antialias;
	}
	public Color getBackground() {
		return background;
	}
	public void setBackground(Color background) {
		this.background = background;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public Insets getInsets() {
		return insets;
	}
	public void setInsets(Insets insets) {
		this.insets = insets;
	}
	public AffineTransform getTransform() {
		return transform;
	}
	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}
}