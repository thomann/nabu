/*
 * Created on 12.01.2004
 */
package ch.unizh.ori.common.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import ch.unizh.ori.nabu.core.DefaultDescriptable;

/**
 * @author pht
 */
public class DefaultTextScript extends DefaultScript implements TextScript {
	
	private List encodings = new ArrayList();
	private Map presentations = new TreeMap();
	private Map converters = new HashMap();
	
	private Locale locale;
	
	private String ex;
	private String exEnc;
	
	public DefaultTextScript(){
	}

	public DefaultTextScript(String id, List encodings, Map converters){
		setId(id);
		this.encodings = encodings;
		this.converters = converters;
	}

	public Object convert(String enc, Text text) {
		return getEncoding(enc).convert(text);
	}

	public Text create(String enc, Object param) {
		return getEncoding(enc).create(param);
	}
	
	public Presentation getPresentation(String id){
		return (Presentation) getPresentations().get(id);
	}

	public List getEncodings() {
		return encodings;
	}

	protected Encoding getEncoding(String enc){
		return (Encoding)converters.get(enc);
	}

	public Map getConverters() {
		return converters;
	}
	
	public String toString(){
		return getId()+"/"+getName()+": "+encodings+" / " + converters;
	}

	public void setConverters(Map map) {
		converters = map;
	}

	/**
	 * @param list
	 */
	public void setEncodings(List list) {
		encodings = list;
	}

	public String getEx() {
		return ex;
	}

	/**
	 * @return
	 */
	public String getExEnc() {
		return exEnc;
	}

	/**
	 * @param string
	 */
	public void setEx(String string) {
		ex = string;
	}

	/**
	 * @param string
	 */
	public void setExEnc(String string) {
		exEnc = string;
	}
	
	public void addEncoding(Encoding enc){
		String id = enc.getId();
		encodings.add(id);
		converters.put(id, enc);
	}

	public Map getPresentations() {
		return presentations;
	}

	public void setPresentations(Map presentations) {
		this.presentations = presentations;
	}

	public Object convert(Object in, String inEnc, String outEnc) {
		
		return null;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
