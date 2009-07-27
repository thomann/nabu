/*
 * Created on 03.01.2005
 *
 */
package ch.unizh.ori.tuppu;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.util.Map;

public abstract class Box implements Plottable {
	private Dimension d;
	
	private GraphicsProperties graphicsProperties = new GraphicsProperties();
	private Map params;

	public static final boolean DEBUG = false;

	public boolean FRAME = false;

	protected abstract void calcSize(Dimension d);

	public Dimension getSize() {
		if (d == null) {
			d = new Dimension();
			calcSize(d);
			Insets insets = getGraphicsProperties().getInsets();
			if(insets != null){
				d.width += insets.left + insets.right;
				d.height += insets.top + insets.bottom;
			}
		}
		return d;
	}

	public void plot(Graphics2D g) {
		Point pen = new Point(0, this.getSize().height);
		Insets insets = getGraphicsProperties().getInsets();
		if(insets != null){
			pen.x += insets.left;
			pen.y -= insets.top;
		}
		this.paint(g, pen);
		if (FRAME){
			Dimension s = getSize();
			g.drawRect(0, 0, (int) s.getWidth()-1, (int) s.getHeight()-1);
		}
	}

	public abstract void paint(Graphics2D g, Point pen);

	public GraphicsProperties getGraphicsProperties() {
		return graphicsProperties;
	}
	public void setGraphicsProperties(GraphicsProperties graphicsProperties) {
		this.graphicsProperties = graphicsProperties;
	}
}