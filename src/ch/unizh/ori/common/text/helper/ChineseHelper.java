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
public class ChineseHelper {
	
	public static String VOWELS = "aeouv\u00fci";
	public static String CONS = "bcdfghjklmnpqrstvwxyz";
	public static String TONES = "1234";
	public static char[] ACCENTS = {'\u0304','\u0301','\u030c','\u0300',};
	
	public static String convertPinyin(String in){
		StringBuffer ret = new StringBuffer(in.length());
		boolean inV = false;
		int startV = -1;
		int endV = -1;
		for (int i = 0; i < in.length(); i++) {
			char ch = in.charAt(i);
			int v = vowelWeight(ch);
			if(v >= 0){
				endV = i;
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
				int offset = endV;
				if(startV != endV){
					// we aren't doing my algorithm, but wikipedias...
					if (false) {
						int weight = vowelWeight(in.charAt(endV));
						for (int j = endV - 1; j >= startV; j--) {
							int weight2 = vowelWeight(in.charAt(j));
							if (weight2 <= weight) {
								offset = j;
								weight = weight2;
							}
						}
					}else{
						offset = startV;
						if("iuv\u00fc".indexOf(in.charAt(startV)) >= 0){
							offset++;
						}
					}
				}
				ret.insert(offset+1,acc);
//				if(in.charAt(offset) == 'i'){
//					ret.setCharAt(offset, '\u0131');
//				}
				startV = endV = -1;
				continue;
			}
			
			if(CONS.indexOf(ch)<0){
				startV = endV = -1;
			}
			ret.append(ch);
		}
		return ScriptUtilities.compose(ret.toString().replace('v', '\u00fc'));
	}

	private static int vowelWeight(char ch) {
		ch = Character.toLowerCase(ch);
		return VOWELS.indexOf(ch);
	}

}
