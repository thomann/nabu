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
public interface Paintable {
	
	public Dimension2D size(FontRenderContext frc);
	
	public void paint(Graphics2D g);
	
	public void setParam(Object param);
	
	public void setPreferences(Preferences prefs);

}
