/*
 * Created on 03.04.2004
 */
package ch.unizh.ori.nabu.voc;

import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * @author pht
 */
public class EmptySource extends Source {

	public List readLections(URL url) {
		return Collections.EMPTY_LIST;
	}

}
