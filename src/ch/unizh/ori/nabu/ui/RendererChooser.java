/*
 * RendererChooser.java
 *
 * Created on 17. Februar 2003, 11:23
 */

package ch.unizh.ori.nabu.ui;

import ch.unizh.ori.nabu.core.*;

/**
 *
 * @author  pht
 */
public abstract class RendererChooser {
    
    /** Holds value of property lastAnswer. */
    private Object lastAnswer;
    
    /** Holds value of property firstAnswer. */
    private boolean firstAnswer;
    
    public abstract Renderer chooseRenderer(QuestionIterator iter);
    
    /** Getter for property lastAnswer.
     * @return Value of property lastAnswer.
     */
    public Object getLastAnswer() {
        return this.lastAnswer;
    }
    
    /** Setter for property lastAnswer.
     * @param lastAnswer New value of property lastAnswer.
     */
    public void setLastAnswer(Object lastAnswer) {
        this.lastAnswer = lastAnswer;
    }
    
    /** Getter for property firstAnswer.
     * @return Value of property firstAnswer.
     */
    public boolean isFirstAnswer() {
        return this.firstAnswer;
    }
    
    /** Setter for property firstAnswer.
     * @param firstAnswer New value of property firstAnswer.
     */
    public void setFirstAnswer(boolean firstAnswer) {
        this.firstAnswer = firstAnswer;
    }
    
}
