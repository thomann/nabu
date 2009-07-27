/*
 * DefaultText.java
 *
 * Created on 17. Februar 2003, 14:54
 */

package ch.unizh.ori.common.text;


/**
 *
 * @author  pht
 */
public abstract class DefaultText implements OldText {
    
    /** Holds value of property script. */
    private OldScript script;
    
    public String getImageURL(){
        return OldStringText.getImageURL(getUnicodeString());
    }
    
    /** Getter for property script.
     * @return Value of property script.
     */
    public OldScript getScript() {
        return this.script;
    }
    
    /** Setter for property script.
     * @param script New value of property script.
     */
    public void setScript(OldScript script) {
        this.script = script;
    }
    
    public abstract String getAsciiString();
    
    public abstract OldText getTransliteration();
    
    public abstract String getUnicodeString();
    
}
