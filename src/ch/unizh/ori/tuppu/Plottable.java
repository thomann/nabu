package ch.unizh.ori.tuppu;

import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 * @author pht
 *
 */
public interface Plottable {
	
	public Dimension getSize();
	public void plot(Graphics2D g);
	public GraphicsProperties getGraphicsProperties();
	public void setGraphicsProperties(GraphicsProperties graphicsProperties);
	
	public static class EmptyPlottable implements Plottable{

		public Dimension getSize() {
			return new Dimension(0,0);
		}

		public void plot(Graphics2D g) {
		}

		public void setGraphicsProperties(GraphicsProperties graphicsProperties) {
		}

		public GraphicsProperties getGraphicsProperties() {
			return null;
		}
		
	}

}
