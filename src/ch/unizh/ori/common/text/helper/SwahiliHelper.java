/*
 * Created on Aug 25, 2004
 *
 */
package ch.unizh.ori.common.text.helper;

/**
 * @author pht
 *
 */
public class SwahiliHelper {
	
	public static String ripThings(String s){
		if(s.startsWith("N")){
			s = s.substring(1);
		}
		if("9/10".equals(s)){
			s = "9";
		}
		return s;
	}
	
}
