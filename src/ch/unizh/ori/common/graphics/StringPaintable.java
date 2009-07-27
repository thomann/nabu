/*
 * Created on 04.03.2004
 */
package ch.unizh.ori.common.graphics;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Dimension2D;

import ch.unizh.ori.common.prefs.Preferences;

/**
 * @author pht
 */
public class StringPaintable implements Paintable {
	
	private String param;
	private Preferences prefs;

	/* (non-Javadoc)
	 * @see ch.unizh.ori.common.graphics.Paintable#size(java.awt.font.FontRenderContext)
	 */
	public Dimension2D size(FontRenderContext frc) {
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.unizh.ori.common.graphics.Paintable#setPreferences(ch.unizh.ori.common.prefs.Preferences)
	 */
	public void setPreferences(Preferences prefs) {
		this.prefs = prefs;
	}

	/* (non-Javadoc)
	 * @see ch.unizh.ori.common.graphics.Paintable#paint(java.awt.Graphics2D)
	 */
	public void paint(Graphics2D g) {
		g.drawString(param,0,0);
	}

	/* (non-Javadoc)
	 * @see ch.unizh.ori.common.graphics.Paintable#setParam(java.lang.Object)
	 */
	public void setParam(Object param) {
		this.param = (String)param;		// May throw ClassCastException
		
	}

}
