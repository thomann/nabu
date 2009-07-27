/*
 * Created on 03.04.2004
 */
package ch.unizh.ori.nabu.voc;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author pht
 */
public class FileLectionSource extends AbstractFileSource {
	
	private int column = -1;

	protected void read(BufferedReader in) throws IOException {
		Map map = new TreeMap();
		List keyList = new ArrayList();
		for(String line=in.readLine(); line != null; line=in.readLine()){
			String[] fields = line.split("\t");
			int column;
			String key = lesson(fields);
			List voc = (List) map.get(key);
			if(voc == null){
				voc = new ArrayList();
				map.put(key, voc);
				keyList.add(key);
			}
			voc.add(fields);
		}
		in.close();
		fieldstreams = new ArrayList(keyList.size());
		int i=0;
		for (Iterator iter = keyList.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			List voc = (List) map.get(key);
			ListFieldStream fs = new ListFieldStream(voc);
			fs.setId(key);
			fs.setName(createLessonName(i++, key));
			fieldstreams.add(fs);
		}
	}

	protected String lesson(String[] fields) {
		int column = this.column;
		if(column < 0){
			column += fields.length;
		}
		if(column < 0 || column >= fields.length){
			column = fields.length-1;
		}
		String key = fields[column];
		return key;
	}

	/**
	 * @param i
	 */
	public void setColumn(String str) {
		column = Integer.parseInt(str);
	}

}
