/*
 * Created on 12.01.2004
 */
package ch.unizh.ori.common.text;

/**
 * @author pht
 */
public class StringText implements Text {
	
	private Script script;
	private String text;
	
	public StringText(Script script, String text){
		this.script = script;
		this.text = text;
	}

	public Script getScript() {
		return script;
	}

	public String getText() {
		return text;
	}

}
