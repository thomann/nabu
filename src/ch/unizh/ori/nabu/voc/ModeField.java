/*
 * Created on 02.04.2004
 */
package ch.unizh.ori.nabu.voc;

import java.util.Map;

import ch.unizh.ori.common.text.PlotterPresentation;
import ch.unizh.ori.common.text.Presentation;
import ch.unizh.ori.nabu.core.DefaultDescriptable;

/**
 * @author pht
 */
public class ModeField extends DefaultDescriptable implements Cloneable {
	
	private boolean asking;
	private String key;
	private String label;
	private String type;
	
	private Presentation presentation;
	
	private Mode mode;
	
	private Column column;
	
	private Map question;
	private Object value;
	
	public ModeField copy(){
		ModeField m = new ModeField();
		m.asking = asking;
		m.key = key;
		m.label = label;
		m.type = type;
		m.question = question;
		m.value = value;
		m.column = column;
		return m;
	}
	
	protected Object clone(){
		return copy();
	}


	/**
	 * @return
	 */
	public boolean isAsking() {
		return asking;
	}

	/**
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return
	 */
	public String getLabel() {
		if (label == null) {
			label = getColumn().getName();
		}
		return label;
	}
	
	public String getName() {
		String name = super.getName();
		if (name == null) {
			name = ( (isAsking())?"?":"" )+getColumn().getName();
			super.setName(name);
		}
		return name;
	}


	/**
	 * @return
	 */
	public Map getQuestion() {
		return question;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param b
	 */
	public void setAsking(boolean b) {
		asking = b;
	}

	/**
	 * @param string
	 */
	public void setKey(String string) {
		key = string;
	}

	/**
	 * @param string
	 */
	public void setLabel(String string) {
		label = string;
	}

	/**
	 * @param map
	 */
	public void setQuestion(Map map) {
		question = map;
		if(question == null){
			value = null;
		}else{
			value = map.get(key);
		}
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * @return
	 */
	public Column getColumn() {
		return column;
	}

	/**
	 * @param column
	 */
	public void setColumn(Column column) {
		this.column = column;
	}
	
	/**
	 * @return
	 */
	public Mode getMode() {
		return mode;
	}
	
	public boolean isImage(){
		if( getColumn() instanceof ImgColumn)
			return true;
		return getPresentation() instanceof PlotterPresentation;
	}

	/**
	 * @param mode
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Presentation getPresentation() {
		return presentation;
	}

	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
	}
	
}
