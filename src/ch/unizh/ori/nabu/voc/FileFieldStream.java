/*
 * Created on 31.03.2004
 */
package ch.unizh.ori.nabu.voc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import ch.unizh.ori.nabu.core.Utilities;

/**
 * @author pht
 */
public class FileFieldStream extends AbstractFieldStream {
	
	private URL url;
	private String enc;
	private String seperator;
	
	private int count = -1;
	
	public FileFieldStream(){
	}

	public Object start() throws Exception {
		BufferedReader in;
		if(enc == null){
			in = new BufferedReader(new InputStreamReader(url.openStream()));
		}else{
			in = new BufferedReader(new InputStreamReader(url.openStream(), enc));
		}
		return in;
	}


	public String[] next(Object param) throws Exception {
		BufferedReader in = (BufferedReader) param;
		String line = in.readLine();
		if(line == null){
			return null;
		}
		// is this really efficient?
		List dummy = Utilities.split(line, seperator, null);
		return (String[]) dummy.toArray(new String[dummy.size()]);
	}

	public void stop(Object param) throws Exception {
		BufferedReader in = (BufferedReader) param;
		in.close();
	}

	/**
	 * @return
	 */
	public String getEnc() {
		return enc;
	}

	/**
	 * @return
	 */
	public String getSeperator() {
		return seperator;
	}

	/**
	 * @return
	 */
	public URL getUrl() {
		return url;
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
	public void setSeperator(String string) {
		seperator = string;
	}

	/**
	 * @param url
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	/**
	 * @return
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param i
	 */
	public void setCount(int i) {
		count = i;
	}

}
