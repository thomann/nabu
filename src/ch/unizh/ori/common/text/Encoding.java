/*
 * Created on 12.01.2004
 */
package ch.unizh.ori.common.text;

import ch.unizh.ori.nabu.core.Descriptable;

/**
 * @author pht
 */
public interface Encoding extends Descriptable {
	
	public Script getScript();

	/**
	 * @param text
	 * @return
	 */
	public Object convert(Text text);

	/**
	 * @param param
	 * @return
	 */
	public Text create(Object param);

}
