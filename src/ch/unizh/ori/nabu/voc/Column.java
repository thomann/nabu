/*
 * Created on 31.03.2004
 */
package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.Central;
import ch.unizh.ori.nabu.core.Descriptable;

/**
 * @author pht
 */
public interface Column extends Descriptable {

	public Object map(String[] arr);
	public int getColumn();
	public void setColumn(int column);
	public void setVoc(Vocabulary voc);
	public Vocabulary getVoc();
	public void setCentral(Central central);
	public Central getCentral();

}
