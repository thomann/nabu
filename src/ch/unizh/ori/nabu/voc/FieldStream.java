/*
 * Created on Sep 17, 2004
 *
 */
package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.Descriptable;

/**
 * @author pht
 *
 */
public interface FieldStream extends Descriptable{
	public abstract Object start() throws Exception;

	public abstract int getCount();

	public abstract String[] next(Object param) throws Exception;

	public abstract void stop(Object param) throws Exception;
}