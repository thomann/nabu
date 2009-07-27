/*
 * Script.java
 *
 * Created on 22. Februar 2003, 20:29
 */

package ch.unizh.ori.common.text;

import ch.unizh.ori.nabu.core.User;

import java.util.*;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author  pht
 */
public abstract class OldScript {
    
    public static final String UNICODE = "unicode";
    public static final String ASCII = "ascii";
    
    public static final String TRANSLITERATION = "transliteration";
    
    public static final String AS_IS = "as_is";
    public static final String AS_XML_ENTITIES = "as_xml_entities";
    public static final String AS_IMAGE = "as_image";
    
    public static final String PREF_RENDER_MODE = "nabu.tupp.Script.renderMode";
    public static final String PREF_RENDER_DEFAULT = "nabu.tupp.Script.renderMode_default";

    /** Holds value of property name. */
    private String name;
    
    /** Holds value of property defaultRenderMode. */
    private String defaultRenderMode = AS_IS;
    
    /** Creates a new instance of Script */
    public OldScript() {
    }
    
    public abstract List getForms();
    
    public abstract OldText getText(ResultSet rs, int col) throws SQLException;
    public abstract void setText(PreparedStatement stmt, int col, String str) throws SQLException;
    
    public String getRenderMode(javax.servlet.jsp.PageContext pageContext){
        User u = User.getUser(pageContext);
        String r = u.getPreferences().getProperty(PREF_RENDER_MODE+"_"+getName());
        System.out.println("pref: "+PREF_RENDER_MODE+"_"+getName()+": "+r);
        if(r == null){
            r = u.getPreferences().getProperty(PREF_RENDER_DEFAULT, getDefaultRenderMode());
            System.out.println("pref: "+PREF_RENDER_DEFAULT+": "+r);
        }
        System.out.println("renderMode: "+r);
        return r;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return this.name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** Getter for property defaultRenderMode.
     * @return Value of property defaultRenderMode.
     */
    public String getDefaultRenderMode() {
        return this.defaultRenderMode;
    }
    
    /** Setter for property defaultRenderMode.
     * @param defaultRenderMode New value of property defaultRenderMode.
     */
    public void setDefaultRenderMode(String defaultRenderMode) {
        this.defaultRenderMode = defaultRenderMode;
    }
    
}
