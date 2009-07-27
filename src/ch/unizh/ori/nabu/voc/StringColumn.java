/*
 * Created on 31.03.2004
 */
package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.common.text.Text;
import ch.unizh.ori.common.text.TextScript;

/**
 * @author pht
 */
public class StringColumn extends AbstractColumn {
	
	private String script;
	private String del;
	private String transliteration;
	
	public Object map(String[] arr) {
		if(false){
			TextScript s = (TextScript)getCentral().scripts.get(this.script);
			Text t = s.create(transliteration, arr[getColumn()]);
			return t;
		}
		String t = "";
		if(getColumn() < arr.length){
			t = arr[getColumn()];
		}
		return t;
	}
	
	/**
	 * @return
	 */
	public String getDel() {
		return del;
	}

	/**
	 * @return
	 */
	public String getScript() {
		return script;
	}

	/**
	 * @param string
	 */
	public void setDel(String string) {
		del = string;
	}

	/**
	 * @param string
	 */
	public void setScript(String string) {
		script = string;
	}

	/**
	 * @return
	 */
	public String getTransliteration() {
		return transliteration;
	}

	/**
	 * @param string
	 */
	public void setTransliteration(String string) {
		transliteration = string;
	}

}
