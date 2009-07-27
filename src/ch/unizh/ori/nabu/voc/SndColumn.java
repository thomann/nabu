/*
 * Created on 01.04.2004
 */
package ch.unizh.ori.nabu.voc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pht
 */
public class SndColumn extends AbstractColumn implements Sotm {
	
	private String ad;
	
	private List voices = new ArrayList();

	public Object map(String[] arr) {
		if(getColumn() >= arr.length){
			return null;
		}
		return arr[getColumn()];
	}
	
	public String getUtterance(String toSay, Map question){
		return (String)question.get(getId());
	}
	
	public void addVoice(Voice v){
		voices.add(v);
	}
	
	/**
	 * @return
	 */
	public String getAd() {
		return ad;
	}

	/**
	 * @param string
	 */
	public void setAd(String string) {
		ad = string;
	}

	/**
	 * @return
	 */
	public List getVoices() {
		return voices;
	}

}
