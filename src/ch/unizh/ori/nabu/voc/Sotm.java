/*
 * Created on 24.11.2004
 *
 */
package ch.unizh.ori.nabu.voc;

import java.util.List;
import java.util.Map;

import ch.unizh.ori.nabu.core.Descriptable;

/**
 * @author pht
 *
 */
public interface Sotm extends Descriptable {

	public abstract String getUtterance(String toSay, Map question);

	public abstract List getVoices();
}