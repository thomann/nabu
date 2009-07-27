/*
 * TestMorphQuestionProducer.java
 *
 * Created on 26. August 2002, 06:40
 */

package ch.unizh.ori.nabu.morph;

import ch.unizh.ori.nabu.core.QuestionProducer;

/**
 *
 * @author  pht
 */
public class TestMorphQuestionProducer extends QuestionProducer {
    
    private static final String[] roots = { "domin", "equ", "serv" };
    private static final String[] suff = { "us", "um", "i", "o", "o", "i", "os", "orum", "is", "is" };
    private static final String[] q = { "1. sg", "2. sg", "3. sg", "1. pl", "2. pl", "3. pl"  };
    
    /** Creates a new instance of TestMorphQuestionProducer */
    public TestMorphQuestionProducer() {
    }
    
    public Object produceNext() {
        int r = (int) Math.floor(Math.random() * roots.length);
        int s = (int) Math.floor(Math.random() * suff.length);
        return new TestMorphQuestion(q[s]+" von "+roots[r]+"us", roots[r] + suff[s]);
    }
    
    public boolean isList() {
        return false;
    }
    
    public int countQuestions() {
        throw new IllegalArgumentException("Should not be called");
    }
    
    public void initSession() {
    }

	public void finishSession() {
	}
    
}
