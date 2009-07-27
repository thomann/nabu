/*
 * CartesianMorphQuestion.java
 *
 * Created on 12. Oktober 2002, 06:40
 */

package ch.unizh.ori.nabu.morph;

import java.util.Arrays;

/**
 *
 * @author  pht
 */
public abstract class CartesianMorphQuestion {
    
    protected String root;
    protected String[] coords;
    protected String form;
    
    /** Creates a new instance of CartesianMorphQuestion */
    public CartesianMorphQuestion(String root, String[] coords, String form) {
        this.root = root;
        this.coords = coords;
        this.form = form;
    }
    
    public String getForm(){
        return form;
    }
    
    public String[] getCoords(){
        return coords;
    }
    
    public String getCoord(int i){
        return getCoords()[i];
    }
    
    public String getRoot(){
        return root;
    }
    
    public abstract Object getQuestion();
    
    public abstract String getQuestionString();
    
    public abstract boolean isCorrect(Object answer);
    
    public abstract String getAnswerString();
    
    public abstract Object getAnswer();
    
    /*public void finishPass(boolean answerWasCorrect, QuestionIterator qi) {
        qi.finishPass(this, answerWasCorrect);
    }*/
    
    
// **************************************************************************
// ****************************  Concrete Classes  **************************
// **************************************************************************
    
    public static class Analyse extends CartesianMorphQuestion{
        
        public Analyse(String root, String[] coords, String form) {
            super(root, coords, form);
        }
        
        public Object getAnswer() {
            return coords;
        }
        
        public String getAnswerString() {
            return Arrays.asList((Object[])getAnswer()).toString();
        }
        
        public Object getQuestion() {
            return form;
        }
        
        public String getQuestionString() {
            return (String)getQuestion();
        }
        
        public boolean isCorrect(Object answer) {
            if(!(answer instanceof String[])){
                return false;
            }
            return Arrays.equals(coords, (String[])answer);
        }
        
    }
    
    public static class Construct extends CartesianMorphQuestion {
        
        public Construct(String root, String[] coords, String form) {
            super(root, coords, form);
        }
        
        public Object getAnswer() {
            return form;
        }
        
        public String getAnswerString() {
            return (String)getAnswer();
        }
        
        public Object getQuestion() {
            return coords;
        }
        
        public String getQuestionString() {
            return root+": "+Arrays.asList((Object[])getQuestion()).toString();
        }
        
        public boolean isCorrect(Object answer) {
            return form.equals(answer);
        }
        
    }
    
}
