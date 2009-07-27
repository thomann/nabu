/*
 * User.java
 *
 * Created on 26. August 2002, 07:51
 */

package ch.unizh.ori.nabu.core;

import java.util.*;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

/**
 *
 * @author  pht
 */
public class User {
    
    /** Holds value of property preferences. */
    private Properties preferences = new Properties();
    
    /** Creates a new instance of User */
    public User() {
    }
    
    /** Getter for property preferences.
     * @return Value of property preferences.
     */
    public Properties getPreferences() {
        return this.preferences;
    }
    
    /** Setter for property preferences.
     * @param preferences New value of property preferences.
     */
    public void setPreferences(Properties preferences) {
        this.preferences = preferences;
    }
    
    public static User getUser(HttpSession session){
        User u = (User)session.getAttribute("user");
        if(u == null){
            u = new User();
            session.setAttribute("user", u);
        }
        return u;
    }
    
    public static User getUser(PageContext pageContext){
        return getUser(pageContext.getSession());
    }
    
}
