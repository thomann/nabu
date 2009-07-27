/*
 * UnicodeScript.java
 *
 * Created on 22. Februar 2003, 21:05
 */

package ch.unizh.ori.common.text;

import java.util.*;
import java.sql.*;

/**
 *
 * @author  pht
 */
public class OldUnicodeScript extends OldScript {
    
    public static final OldScript SANSKRIT = new OldUnicodeScript(Conversion.SANSKRIT, "sanskrit");
    public static final OldScript ARABIC = new OldUnicodeScript("MacArabic", "arabic");
    public static final OldScript ACCADIAN = new OldUnicodeScript(Conversion.ACCADIAN, "accadian");
    public static final OldScript HEBREW = new OldUnicodeScript("UTF-8", "hebrew");
    public static final OldScript NULL = new OldUnicodeScript(null, null);
    
    static{
        SANSKRIT.setDefaultRenderMode(OldScript.AS_IMAGE);
        ARABIC.setDefaultRenderMode(OldScript.AS_IS);
        ACCADIAN.setDefaultRenderMode(OldScript.AS_XML_ENTITIES);
        HEBREW.setDefaultRenderMode(OldScript.AS_XML_ENTITIES);
    }
    
    private String enc;
    
    /** Creates a new instance of UnicodeScript */
    public OldUnicodeScript(String enc) {
        this.enc = enc;
    }
    
    /** Creates a new instance of UnicodeScript */
    public OldUnicodeScript(String enc, String name) {
        this.enc = enc;
        setName(name);
    }
    
    public List getForms() {
        return null;
    }
    
    public OldText getText(ResultSet rs, int col) throws SQLException {
        return new OldStringText(Conversion.getColumn(rs, col, enc), this);
    }
    
    public void setText(PreparedStatement stmt, int col, String str) throws SQLException {
        Conversion.setColumn(stmt, col, str, enc);
    }
        
}
