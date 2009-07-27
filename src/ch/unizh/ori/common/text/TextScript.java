/*
 * Created on 05.03.2006
 *
 */
package ch.unizh.ori.common.text;

import java.util.List;

public interface TextScript extends Script {

	public Object convert(String enc, Text text);
	
	public Text create(String enc, Object param);

	public List getEncodings();
}
