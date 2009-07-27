/*
 * Statistics.java
 *
 * Created on 28. September 2002, 21:48
 */

package ch.unizh.ori.nabu.stat;

import java.text.MessageFormat;

import ch.unizh.ori.nabu.core.*;

/**
 *
 * @author  pht
 */
public class Statistics implements java.io.Serializable {
    
    /** Holds value of property asked. */
    private int asked = 0;
    
    /** Holds value of property total. */
    private int total = -1;
    
    private int toAsk;
    private int problems = 0;
    private int completed = 0;
    
    private int tries = 0;
    private long lastStart;
    private long totalTime = 0;
    
    /** Holds value of property iter. */
    private QuestionIterator iter;
    
    /** Creates a new instance of Statistics */
    public Statistics() {
    }
    
    /** Creates a new instance of Statistics */
    public Statistics(QuestionIterator iter) {
        this();
        setIter(iter);
    }
    
    public void startQuestion(){
    	lastStart = System.currentTimeMillis();
    }
    
    public void unsuccessfulTry(){
    	tries++;
    }
    
    public void finishQuestion(boolean correct, int prevProblems){
    	totalTime += (System.currentTimeMillis() - lastStart);
    	asked++;
    	if(correct){
			toAsk--;
    		if(prevProblems <= 1){
    			completed++;
    			if(prevProblems==1){
    				problems--;
    			}
    		}
    	}else{
    		if(prevProblems<=0){
    			toAsk += 3;
    			problems ++;
    		}else{
    			toAsk++;
    		}
    	}
    }
    
    private MessageFormat messageFormat;
    public String toString() {
		if(messageFormat == null){
			messageFormat = new MessageFormat("total: {0}, asked: {1}, completed: {2}, " +				"toAsk: {3}, problems: {4} " +				"- totalTime: {5}, perAsked: {6}, perCompleted: {7}");
		}
		Object[] messages = {
			getTotal()+"", getAsked()+"", getCompleted()+"",
			getToAsk()+"", getProblems()+"",
			getTotalTime()/1000+"", getTimePerAsked()/1000+"", getTimePerCompleted()/1000+"" 
		};
		return messageFormat.format(messages);
	}

    
    /** Getter for property asked.
     * @return Value of property asked.
     */
    public int getAsked() {
        return this.asked;
    }
    
    /** Getter for property total.
     * @return Value of property total.
     */
    public int getTotal() {
        return this.total;
    }
    
    /** Setter for property total.
     * @param total New value of property total.
     */
    public void setTotal(int total) {
        this.total = total;
        toAsk = total;
    }
    
    /** Getter for property iter.
     * @return Value of property iter.
     */
    public QuestionIterator getIter() {
        return this.iter;
    }
    
    /** Setter for property iter.
     * @param iter New value of property iter.
     */
    public void setIter(QuestionIterator iter) {
        this.iter = iter;
        setTotal(iter.countQuestions()+1);
    }
    
	/**
	 * @return
	 */
	public int getCompleted() {
		return completed;
	}

	/**
	 * @return
	 */
	public long getLastStart() {
		return lastStart;
	}

	/**
	 * @return
	 */
	public int getProblems() {
		return problems;
	}

	/**
	 * @return
	 */
	public int getToAsk() {
		return toAsk;
	}

	/**
	 * @return
	 */
	public long getTotalTime() {
		return totalTime;
	}

	/**
	 * @return
	 */
	public int getTries() {
		return tries;
	}

	/**
	 * @param i
	 */
	public void setToAsk(int i) {
		toAsk = i;
	}
	
	public long getTimePerCompleted(){
		if(completed==0){
			return 0;
		}
		return getTotalTime() / completed;
	}

	public long getTimePerAsked(){
		if(asked==0){
			return 0;
		}
		return getTotalTime() / asked;
	}

}
