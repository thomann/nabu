package ch.unizh.ori.nabu.ui.http.webservices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.unizh.ori.nabu.ui.http.ContextListener;
import ch.unizh.ori.nabu.ui.http.HttpCentral;
import ch.unizh.ori.nabu.ui.http.SubsFilter;
import ch.unizh.ori.nabu.voc.Vocabulary;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;



/**
 * The Nabu WebService. Has to be connected to a Nabu {@link HttpCentral}} and its
 * subs {@link ContextListener#central} and {@link SubsFilter#subs} respectively.
 * 
 * @author pht
 * @see SubsFilter
 *
 */

@WebService
public class NabuWS {
	
	private static Logger log = Logger.getLogger(NabuWS.class);
	
	private HttpCentral central;
	private Map<String, HttpCentral> subs;

	/**
	 * Gets the Nabu central and subs using
	 * {@link ContextListener#central} and {@link SubsFilter#subs}.
	 * 
	 */
	private void initVars() {
		if(central != null)
			return;
		central = ContextListener.central;
		subs = SubsFilter.subs;
	}
	
	/**
	 * The Web Method to get the set of subs.
	 * @return Set containing the names of the subs
	 */
	@WebMethod
	public Set<String> getSubs(){
		initVars();
		return subs.keySet();
	}

	/**
	 * The Web Method to get the list of .
	 * @return List containing the names of the subs
	 */
	@WebMethod
	public List<String[]> getVocs(String sub){
		log.trace("");
		initVars();
		HttpCentral c;
		if(sub != null)
			c = subs.get(sub);
		else
			c = central;
		List organization = c.getOrganization();
		List<String[]> ret = new ArrayList<String[]>(organization.size());
		for(Object o : organization){
			if (o instanceof Vocabulary) {
				Vocabulary voc = (Vocabulary) o;
				ret.add(new String[]{voc.getName(),voc.getId(),voc.getDescription()});
			}else if(o!=null){
				ret.add(new String[]{o.toString()});
			}
		}
		return ret;
	}
	
}
