/*
 * Created on 25.11.2004
 *
 */
package ch.unizh.ori.nabu.voc;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author pht
 *
 */
public class ConvertPrefixSotm extends PrefixSotm {
	
	private ConvertSupport convertSupport = new ConvertSupport();
	
	public String getUtterance(String toSay, Map question) {
		try {
			return (String)convertSupport.map(toSay);
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return toSay;
	}
	
	public String getClassName() {
		return convertSupport.getClassName();
	}
	public String getMethodName() {
		return convertSupport.getMethodName();
	}
	public void setClassName(String string) throws SecurityException,
			NoSuchMethodException {
		convertSupport.setClassName(string);
	}
	public void setMethodName(String string) throws SecurityException,
			NoSuchMethodException {
		convertSupport.setMethodName(string);
	}
}
