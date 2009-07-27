package ch.unizh.ori.nabu.ui.http;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ch.unizh.ori.nabu.ui.http.webservices.NabuWS;

/**
 * The <code>SubsFilter</code> handles branching into the different subs as well
 * as authentication, authorization and SSL redirection. Every request to Nabu
 * has to pass here by.
 * 
 * For authentication configuration see {@link #init(FilterConfig)}. For the actions
 * performed on the request see {@link #doFilter(ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
 * 
 * A word on LDAP/UniAccess authentication: This is done in this class using {@link DirContext}, by binding
 * the user with the login password to <code>uid=&lt;username&gt;,ou=People,ou=UniAccess,ou=zi,dc=unizh,dc=ch</code>
 * on server <code>ldap://ldap.uzh.ch:389/</code>. If binding is ok, the password is correct, else it's not.
 * 
 * For debugging purposes, if a parameter <code>debugAuth=1</code> is added to any url, a 401 error is
 * returned, so that the browser asks again for a password. This can be used to give a new
 * username, which is else only possible by restarting the browser.
 * 
 * @author pht
 * @see #doFilter(ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
 */	
public class SubsFilter implements Filter {
	
	private Set<String> topUsers = new HashSet<String>();
	private Map<String, String> givenUsers = new HashMap<String, String>();
	
	private static Logger log = Logger.getLogger(SubsFilter.class);

	/**
	 * This maps sub names to the corresponding {@link HttpCentral}.
	 * 
	 * @see NabuWS#getSubs()
	 */
	public static Map<String, HttpCentral> subs;

	private ServletContext application;

	
	
	/**
	 * The https port used by this filter for redirecting.
	 * We have a problem with getting the correct port by api. 
	 */
	public static int securePort = 443;
	
	private boolean portInitialized = false;
	private static String portString = "";

