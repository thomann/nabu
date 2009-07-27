/*
 * Created on 25.11.2004
 *
 */
package ch.unizh.ori.common.text.helper;

import ch.unizh.ori.common.text.Script;
import ch.unizh.ori.common.text.Transliteration;
import ch.unizh.ori.nabu.core.DefaultDescriptable;

/**
 * @author J. Thomann
 *
 * created first in project JApd
 * 
 */
public class GermanHelper extends DefaultDescriptable implements Transliteration {
	
	private Script script;
	public Script getScript() {
		return script;
	}
	public void setScript(Script script) {
		this.script = script;
	}
	public Object toForeign(Object standard) {
		return ((String)standard).replaceAll("\u00df", "ss");
	}
	public Object toStandard(Object foreign) {
		return ((String)foreign).replaceAll("\u00df", "ss");
	};
}
