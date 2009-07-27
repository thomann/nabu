/*
 * Created on Oct 16, 2004
 *
 */
package ch.unizh.ori.common.text.helper;

import ch.unizh.ori.common.text.ScriptUtilities;

/**
 * @author pht
 *
 */
public class ThaiHelper {
	
	public static String VOWELS = "aiWueEoOY";
	public static String CONS = "kcdtpbhyfslgnmw?";
	public static String TONES = "1234";
	public static char[] ACCENTS = {'\u0300','\u0302','\u0301','\u030c',};
	
	public static String ASCII_CHARS = "WEYO?";
	public static String IPA_CHARS = "\u026F\u025B\u0264\u0254\u0294";
	
	public static String convertPronunciation(String in){
		StringBuffer ret = new StringBuffer(in.length());
		boolean inV = false;
		int startV = -1;
		for (int i = 0; i < in.length(); i++) {
			char ch = in.charAt(i);
			if(VOWELS.indexOf(ch)>=0){
				if(!inV){
					startV = i;
				}
				inV = true;
				ret.append(ch);
				continue;
			}
			inV = false;
			int tone = TONES.indexOf(ch);
			if(tone>=0 && startV >= 0){
				// for now we put the accent on the first vowel
				char acc = ACCENTS[tone];
				if(ret.charAt(startV)=='i'){
					ret.setCharAt(startV, '\u0131');
				}
				ret.insert(startV+1,acc);
//				if(in.charAt(offset) == 'i'){
//					ret.setCharAt(offset, '\u0131');
//				}
				startV = -1;
				continue;
			}
			
			if(CONS.indexOf(ch)<0){
				startV = -1;
			}
			ret.append(ch);
		}
		String str = ret.toString();
		for(int i=0; i<ASCII_CHARS.length(); i++)
			str = str.replace(ASCII_CHARS.charAt(i), IPA_CHARS.charAt(i));
		return ScriptUtilities.compose(str);
	}

}
