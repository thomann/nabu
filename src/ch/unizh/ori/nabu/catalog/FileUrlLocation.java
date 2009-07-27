/*
 * Created on 10.03.2004
 */
package ch.unizh.ori.nabu.catalog;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

/**
 * @author pht
 */
public class FileUrlLocation extends UrlLocation {

	public List locations(final String suffix) throws MalformedURLException {
		File dir = new File(baseUrl.getFile());
		if(!dir.isDirectory()){
			try {
				return Collections.singletonList(dir.toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}else{
			String[] files = dir.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(suffix);
				}
			});
			return locations(files);
		}
		return null;
	}

}
