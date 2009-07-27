/*
 * CartesianAnalyseRenderer.java
 *
 * Created on 25. September 2002, 17:32
 */

package ch.unizh.ori.nabu.morph;

import javax.servlet.http.HttpServletRequest;

import ch.unizh.ori.nabu.ui.http.HttpRenderer;

/**
 *
 * @author  pht
 */
public class CartesianAnalyseRenderer implements HttpRenderer {
    
    private CartesianMorphQuestionProducer producer;
    
    private Object answer;
    
    /** Creates a new instance of CartesianAnalyseRenderer */
    public CartesianAnalyseRenderer(CartesianMorphQuestionProducer producer) {
        this.producer = producer;
    }
    
    public String getJspPath() {
        return "/WEB-INF/renderers/CartesianAnalyseRenderer.jsp";
    }
    
    public void populateFromRequest(HttpServletRequest request){
        String ans = request.getParameter("ans");
        setAnswer( (ans == null) ? "" : ans );
    }
    
    /** Getter for property answer.
     * @return Value of property answer.
     */
    public Object getAnswer() {
        return answer;
    }
    
    /** Setter for property answer.
     * @param answer New value of property answer.
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

	public void clear() {
	}
    
}
