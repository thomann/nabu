/*
 * Created on Jun 20, 2004
 *
 */
package ch.unizh.ori.nabu.voc;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author pht
 *
 */
public class LineSndColumn extends SndColumn {
	
	private String patternString;
	private MessageFormat mf;
	
	private int idFill = 3;
	private int lessonFill = 1;
	
	public String getUtterance(String toSay, Map question) {
		String ret = null;
		Object[] par = {
				fill(question.get("lesson"), lessonFill), fill(question.get("id"), idFill)
		};
		if(par[0] != null && par[1] != null){
			if(messageFormat() != null){
				ret = messageFormat().format(par);
			}
		}
		return ret;
	}
	
	public static String fill(Object o, int i){
		String str = (o==null)?"": o.toString();
		switch (i-str.length()) {
			case 1 :
				return "0"+str;
			case 2:
				return "00"+str;
			case 3:
				return "000"+str;
			case 4:
				return "0000"+str;
			case 5:
				return "00000"+str;
			default :
				if(str.length()>=i){
					return str;
				}
				StringBuffer tmp = new StringBuffer();
				for(int j = i-str.length(); i>1; i--){
					tmp.append('0');
				}
				return tmp+str;
		}
	}
	
	private MessageFormat messageFormat(){
		if (mf == null && patternString != null) {
			mf = new MessageFormat(patternString);
		}
		return mf;
	}

	/**
	 * @return Returns the pattern.
	 */
	public String getPattern() {
		return patternString;
	}
	/**
	 * @param pattern The pattern to set.
	 */
	public void setPattern(String pattern) {
		this.patternString = pattern;
	}
	public int getIdFill() {
		return idFill;
	}
	public void setIdFill(int idFill) {
		this.idFill = idFill;
	}
	public int getLessonFill() {
		return lessonFill;
	}
	public void setLessonFill(int lessonFill) {
		this.lessonFill = lessonFill;
	}
}
