/*
 * Created on Aug 24, 2004
 *
 */
package ch.unizh.ori.nabu.voc;

import java.util.Map;
import java.util.regex.Pattern;

import ch.unizh.ori.nabu.core.DefaultDescriptable;

/**
 * @author pht
 *
 */
public class Filter extends DefaultDescriptable {
	
	private Mode mode;
	
	private String field;
	private String starts = null;
	private String ends = null;
	private String contains = null;
	private String pattern = null;
	private Pattern thePattern = null;
	private String except = null;
	private Pattern theExceptPattern = null;
	
	public boolean accept(Map voc){
		if(field == null){
			throw new NullPointerException("Field is null!");
		}
		String m = (String) voc.get(field);
		if(starts != null && !m.startsWith(starts)){
			return false;
		}
		
		if(ends != null && !m.endsWith(ends)){
			return false;
		}
		
		if(contains != null && m.indexOf(contains)<0){
			return false;
		}
		
		if(thePattern != null && !thePattern.matcher(m).matches()){
			return false;
		}
		
		if(theExceptPattern != null && theExceptPattern.matcher(m).matches()){
			return false;
		}
		
		return true;
	}

	/**
	 * @return Returns the contains.
	 */
	public String getContains() {
		return contains;
	}
	/**
	 * @param contains The contains to set.
	 */
	public void setContains(String contains) {
		this.contains = contains;
	}
	/**
	 * @return Returns the ends.
	 */
	public String getEnds() {
		return ends;
	}
	/**
	 * @param ends The ends to set.
	 */
	public void setEnds(String ends) {
		this.ends = ends;
	}
	/**
	 * @return Returns the field.
	 */
	public String getField() {
		return field;
	}
	/**
	 * @param field The field to set.
	 */
	public void setField(String field) {
		this.field = field;
	}
	/**
	 * @return Returns the mode.
	 */
	public Mode getMode() {
		return mode;
	}
	/**
	 * @param mode The mode to set.
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	/**
	 * @return Returns the starts.
	 */
	public String getStarts() {
		return starts;
	}
	/**
	 * @param starts The starts to set.
	 */
	public void setStarts(String starts) {
		this.starts = starts;
	}
	/**
	 * @return Returns the pattern.
	 */
	public String getPattern() {
		return pattern;
	}
	/**
	 * @param pattern The pattern to set.
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
		if(pattern != null){
			thePattern = Pattern.compile(pattern);
		}else{
			thePattern = null;
		}
	}
	/**
	 * @return Returns the except.
	 */
	public String getExcept() {
		return except;
	}
	/**
	 * @param except The except to set.
	 */
	public void setExcept(String except) {
		this.except = except;
		if(except != null){
			theExceptPattern = Pattern.compile(except);
		}else{
			theExceptPattern = null;
		}
	}
}
