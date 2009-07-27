/*
 * Created on 03.01.2005
 *  
 */
package ch.unizh.ori.tuppu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class StringBox extends Box {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(StringBox.class);

	public static FontRenderContext defaultFrc = new FontRenderContext(null,
			true, false);

	private String s;

	protected Font font;

	protected FontRenderContext frc;
	
	public StringBox(String s, Font font, FontRenderContext frc) {
		this.s = s;
		this.font = font;
		this.frc = frc;
	}

	public void calcSize(Dimension d) {
		TextLayout tl = new TextLayout(s, getFont(), frc);
		Rectangle2D bounds;
		bounds = tl.getBounds();
		d.setSize(bounds.getWidth(), bounds.getHeight());
	}

	public void paint(Graphics2D g, Point pen) {
		Font theFont = getFont();
		if (Box.DEBUG)
			log.debug("Painting: " + s + ", font: " + theFont + ",  pen: ("
					+ pen.x + "," + pen.y + ")");

		g.setFont(theFont);
		TextLayout layout = new TextLayout(s, font, frc);

		Dimension d = getSize();
		Rectangle2D bounds = layout.getBounds();
		float x = pen.x-(float)bounds.getX();
		float y = pen.y-(float)(bounds.getHeight()+bounds.getY());
		bounds.setRect(pen.x,pen.y-bounds.getHeight(),
				bounds.getWidth(),
				bounds.getHeight());
		
//		g.draw(bounds);
		layout.draw(g, x, y);
		
		pen.x += bounds.getWidth();
		pen.y -= bounds.getHeight();
		if (Box.DEBUG)
			log.debug(" -> (" + pen.x + "," + pen.y + ")");
	}

	protected Font getFont() {
		if(font == null){
			return getGraphicsProperties().getFont();
		}
		return font;
	}
}