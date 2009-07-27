/*
 * Created on 05.04.2004
 */
package ch.unizh.ori.nabu.ui.swing;

import javax.swing.JComponent;

import ch.unizh.ori.nabu.ui.Renderer;

/**
 * @author pht
 */
public interface SwingRenderer extends Renderer {
	public void showSolution();
	public void activate();
//	public void hitReturn();
	public JComponent getComponent();
	public void setSession(NabuSession session);
	public void process(boolean showSolution);
}