/*
 * Created on 05.03.2006
 *
 */
package ch.unizh.ori.common.text;

import ch.unizh.ori.nabu.core.Descriptable;

public interface Transliteration extends Descriptable {
	
	public Object toStandard(Object foreign);
	
	public Object toForeign(Object standard);
	
	public Script getScript();
	
	public void setScript(Script script);

}
