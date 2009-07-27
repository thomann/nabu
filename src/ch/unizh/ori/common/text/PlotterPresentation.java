/*
 * Created on 03.03.2006
 *
 */
package ch.unizh.ori.common.text;

import org.apache.log4j.Logger;

import ch.unizh.ori.nabu.core.DefaultDescriptable;
import ch.unizh.ori.tuppu.Plotter;
import ch.unizh.ori.tuppu.StringPlotter;

public class PlotterPresentation extends DefaultDescriptable implements Presentation {
	
	private Plotter plotter;
	
	private String className;
	
	private Script script;
	
	private String plotterId;
	private String outTransliteration;
	
	private String fontname;

	public Plotter getPlotter() {
		if (plotter != null)
			return plotter;
		if(Plotter.getPlotter(getPlotterId())!=null)
			return plotter = Plotter.getPlotter(getPlotterId());
		try {
			plotter = (Plotter) Class.forName(getClassName()).newInstance();
			if (plotter instanceof StringPlotter) {
				StringPlotter stringPlotter = (StringPlotter) plotter;
				stringPlotter.setFontname(getFontname());
			}
			return plotter;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void setPlotter(Plotter plotter) {
		this.plotter = plotter;
	}

	public String getPlotterId() {
		return plotterId;
	}

	public void setPlotterId(String plotterId) {
		this.plotterId = plotterId;
	}

	public String getOutTransliteration() {
		return outTransliteration;
	}

	public void setOutTransliteration(String outEnc) {
		this.outTransliteration = outEnc;
	}

	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFontname() {
		return fontname;
	}

	public void setFontname(String fontname) {
		this.fontname = fontname;
		log.debug(fontname);
	}
	
	private static Logger log = Logger
			.getLogger(PlotterPresentation.class);
	
}
