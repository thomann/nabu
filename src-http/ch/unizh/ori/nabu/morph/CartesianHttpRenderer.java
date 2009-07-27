/*
 * CartesianHttpRenderer.java
 *
 * Created on 25. September 2002, 17:32
 */

package ch.unizh.ori.nabu.morph;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import ch.unizh.ori.nabu.ui.http.HttpRenderer;

/**
 *
 * @author  pht
 */
public abstract class CartesianHttpRenderer implements HttpRenderer {
    
    private CartesianMorphQuestionProducer producer;
    
    /** Creates a new instance of CartesianHttpRenderer */
    public CartesianHttpRenderer(CartesianMorphQuestionProducer producer) {
        this.producer = producer;
    }
    
    
    public String[] getCoordinateNames(){
        return producer.getCoordinateNames();
    }
    
    public String getCoordinateName(int i){
        return getCoordinateNames()[i];
    }

    public String[][] getSpace(){
        return producer.getSpace();
    }
    
    public String getCoordinateLabel(int i){
        return producer.getCoordinateLabel(i);
    }
    
    public String getValueLabel(int i, int j){
        return producer.getValueLabel(i, j);
    }
    
    public String[] getCoordinates(){
        return ((CartesianMorphQuestion)getQuestion()).getCoords();
    }
    
    /**
	 * @return
	 */
	private static CartesianMorphQuestion getQuestion() {
		return null;
	}


	public String getCoordinate(int i){
        return getCoordinates()[i];
    }
    
    public String getRoot(){
        return ((CartesianMorphQuestion)getQuestion()).getRoot();
    }
    
    public String getForm(){
        return ((CartesianMorphQuestion)getQuestion()).getForm();
    }
    
    public static class Analyse extends CartesianHttpRenderer{
        
        private Answer answer = new Answer(null, new HashMap());
        
        public Analyse(CartesianMorphQuestionProducer producer){
            super(producer);
        }

        public void copyAnswer() {
            CartesianMorphQuestion q = (CartesianMorphQuestion)getQuestion();
            answer.ansRoot = q.getRoot();
            for(int i=getCoordinateNames().length-1; i>=0; i--){
                answer.ansCoord.put(getCoordinateName(i), q.getCoord(i));
            }
        }
        
        public void clear() {
            answer.ansRoot = null;
            answer.ansCoord.clear();
        }

        public void populateFromRequest(HttpServletRequest request){
            String newAnsRoot = request.getParameter("ansRoot");
            answer.ansRoot = (newAnsRoot==null)?"":newAnsRoot;
            
            for(int i=getCoordinateNames().length-1; i>=0; i--){
                String n = getCoordinateName(i);
                String nc = request.getParameter("ans_"+n);
                nc = (nc==null)?"":nc;
                answer.ansCoord.put(n, nc);
            }
        }

        public String getJspPath() {
            return "/WEB-INF/renderers/CartesianAnalyseRenderer.jsp";
        }
        
        public boolean isCorrect(Object question) {
            CartesianMorphQuestion q = (CartesianMorphQuestion)question;
            if( !q.getRoot().equals(getRoot()) )
                return false;
            for(int i=getCoordinateNames().length-1; i>=0; i--){
                String c = getCoordinateName(i);
                String a = (String)answer.ansCoord.get(c);
                if( c==null || !a.equals(q.getCoord(i)) )
                    return false;
            }
            return true;
        }
        
        public String getAnsRoot(){
            return answer.ansRoot;
        }
        
        public String getAnsCoord(String coordName){
            return (String)answer.ansCoord.get(coordName);
        }
        
        public String getFocusKey(){
            return "ansRoot";
        }
        
        /** Getter for property answer.
         * @return Value of property answer.
         */
        public Object getAnswer() {
            return answer.copy();
        }

        public static class Answer{
            private String ansRoot;
            private Map ansCoord;
            
            public Answer(String ansRoot, Map ansCoord){
                this.ansRoot = ansRoot;
                this.ansCoord = ansCoord;
            }
            
            public Answer(Answer a){
                this.ansRoot = a.ansRoot;
                this.ansCoord = a.ansCoord;
             }
            
            public boolean equals(Object o){
                if(!(o instanceof Answer))
                    return false;
                Answer a = (Answer)o;
                if(!(ansRoot==null) ? a.ansRoot==null:ansRoot.equals(a.ansRoot))
                    return false;
                return (ansCoord==null) ? a.ansCoord==null:ansCoord.equals(a.ansCoord);
            }
            
            public Answer copy(){
                return new Answer(ansRoot, new HashMap(ansCoord));
            }

        }

		public boolean isCorrect() {
			return false;
		}

		public void processRequest(HttpServletRequest request, boolean showSolution) {
		}

		public void setQuestion(Object q) {
		}

		public boolean isShowSolution() {
			return false;
		}

		public void afterTest() {
		}

    }
    
    public static class Construct extends CartesianHttpRenderer{
        
        public Construct(CartesianMorphQuestionProducer producer){
            super(producer);
        }

        /** Holds value of property answer. */
        private Object answer = null;

        public String getJspPath() {
            return "/WEB-INF/renderers/CartesianConstructRenderer.jsp";
        }

        public void populateFromRequest(HttpServletRequest request){
            setAnswer(request.getParameter("ans"));
        }

        public void clear() {
            setAnswer(null);
        }

        public void copyAnswer(){
            setAnswer(getQuestion().toString());
        }
        
        /** Getter for property answer.
         * @return Value of property answer.
         */
        public Object getAnswer() {
            return this.answer;
        }

        /** Setter for property answer.
         * @param ans New value of property answer.
         */
        public void setAnswer(Object answer) {
            this.answer = answer;
        }

		public boolean isCorrect() {
			return false;
		}

		public void processRequest(HttpServletRequest request, boolean showSolution) {
		}

		public String getFocusKey() {
			return null;
		}

		public void setQuestion(Object q) {
		}

		public boolean isShowSolution() {
			return false;
		}

		public void afterTest() {
		}

    }
    
}
