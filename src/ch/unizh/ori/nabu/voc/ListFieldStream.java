/*
 * Created on 08.04.2004
 */
package ch.unizh.ori.nabu.voc;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author pht
 */
public class ListFieldStream extends AbstractFieldStream {
	
	private Collection items;
	
	public ListFieldStream(Collection items){
		this.items = items;
	}

	public Object start() throws Exception {
		return items.iterator();
	}

	public String[] next(Object param) throws Exception {
		Iterator iter = (Iterator) param;
		if(iter.hasNext()){
			return (String[])iter.next();
		}else{
			return null;
		}
	}

	public void stop(Object param) throws Exception {
		Iterator iter = (Iterator) param;
		iter = null;
	}

	public int getCount() {
		if(items == null){
			return 0;
		}
		return items.size();
	}
	
}
