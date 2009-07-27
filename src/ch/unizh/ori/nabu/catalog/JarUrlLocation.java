/*
 * Created on 10.03.2004
 */
package ch.unizh.ori.nabu.catalog;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author pht
 */
public class JarUrlLocation extends UrlLocation {

	public List locations(String suffix) throws MalformedURLException, IOException {
		List ret = new ArrayList();
		JarURLConnection jarConnection = (JarURLConnection)baseUrl.openConnection();
		JarFile jarFile = jarConnection.getJarFile();
		String prefix = jarConnection.getEntryName();
		Enumeration enumeration = jarFile.entries();
		while (enumeration.hasMoreElements()) {
			JarEntry je = (JarEntry) enumeration.nextElement();
			String name = je.getName();
			if(name.startsWith(prefix) && name.endsWith(suffix)){
				ret.add(name);
			}
		}
		return ret;
	}

}
