/*
 * QuestionProducer.java
 *
 * Created on 23. August 2002, 19:35
 */

package ch.unizh.ori.nabu.core;

/**
 *
 * @author  pht
 */
public abstract class QuestionProducer implements java.io.Serializable {
    
    /** Creates a new instance of QuestionProducer */
    public QuestionProducer() {
    }
    
	public abstract void initSession();
	public abstract void finishSession();
    
    public abstract Object produceNext();
    
    public abstract boolean isList();
    
    public abstract int countQuestions();
    
}