	/**
	 * Initializes the Filter by getting the list of subs and reading top user
	 * and given user information.	
	 * 
	 * A sub is just a copy of the nabu realm backed by distinct vocabularies.
	 * Every directory beneath <code>/WEB-INF/subs</code>
	 * implicates a sub, except hidden folders and folders with name <code>admin</code>.
	 * This directory is the <code>uploadvocs</code> location for the sub.
	 * 
	 * A top user has "root" access to the main nabu and every sub. Top users are
	 * specified by the init param <code>topUsers</code> as a comma separated list,
	 * as <code>pht,johi</code>.
	 * 
	 * A "given user" is not authenticated via LDAP/UniAccess. Instead a
	 * "given user" is specified by a init param with name <code>user.&lt;username&gt;</code>.
	 * The password is given by the value as an a MD5 hash (see {@link SubsFilter#hashPassword(String)}).
	 * 
	 * @param config the filter configuration.
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		subs = new HashMap<String, HttpCentral>();
		application = config.getServletContext();
		File dir = new File(application.getRealPath("/WEB-INF/subs"));
		if(dir.exists() && dir.isDirectory()){
			for (int i = 0; i < dir.list().length; i++) {
				String f = dir.list()[i];
				if(f.equalsIgnoreCase("admin") || f.startsWith(".") || new File(dir,f).isHidden())
					continue;
				log.info("Adding subCentral: " + f);
				HttpCentral central = new HttpCentral(application, f);
				central.setSubName(f.substring(0,1).toUpperCase()+f.substring(1));
				subs.put(f, central);
				central.setId(f);
				central.setName(f);
				central.setUploadVocLocation(application.getRealPath("/WEB-INF/subs/" + f));
				central.read();
			}
		}
		
		List<String> initNames = new ArrayList<String>(Collections.list(config.getInitParameterNames()));
		for(String name : initNames){
			if(name.startsWith("user.")){
				givenUsers.put(name.substring("user.".length()), config.getInitParameter(name));
			}else if(name.equals("topUsers")){
				topUsers.addAll(Arrays.asList(config.getInitParameter(name).split(",")));
			}
		}
	}


	/**
	 * Filters the request. First if it is the first request, infers the correct SSL-Port by using {@link ServletRequest#getServerPort()}
	 * and {@link ServletRequest#isSecure()}.
	 * 
	 * Then it analyzes the path to get the sub. The appropriate {@link HttpCentral} and <code>$sub</code> path
	 * is added to the request attributes. If administrative (<code>/admin/*</code>) tasks are adressed,
	 * the url gets normalized to <code>https://&lt;server&gt;:{@link #securePort}/nabu/admin/$sub/...</code>.
	 * 
	 * Then Basic HTTP authentication is required. The resulting username is authenticated as 
	 * a "given user" or via LDAP/UniAccess bind. Finally the user is authorized either as
	 * a "top user" or by using the ACL information in the corresponding sub.
	 * 
	 * The request is redirected to the plain jsp or servlet.
	 * 
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	public void doFilter(javax.servlet.ServletRequest _request,
			javax.servlet.ServletResponse _response,
			javax.servlet.FilterChain chain) throws java.io.IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) _request;
		HttpServletResponse response = (HttpServletResponse) _response;
		
		if(!portInitialized){
			portInitialized = true;
			securePort = request.getServerPort();
			if(!request.isSecure()){
				securePort += 443-80;
			}
			log.info("Secure port assigned to: "+securePort);
			portString = (securePort == 443)?"":":"+securePort;
		}

		String thePath = request.getServletPath();
		HttpCentral c = null;
		
		String key = "";
		for (Iterator iter = subs.keySet().iterator(); iter.hasNext();) {
			key = (String) iter.next();
			if (thePath.startsWith("/" + key + "/") || thePath.startsWith("/admin/" + key + "/")) {
//				LOGGER.debug("contextPath: " + request.getContextPath());
//				LOGGER.debug("pathInfo: " + request.getPathInfo());
//				LOGGER.debug("pathTranslated: " + request.getPathTranslated());
//				LOGGER.debug("QueryString: " + request.getQueryString());
//				LOGGER.debug("getRequestURI: " + request.getRequestURI());
//				LOGGER.debug("getServletPath: " + request.getServletPath());
				c = (HttpCentral) subs.get(key);
				if (c == null) {
					throw new IllegalArgumentException("Url no good");
				}
				
				key = "/"+key;
				break;
			}
		}
		if (c == null) {
			c = (HttpCentral) application.getAttribute("central");
			key = "";
		}

		request.setAttribute("central", c);
		request.setAttribute("sub", key);
		
		boolean admin = thePath.startsWith("/admin/");
				
		int subPathLen = key.length();
		if(admin)
			subPathLen += "/admin".length();
		String subpath = thePath.substring(subPathLen);
		if(!admin && subpath.startsWith("/admin/")){
			response.sendRedirect(getDispatchPath(request.getContextPath()+"/admin"+key+subpath.substring("/admin".length()), request, response));
			log.debug("key: " + key);
			return;
		}
		if(admin)
			subpath = "/admin"+subpath;
		
		if(admin){
			// ensure SSL
			if(!request.isSecure()){
				String secureLocation = "https://"+request.getServerName()+portString+request.getRequestURI();
				response.sendRedirect(secureLocation);
				return;
			}
		}
		
		
		if(admin){
			String user = authenticate(request, response,c);
			if(user == null){
				return;
			}
			if(!authorize(request, response, user, c)){
				return;
			}
		}
			
		if(key != ""){
			String s = getDispatchPath(subpath, request, response);
			log.debug("s: " + s);
			request.getRequestDispatcher(s).forward(request, response);
//				chain.doFilter(request, response);
			return;
		}
		
		chain.doFilter(request, response);
	}

	private boolean authorize(HttpServletRequest req,
			HttpServletResponse res, String user, HttpCentral central) throws IOException {
		
		// now we lookup and set the area, the user belongs to
		boolean isAdmin = (central.getAdmins().contains(user) || req.isUserInRole("nabu-admin") || topUsers.contains(user));
		boolean isTutor = central.getTutors().contains(user);
		log.debug(String.format("user: %s, admin: %s, tutor %s", user, isAdmin, isTutor));
		if(!(isAdmin || isTutor)){
			res.sendError(401, "Not authorized for NABU "+central.getName()+"!");
			return false;
		}
		
		req.setAttribute("user", user);
		req.setAttribute("isAdmin", isAdmin);
		req.setAttribute("isTutor", isTutor);
		return true;
	}

	private String authenticate(HttpServletRequest req,
			HttpServletResponse res, HttpCentral central) throws IOException {
		String auth = req.getHeader("authorization");
		if (auth != null && req.getParameter("debugAuth")==null) {
			auth = new String((new sun.misc.BASE64Decoder()).decodeBuffer(auth
					.substring("Basic ".length())));
			int i = auth.indexOf(":");
			String user = auth.substring(0, i);
			String passwd = auth.substring(i + 1);
			log.debug("auth: " + user);
			String givenPassword = givenUsers.get(user);
			if(givenPassword != null) {
				passwd = hashPassword(passwd);
				if(givenPassword.equals(passwd))
					return user;
			} else {
				try {
					String name = "ou=People,ou=UniAccess,ou=zi,dc=unizh,dc=ch";
					String userPrincipal = "uid=" + user + "," + name;
					String connectionURL = "ldap://ldap.unizh.ch:389/";// +
																		// name;

					Hashtable env = new Hashtable();
					env.put(Context.INITIAL_CONTEXT_FACTORY,
							"com.sun.jndi.ldap.LdapCtxFactory");
					env.put(Context.SECURITY_AUTHENTICATION, "simple");
					env.put(Context.SECURITY_PRINCIPAL, userPrincipal);
					env.put(Context.SECURITY_CREDENTIALS, passwd);
					env.put(Context.PROVIDER_URL, connectionURL);
					DirContext ctx = new InitialDirContext(env);

					// no exception, so we could bind :-)

					return user;

					// Attributes attributes = ctx.getAttributes(userPrincipal);
					//
					// for (NamingEnumeration ae = attributes.getAll();
					// ae.hasMore();) {
					// Attribute attr = (Attribute) ae.next();
					// System.out.printf("%s: %s%n", attr.getID(), Collections
					// .list(attr.getAll()));
					// }
					//
					// attributes = ctx.getAttributes(userPrincipal,
					// new String[] { "swissEduPersonHomeOrganization" });
					// for (NamingEnumeration ae = attributes.getAll();
					// ae.hasMore();) {
					// Attribute attr = (Attribute) ae.next();
					// System.out.printf("--%s: %s%n", attr.getID(), Collections
					// .list(attr.getAll()));
					// }

				} catch (Exception e) {
					log.debug(e);
				}
			}
		}
		res.setHeader("WWW-Authenticate", "Basic realm=\"Nabu "
				+ central.getSubName() + "\"");
		res.sendError(401);
		return null;
	}

	public String getDispatchPath(String path, HttpServletRequest request,
			HttpServletResponse response) {
		String queryString = request.getQueryString();
		if(queryString != null){
			return path + "?" + queryString;
		}else{
			return path;
		}
	}
	
	public void destroy() {
	}


	/**
	 * Returns the MD5 message digest of password padded to 32 characters.
	 * 
	 * @param password password to hash
	 * @return the MD5 message digest of password padded to 32 characters
	 */
	public static String hashPassword(String password) {
		String hashword = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashword = String.format("%032x",hash);
		} catch (NoSuchAlgorithmException nsae) {
			log.error(nsae);
		}
		return hashword;
	}

}
