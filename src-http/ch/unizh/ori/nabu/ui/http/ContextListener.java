/*
 * ContextListener.java
 *
 * Created on 13. April 2003, 14:52
 */

package ch.unizh.ori.nabu.ui.http;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

/**
 *
 * @author  pht
 * @version
 */

public class ContextListener implements ServletContextListener {
	
	private static Logger LOGGER = Logger.getLogger(ContextListener.class);
	public static HttpCentral central; 
    
    public void contextInitialized(ServletContextEvent e) {
    	LOGGER.info("Starting context "+e.getServletContext().getServletContextName()+
    			". Logging enabled with level "+LOGGER.getEffectiveLevel());
        central = new HttpCentral(e.getServletContext(), "central");
		central.read();
//		try {
//			Context initCtx = new InitialContext();
//			Context envCtx = (Context) initCtx.lookup("java:comp/env");
//			
//			envCtx.bind("nabu",c);
//			LOGGER.info("Bound nabu");
//		} catch (NamingException ex) {
//			LOGGER.error(ex);
//		}

    }
    
    public void contextDestroyed(ServletContextEvent e) {
    }
}
