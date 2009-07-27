/*
 * Created on 24.11.2004
 *
 */
package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.DefaultDescriptable;


public class Voice extends DefaultDescriptable{
	private String prefix, type;
	/**
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param string
	 */
	public void setPrefix(String string) {
		prefix = string;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

}