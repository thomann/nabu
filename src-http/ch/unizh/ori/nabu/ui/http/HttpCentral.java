/*
 * Created on 31.03.2004
 */
package ch.unizh.ori.nabu.ui.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import ch.unizh.ori.common.text.PlotterPresentation;
import ch.unizh.ori.nabu.core.Central;
import ch.unizh.ori.nabu.voc.Vocabulary;

/**
 * @author pht
 */
public class HttpCentral extends Central {
	
	private transient ServletContext application;
	private static final Logger LOGGER = Logger.getLogger(HttpCentral.class);
	
	private String subName;
	private String uploadVocLocation;
	private List organization = new ArrayList();

	public static final String DEFAULT_UPLOAD_LOCATION = "/WEB-INF/uploadvocs/";
	public static final String PROPERTIES_SUFFIX = ".properties";
	
	private Set<String> admins = new TreeSet<String>();
	private Set<String> publicAdmins = Collections.unmodifiableSet(admins);
	private Set<String> tutors = new TreeSet<String>();
	private Set<String> publicTutors = Collections.unmodifiableSet(tutors);

	public Set<String> getAdmins() {
		return publicAdmins;
	}
	public Set<String> getTutors() {
		return publicTutors;
	}
	public HttpCentral(){
		throw new RuntimeException("Never use this constructor!");
	}
	public HttpCentral(ServletContext application, String name){
		this.application = application;
		application.setAttribute(name, this);
		if(uploadVocLocation == null){
			uploadVocLocation = application.getRealPath(DEFAULT_UPLOAD_LOCATION);
		}
	}
	
	public void read(){
		LOGGER.debug("reading: "+getName());
		readUploadVocs();
		readOrganization();
		readACL();
	}
	
