/*
 * Created on 31.03.2004
 */
package ch.unizh.ori.nabu.voc;

import java.io.Serializable;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author pht
 */
public abstract class Source implements Serializable {
	
	private String src, enc, grouping;
	
	/**
	 * Filename of the corresponding file.
	 * Relative to the base of the vocabulary.
	 * May be <code>null</code> (eg. if data is in DB).
	 */
	private String filename = null;
	
	private String header;
	
	public abstract List readLections(URL base) throws Exception;

	/**
	 * @return
	 */
	public String getEnc() {
		return enc;
	}

	/**
	 * @return
	 */
	public String getGrouping() {
		return grouping;
	}

	/**
	 * @return
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * @param string
	 */
	public void setEnc(String string) {
		enc = string;
	}

	/**
	 * @param string
	 */
	public void setGrouping(String string) {
		grouping = string;
	}

	/**
	 * @param string
	 */
	public void setSrc(String string) {
		src = string;
	}

	protected String label;

	protected MessageFormat[] fmts;

	protected String createLessonName(int i, String name) {
		createFmts();
		if(label == null){
			return "Lesson "+name;
		}else{
			Object[] args = {name};
			return fmts[Math.min(i, fmts.length-1)].format(args);
		}
	}

	protected void createFmts() {
		if(label == null){
			return;
		}
		String[] arr = label.split(",");
		if(arr.length == 0){
			label = null;
			return;
		}
		fmts = new MessageFormat[arr.length];
		for(int i=0; i<fmts.length; i++){
			fmts[i] = new MessageFormat(arr[i]);
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String string) {
		label = string;
	}
	
	public void addHeaderString(String string){
		if(header == null || (header.equals(""))) {
			setHeader(string);
		} else {
			setHeader(getHeader() + "\n" + string);
		}
		Logger.getLogger(Source.class).debug("Header: "+getHeader());
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
