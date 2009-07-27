/*
 * Text.java
 *
 * Created on 25. September 2002, 10:40
 */

package ch.unizh.ori.common.text;

/**
 *
 * @author  pht
 */
public interface OldText extends java.io.Serializable {
    
    public String getUnicodeString();
    public String getAsciiString();
    public String getImageURL();
    
    public OldScript getScript();
    
    public OldText getTransliteration();
    
}
