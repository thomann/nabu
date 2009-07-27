/*
 * Created on 09.04.2004
 */
package ch.unizh.ori.nabu.voc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author pht
 */
public abstract class AbstractFileSource extends Source {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(AbstractFileSource.class);

	protected List fieldstreams;

	public List readLections(URL base) throws UnsupportedEncodingException, IOException {
		fieldstreams = new ArrayList();
		URL url = new URL(base, getSrc());
		log.info(url);
		BufferedReader in =
			new BufferedReader(
				new InputStreamReader(url.openStream(), getEnc()));
		read(in);
		return fieldstreams;
	}

	protected abstract void read(BufferedReader in) throws IOException;

}
