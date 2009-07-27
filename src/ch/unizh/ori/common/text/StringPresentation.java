/*
 * Created on 02.03.2006
 *
 */
package ch.unizh.ori.common.text;

import ch.unizh.ori.nabu.core.DefaultDescriptable;

public class StringPresentation extends DefaultDescriptable implements Presentation {

	private String font;
	private String fontSize;
	
	private String outTransliteration;
	
	private Script script;

	public StringPresentation() {
		super();
	}

	public StringPresentation(String id, String name) {
		super(id, name);
	}

	public String getOutText(Object value, String enc){
		return (String) script.convert(value, enc, getOutTransliteration());
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
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

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
	
}
