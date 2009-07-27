/*
 * Created on 28.02.2006
 *
 */
package ch.unizh.ori.tuppu;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

public class StrutBox extends Box {
	
	private Dimension d;

	protected void calcSize(Dimension d) {
		d.setSize(this.d);
	}

	public void paint(Graphics2D g, Point pen) {
		Dimension d = getSize();
		pen.x += d.width;
		pen.y -= d.height;
	}

	public StrutBox(Dimension d) {
		this.d = d;
	}

}
