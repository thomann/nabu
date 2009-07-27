/*
 * Renderer.java
 *
 * Created on 13. April 2003, 07:33
 */

package ch.unizh.ori.nabu.ui;

/**
 *
 * @author  pht
 */
public interface Renderer extends java.io.Serializable {
    
    public boolean isCorrect();
    
    public void setQuestion(Object q);
    
//    public static final int ACTION_ANOTHER_TRY = 1;
//    public static final int ACTION_SHOW_SOLUTION = 2;
//    public static final int ACTION_NEXT = 3;
//    
//    public int getAction();

	public boolean isShowSolution();
    
    public void clear();

}
