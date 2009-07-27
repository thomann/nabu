/*
 * TestMorphQuestion.java
 *
 * Created on 26. August 2002, 06:40
 */

package ch.unizh.ori.nabu.morph;

import ch.unizh.ori.nabu.core.QuestionIterator;

/**
 *
 * @author  pht
 */
public class TestMorphQuestion {
    
    private String q;
    private String a;
    
    /** Creates a new instance of TestMorphQuestion */
    public TestMorphQuestion(String q, String a) {
        this.q = q;
        this.a = a;
    }
    
    public String getQuestionString() {
        return q;
    }
    
    public boolean isCorrect(Object answer) {
        return a.equals(answer);
    }
    
    public String getAnswerString() {
        return a;
    }
    
    public void finishPass(boolean answerWasCorrect, QuestionIterator qi) {
    }
    
}
