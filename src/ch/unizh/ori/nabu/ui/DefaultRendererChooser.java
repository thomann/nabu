/*
 * DefaultRendererChooser.java
 *
 * Created on 17. Februar 2003, 11:25
 */

package ch.unizh.ori.nabu.ui;

import ch.unizh.ori.nabu.core.*;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author  pht
 */
public class DefaultRendererChooser extends RendererChooser implements java.io.Serializable {
    
    /** Holds value of property rendererMap. */
    private Map rendererMap = new HashMap();
    
    /** Creates a new instance of DefaultRendererChooser */
    public DefaultRendererChooser() {
    }
    
    /** Getter for property rendererMap.
     * @return Value of property rendererMap.
     */
    public Map getRendererMap() {
        return this.rendererMap;
    }
    
    /** Setter for property rendererMap.
     * @param rendererMap New value of property rendererMap.
     */
    public void setRendererMap(Map rendererMap) {
        this.rendererMap = rendererMap;
    }
    
    public void put(Object key, Object renderer){
        getRendererMap().put(key, renderer);
    }
    
    public Renderer chooseRenderer(QuestionIterator iter) {
        Renderer r = (Renderer)getRendererMap().get(iter.getProducer());
        if(r == null){
            Object q = iter.getQuestion();
            if(q != null){
                r = (Renderer)getRendererMap().get(q.getClass());
            }
        }
        return r;
    }
    
}
