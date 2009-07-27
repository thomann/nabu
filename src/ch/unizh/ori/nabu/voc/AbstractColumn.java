/*
 * Created on 31.03.2004
 */
package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.Central;
import ch.unizh.ori.nabu.core.DefaultDescriptable;

/**
 * @author pht
 */
public abstract class AbstractColumn extends DefaultDescriptable implements Column {
	
	private Vocabulary voc;
	
	private Central central;
	private int column = -1;

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * @return
	 */
	public Central getCentral() {
		return central;
	}

	/**
	 * @param central
	 */
	public void setCentral(Central central) {
		this.central = central;
	}

	/**
	 * @return
	 */
	public Vocabulary getVoc() {
		return voc;
	}

	/**
	 * @param vocabulary
	 */
	public void setVoc(Vocabulary vocabulary) {
		voc = vocabulary;
	}

}
