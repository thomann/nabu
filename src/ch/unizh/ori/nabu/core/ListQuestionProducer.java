/*
 * Created on 02.04.2004
 */
package ch.unizh.ori.nabu.core;

import java.util.Collection;
import java.util.Iterator;
;

/**
 * @author pht
 */
public class ListQuestionProducer extends QuestionProducer {
	
	private Collection coll;
	private transient Iterator iter = null;
	private int count = 0;
	
	public ListQuestionProducer(Collection coll){
		this.coll = coll;
	}

	public void initSession() {
		iter = coll.iterator();
		count = coll.size();
	}

	public Object produceNext() {
		if(iter.hasNext()){
			count --;
			return iter.next();
		}else{
			return null;
		}
	}

	public boolean isList() {
		return true;
	}

	public int countQuestions() {
		return count;
	}

	public void finishSession() {
		iter = null;
		count = 0;
	}

}
