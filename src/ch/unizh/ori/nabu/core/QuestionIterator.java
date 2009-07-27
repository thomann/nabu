/*
 * QuestionIterato.java
 *
 * Created on 13. April 2003, 07:37
 */

package ch.unizh.ori.nabu.core;

import ch.unizh.ori.nabu.stat.Statistics;
import ch.unizh.ori.nabu.ui.Renderer;

/**
 *
 * @author  pht
 */
public interface QuestionIterator {
    
    public Object getQuestion();
    public QuestionProducer getProducer();
    public Renderer getRenderer();
    public Statistics getStatistics();
    
    public void init();
    public void destroy();
    
    public boolean doEvaluate();
    
    public void next();
    
//    public int checkAnswer();
    
    public int countQuestions();
    public int countProblems();
    
    public int getTimesForProblem(Object question);
    public boolean isProblemsOnly();
    public void setProblemsOnly(boolean problemsOnly);
}
