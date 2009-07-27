/*
 * Created on 03.01.2005
 *
 */
package ch.unizh.ori.tuppu;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VectorBox extends Box {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(VectorBox.class);

	public static int LEFT_TO_RIGHT = 0;
	public static int TOP_TO_DOWN = 1;

	private List l = new ArrayList();

	public int direction = LEFT_TO_RIGHT;

	public void add(Box r) {
		l.add(r);
	}

	public void calcSize(Dimension d) {
		Dimension sub;
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			Box r = (Box) iter.next();
			sub = r.getSize();
			if (direction == LEFT_TO_RIGHT) {
				d.width += sub.width;
				d.height = Math.max(d.height, sub.height);
			} else if (direction == TOP_TO_DOWN) {
				d.height += sub.height;
				d.width = Math.max(d.width, sub.width);
			}
		}
	}

	public void paint(Graphics2D g, Point pen) {
		if (Box.DEBUG)
			log.debug("Vector: pen=(" + pen.x + "," + pen.y + ")");
		Dimension d = getSize();
		Dimension sub;
		Point subPen = new Point(pen);
		if (direction == LEFT_TO_RIGHT) {
			for (Iterator iter = l.iterator(); iter.hasNext();) {
				Box r = (Box) iter.next();
				sub = r.getSize();
				subPen.y = pen.y + (sub.height - d.height) / 2;
				r.paint(g, subPen);
				subPen.y = pen.y;
			}
			pen.setLocation(subPen.x, pen.y - d.height);
		} else if (direction == TOP_TO_DOWN) {
			for (int i = l.size() - 1; i >= 0; i--) {
				Box r = (Box) l.get(i);
				sub = r.getSize();
				subPen.x = pen.x - (sub.width - d.width) / 2;
				r.paint(g, subPen);
				subPen.x = pen.x;
			}
			pen.setLocation(pen.x + d.width, subPen.y);
		}
		if (Box.DEBUG)
			log.debug("Vector: pen -> (" + pen.x + "," + pen.y + ")");
	}

}