	public synchronized void readOrganization() {
		Set vs = new TreeSet(vocs.keySet());
		boolean hadTitle = false;
		organization = new ArrayList();
		BufferedReader in = null;
		try {
			// Read from list.index
			in = new BufferedReader(
					new InputStreamReader(new FileInputStream(
							getUploadVocLocation() + "/list.index"), "UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				// if it starts with "-" its a title else a voc
				if (line.startsWith("-")) {
					organization.add(line.substring(1));
					hadTitle = true;
				} else if(vocs.get(line) == null){
					LOGGER.info("In Loading Organization: Didn't have "+line);
				} else {
					organization.add(vocs.get(line));
					vs.remove(line);
				}
			}
		} catch(FileNotFoundException ex){
			application.log("list.index does'nt exist: "+ex);
		} catch (IOException ex) {
			ex.printStackTrace();
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// add not yet named entries
		// add a title if we had already something.
		if(hadTitle && vs.size() > 0){
			organization.add("Andere");
		}
		for (Iterator iterator = vs.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			organization.add(vocs.get(key));
		}
	}
	
	public synchronized void setOrganizationUp(int i){
		synchronized(organization){
			if(i==0)
				return;
			Object tmp = organization.get(i);
			organization.set(i, organization.get(i-1));
			organization.set(i-1, tmp);
			saveOrganization();
		}
	}
	
	public synchronized void setOrganizationDown(int i){
		synchronized(organization){
			if(i==organization.size()-1)
				return;
			Object tmp = organization.get(i);
			organization.set(i, organization.get(i+1));
			organization.set(i+1, tmp);
			saveOrganization();
		}
	}
	
	public synchronized void organizationAddTitle(String title, int i){
		synchronized(organization){
			organization.add(i, title);
			saveOrganization();
		}
	}
	
	public synchronized void organizationSetTitle(String title, int i){
		synchronized(organization){
			organization.set(i, title);
			saveOrganization();
		}
	}
	
	public synchronized void setOrganizationRemoveTitle(int i){
		synchronized(organization){
			if(!(organization.get(i) instanceof String))
				return;
			Object tmp = organization.remove(i);
			saveOrganization();
		}
	}
	
	public synchronized void saveOrganization(){
		try{
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getUploadVocLocation() + "/list.index"), "UTF-8"));
			for (Iterator iterator = getOrganization().iterator(); iterator.hasNext();) {
				Object val = iterator.next();
				if(val instanceof String){
					out.println("-"+val);
				}else{
					Vocabulary voc = (Vocabulary) val;
					out.println(voc.getId());
				}
			}
			out.close();
		}catch(IOException ex){
			application.log("Problem saving organization", ex);
		}
	}
	
	public synchronized void readACL() {
		admins = new TreeSet<String>();
		publicAdmins = Collections.unmodifiableSet(admins);
		tutors = new TreeSet<String>();
		publicTutors = Collections.unmodifiableSet(tutors);
		try {
			// Read from list.index
			Properties users = new Properties();
			users.load(new BufferedInputStream(new FileInputStream(
							getUploadVocLocation() + "/acl.index")));
			Map<String, String> users2 = new HashMap<String, String>((Map) users);
			for(String user : users2.keySet()){
				String val=users.getProperty(user);
				if(val.contains("admin")){
					admins.add(user);
				}
				if(val.contains("tutor")){
					tutors.add(user);
				}
			}
		} catch(FileNotFoundException ex){
			application.log("acl.index does'nt exist: "+ex);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		LOGGER.debug(tutors);
		LOGGER.debug(admins);
	}
	
	public synchronized void setAdminsString(String adminsSeperatedByNewline){
		admins.clear();
		for(String user : adminsSeperatedByNewline.split("\n")){
			user = user.trim();
			if(user.length() != 0)
				admins.add(user);
		}
		saveACL();
	}
	
	public synchronized void setTutorsString(String tutorsSeperatedByNewline){
		tutors.clear();
		for(String user : tutorsSeperatedByNewline.split("\n")){
			user = user.trim();
			if(user.length() != 0)
				tutors.add(user);
		}
		saveACL();
	}
	
	public synchronized void saveACL(){
		OutputStream out = null;
		LOGGER.debug(tutors);
		LOGGER.debug(admins);
		try{
			Properties users = new Properties();
			for (String user : admins) {
				users.setProperty(user, "admin");
			}
			for(String user : tutors){
				if(users.contains(user))
					users.setProperty(user, users.getProperty(user)+", tutor");
				else
					users.setProperty(user,"tutor");
			}
			out = new BufferedOutputStream(new FileOutputStream(getUploadVocLocation() + "/acl.index"));
			users.store(out, "Saved by HttpCentral");
		}catch(IOException ex){
			application.log("Problem saving acl", ex);
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean canWrite(PageContext page, String vocId){
		String user = (String) page.findAttribute("user");
		Object isAdmin = page.findAttribute("isAdmin");
		if(isAdmin!=Boolean.TRUE && vocId!=null){
			Vocabulary theVocabulary = (Vocabulary) getVocs().get(vocId);
			if(theVocabulary!=null && theVocabulary.getAuthor() != null && !theVocabulary.getAuthor().equals(user)){
				return false;
			}
		}
		return true;
	}
	
	public void assertCanWrite(HttpServletRequest request, String vocId) throws JspException{
		String user = (String) request.getAttribute("user");
		if(user == null)
			user = (String)request.getSession().getAttribute("user");
		if(request.getAttribute("isAdmin")!=Boolean.TRUE && vocId!=null){
			Vocabulary theVocabulary = (Vocabulary) getVocs().get(vocId);
			if(theVocabulary!=null && theVocabulary.getAuthor() != user){
				throw new JspException(String.format("User %s may not edit vocabulary %s", user, vocId));
			}
		}
	}
	
	@Override
	public void addVoc(Vocabulary voc) {
		super.addVoc(voc);
		organization.add(voc);
	}
	
	public void reloadVoc(String id){
		if(!new File(getUploadVocLocation(), id+PROPERTIES_SUFFIX).exists())
			throw new RuntimeException("Doesn't exist or is not reloadable: "+id+PROPERTIES_SUFFIX);
		getVocs().remove(id);
		try {
			digestXML(new File(getUploadVocLocation(),id+".xml").getAbsolutePath());
		} catch (SAXException ex) {
			LOGGER.error("Reloading "+id, ex);
		} catch (IOException ex) {
			LOGGER.error("Reloading "+id, ex);
		}
	}

	public void readUploadVocs(){
		readDir(getUploadVocLocation());
		readDir(application.getRealPath("/WEB-INF/config"));
	}
	
	public void readWebAppFiles(String dir){
		Set paths = application.getResourcePaths(dir);
		for (Iterator iter = paths.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if(element.endsWith(".xml")){
				String path = application.getRealPath(element);
				try {
					log("digesting: "+path);
					digestXML(path);
				} catch (SAXException e) {
					application.log("readFiles", e);
				} catch (IOException e) {
					application.log("readFiles", e);
				}
			}
		}
	}
	
	public void log(String str, Throwable t) {
		application.log(str, t);
	}
	
	public void log(String str) {
		application.log(str);
	}

	public String fixLocation(String name){
		return fixLocation(getUploadVocLocation(), name);
	}

	public static String fixLocation(String location, String name){
		int index = name.lastIndexOf(File.separator);
		index = Math.max(0, index);
		return location + File.separator + name.substring(index);
	}
	
	public List getUploadVocList(){
		List ret = new ArrayList();
		String[] files = new File(getUploadVocLocation()).list();
		for (int i = 0; i < files.length; i++) {
			String file = files[i];
			if(file.endsWith(PROPERTIES_SUFFIX)){
				String id = file.substring(0, file.length()-PROPERTIES_SUFFIX.length());
				ret.add(id);
			}
		}
		return ret;
	}
	
	public static Properties loadProps(HttpCentral c, String name) throws Exception{
		return c.loadProps(name);
	}
	
	public Properties loadProps(String name) throws Exception{
		Properties ret = null;
		if(name != null){
			String path = fixLocation(name+PROPERTIES_SUFFIX);
			ret = new Properties();
			ret.load(new FileInputStream(path));
		}
		return ret;
	}
	
	public String getPlotterUrl(PlotterPresentation p, String scriptId, Object value, String transliteration){
		value = getScript(scriptId).convert(value, transliteration, p.getOutTransliteration());
		String prefix = "/plotter?plotter="+p.getPlotterId()+"&text=";
		if("egy".equals(p.getPlotterId())){
			prefix = "/hiero?text=";
		}else if("sux".equals(p.getPlotterId())){
			prefix = "/cuneiform?text=";
		}
		return prefix+value;
	}


	/**
	 * @return
	 */
	public String getUploadVocLocation() {
		return uploadVocLocation;
	}

	/**
	 * @param string
	 */
	public void setUploadVocLocation(String string) {
		uploadVocLocation = string;
	}
	public List getOrganization() {
		return organization;
	}
	public void setOrganization(List organization) {
		this.organization = organization;
	}
	/**
	 * @return the subName
	 */
	public String getSubName() {
		return subName;
	}
	/**
	 * @param subName the subName to set
	 */
	public void setSubName(String subName) {
		this.subName = subName;
	}

}
