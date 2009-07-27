/*
 * Created on 02.04.2004
 */
package ch.unizh.ori.nabu.ui.http;

import javax.servlet.http.HttpServletRequest;

import ch.unizh.ori.nabu.ui.Renderer;

/**
 * @author pht
 */
public interface HttpRenderer extends Renderer {
	public abstract String getJspPath();
	public abstract void processRequest(
		HttpServletRequest request,
		boolean showSolution);
	public abstract String getFocusKey();
}