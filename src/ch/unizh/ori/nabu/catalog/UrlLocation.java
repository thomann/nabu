/*
 * Created on 10.03.2004
 */
package ch.unizh.ori.nabu.catalog;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pht
 */
public abstract class UrlLocation {
	
	protected URL baseUrl;
	
	
	/**
	 * Returns an <code>List</code> of <code>URL</code>'s.
	 * @param suffix
	 * @return
	 */
	public abstract List locations(String suffix) throws MalformedURLException, IOException;
	
	protected List locations(String[] files) throws MalformedURLException{
		if(files == null) return null;
		List ret = new ArrayList(files.length);
		for (int i = 0; i < files.length; i++) {
			ret.add(new URL(baseUrl, files[i]));
		}
		return ret;
	}
	
//	public URL location(String url) throws MalformedURLException{
//		return new URL(baseUrl, url);
//	}
	
	public static UrlLocation create(URL baseUrl) throws MalformedURLException{
		UrlLocation ul = null;
		if("jar".equals(baseUrl.getProtocol())){
			ul = new JarUrlLocation();
		}else if(baseUrl.getFile().endsWith(".jar")){
			ul = new JarUrlLocation();
			baseUrl = new URL("jar:"+baseUrl+"!/");
		}else{
			ul = new FileUrlLocation();
		}
		
		if(ul != null){
			ul.baseUrl = baseUrl;
		}
		return null;
	}

}
