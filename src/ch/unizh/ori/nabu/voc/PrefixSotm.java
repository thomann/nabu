/*
 * Created on 25.11.2004
 *
 */
package ch.unizh.ori.nabu.voc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.unizh.ori.common.text.DefaultScript;

/**
 * @author pht
 *
 */
public class PrefixSotm extends DefaultScript implements Sotm {
	
	private List voices = new ArrayList();

	public String getUtterance(String toSay, Map question) {
		return toSay;
	}

	public List getVoices() {
		return voices;
	}
	
	public void addVoice(Voice v){
		voices.add(v);
	}

}
