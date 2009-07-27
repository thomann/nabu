/*
 * AbstractFieldStream.java
 *
 * Created on 25. August 2003, 07:23
 */

package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.DefaultDescriptable;
import java.util.*;

/**
 * 
 * @author pht
 */
public abstract class AbstractFieldStream extends DefaultDescriptable implements
		FieldStream {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(AbstractFieldStream.class);

	public abstract Object start() throws Exception;

	public abstract int getCount();

	public abstract String[] next(Object param) throws Exception;

	public abstract void stop(Object param) throws Exception;

	public Collection getCollection() throws Exception {
		return new SourceCollectionAndIterator(this);
	}

	/*
	 * **************************************************************************
	 * A helper Class...
	 * **********************************************************************
	 *  
	 */
	private static class SourceCollectionAndIterator extends AbstractCollection
			implements Iterator {

		private FieldStream fs;

		private Object param;

		private String[] next;

		public SourceCollectionAndIterator(FieldStream fs) throws Exception {
			this.fs = fs;
			param = fs.start();
		}

		public int size() {
			log.error("SourceCollectionAndIterator.size()");
			throw new RuntimeException("We don't implement this!");
		}

		public Iterator iterator() {
			return this;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public boolean hasNext() {
			if (next != null) {
				return true;
			} else {
				return nextIntern();
			}
		}

		private boolean nextIntern() {
			try {
				next = fs.next(param);
				if (next == null) {
					fs.stop(param);
					return false;
				}
				return true;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		public Object next() {
			if (hasNext()) {
				Object ret = next;
				next = null;
				return ret;
			} else {
				throw new NoSuchElementException();
			}
		}

	}

}