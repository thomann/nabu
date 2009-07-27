package ch.unizh.ori.nabu.voc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ch.unizh.ori.nabu.ui.VocabularyXMLExporter;
/**
 * Class <code>VocabularyConfigurator</code> is a helper class to turn
 * the configuration given by a tutor into the XML needed.
 * @author pht
 *
 */
public class VocabularyConfigurator {

	/**
	 * Turns the data given by a tutor into the appropriate XML-Description. It parses
	 * "columns" and "modes" from vars and parses it using readTable with parameter separator.
	 * 
	 * @param vars contains the parameters like name, description, id, encoding etc.
	 * @param separator contains the separator to get columns and modes from the properties.
	 * @return A <code>DOM-Document</code> which can be loaded as a Vocabulary, or saved for later use.
	 * @throws ParserConfigurationException
	 */
	public static Document export(Properties vars, String separator)
				throws ParserConfigurationException, IOException{
		List cols = readTable(vars.getProperty("columns"), separator);
		List modes = readTable(vars.getProperty("modes"),separator);
		return export(new SimpleVocabularyConfiguration(vars, cols, modes));
	}
	
	public static class SimpleVocabularyConfiguration {
		private Properties vars;
		private List<String[]> columns;
		private List<String[]> modes;
		
		public SimpleVocabularyConfiguration(Properties vars, List columns,
				List modes) {
			this.vars = vars;
			if(this.vars == null)
				this.vars = new Properties();
			setColumns(columns);
			setModes(modes);
		}

		public SimpleVocabularyConfiguration(Properties vars) {
			setVars(vars);
		}

		public SimpleVocabularyConfiguration(File propertiesFile) throws IOException {
			Properties vars = new Properties();
			vars.load(new BufferedInputStream(new FileInputStream(propertiesFile)));
			setVars(vars);
		}

		public Properties getVars() {
			return vars;
		}

		public void setVars(Properties vars) {
			this.vars = vars;
			columns = readTable(vars.getProperty("columns"), ":");
			modes = readTable(vars.getProperty("modes"),":");
		}

		public String getId() {
			return vars.getProperty("id");
		}

		public void setId(String id) {
			vars.setProperty("id",id);
		}

		public String getName() {
			return vars.getProperty("name");
		}

		public void setName(String name) {
			vars.setProperty("name",name);
		}

		public String getDescription() {
			return vars.getProperty("description");
		}

		public void setDescription(String description) {
			vars.setProperty("description",description);
		}

		public String getLessonFormat() {
			return vars.getProperty("lfmt");
		}

		public void setLessonFormat(String lfmt) {
			vars.setProperty("lfmt",lfmt);
		}

		public boolean isEmptyline() {
			return "on".equals(vars.getProperty("emptyline"));
		}

		public void setEmptyline(boolean emptyline) {
			vars.setProperty("emptyline",emptyline?"on":"off");
		}

		public String getEncoding() {
			return vars.getProperty("enc");
		}

		public void setEncoding(String encoding) {
			vars.setProperty("enc",encoding);
		}

		public String getChangeDate() {
			return vars.getProperty("change.date");
		}

		public void setChangeDate(String changeDate) {
			vars.setProperty("change.date",changeDate);
		}

		public String getChangeIp() {
			return vars.getProperty("change.ip");
		}

		public void setChangeIp(String changeIp) {
			vars.setProperty("change.ip",changeIp);
		}

		public String getChangeUser() {
			return vars.getProperty("change.user");
		}

		public void setChangeUser(String changeUser) {
			vars.setProperty("change.user",changeUser);
		}

		public List<String[]> getColumns() {
			return columns;
		}

		public void setColumns(List<String[]> columns) {
			this.columns = columns;
			vars.setProperty("columns", joinTable(columns));
		}

		public List<String[]> getModes() {
			return modes;
		}

		public void setModes(List<String[]> modes) {
			this.modes = modes;
			vars.setProperty("modes", joinTable(modes));
		}

		public void storeProperties(OutputStream out, String comments) throws IOException {
			vars.store(out, comments);
		}

		public void storeProperties(File file, String comments) throws IOException {
			storeProperties(new BufferedOutputStream(new FileOutputStream(file)), comments);
		}

	}
	
	public static String joinTable(List<String[]> modes) {
		String[] arr = new String[modes.size()];
		int i = 0;
		for(String[] mode : modes){
			arr[i++] = join(mode, ":");
		}
		String s = join(arr, "\n");
		return s;
	}

	public static String join(String[] arr, String separator){
		if(arr.length == 0)
			return "";
		StringBuilder s = new StringBuilder(arr[0]);
		for(int i=1; i<arr.length; i++)
			s.append(separator).append(arr[i]);
		return s.toString();
	}
	public static String join(List<String> arr, String separator){
		if(arr.size() == 0)
			return "";
		StringBuilder s = new StringBuilder(arr.get(0));
		for(int i=1; i<arr.size(); i++)
			s.append(separator).append(arr.get(i));
		return s.toString();
	}

	/**
	 * Turns the data given by a tutor into the appropriate XML-Description.
	 * 
	 * @param vars contains the parameters like name, description, id, encoding etc.
	 * @param columns a <code>List<code> of <code>String[]</code>s that encode the columns
	 *  id, name, script and delimiters, where the last one or the last two are facultative.
	 * @param modes contains a <code>List</code> of <code>String[]</code>s that encode the
	 * modes id, short, and facultatively name.
	 * @return A <code>DOM-Document</code> which can be loaded as a Vocabulary, or saved for later use.
	 * @throws ParserConfigurationException
	 * @deprecated Use {@link #export(SimpleVocabularyConfiguration)} instead
	 */
	public static Document export(Properties vars, List columns, List modesList) throws ParserConfigurationException{
		return export(new SimpleVocabularyConfiguration(vars, columns,
				modesList));
	}

	/**
	 * Turns the data given by a tutor into the appropriate XML-Description.
	 * @param parameterObject TODO
	 * @param modes contains a <code>List</code> of <code>String[]</code>s that encode the
	 * modes short, and facultatively name.
	 * 
	 * @return A <code>DOM-Document</code> which can be loaded as a Vocabulary, or saved for later use.
	 * @throws ParserConfigurationException
	 */
	public static Document export(SimpleVocabularyConfiguration parameterObject) throws ParserConfigurationException{
		Document doc = DocumentBuilderFactory.newInstance()
			.newDocumentBuilder().newDocument();
		Element root = doc.createElement("items");
		doc.appendChild(root);
		Element vocabulary = doc.createElement("vocabulary");
		root.appendChild(vocabulary);
		
		// adding elementary properties
		vocabulary.setAttribute("id", parameterObject.getVars().getProperty("id"));
		if(parameterObject.getVars().getProperty("change.user")!= null){
			vocabulary.setAttribute("author", parameterObject.getVars().getProperty("change.user"));
		}
		addProperty(doc, vocabulary, "name", parameterObject.getVars().getProperty("name"));
		addProperty(doc, vocabulary, "description", parameterObject.getVars().getProperty("description"));
		
		// adding mappings, ie columns
		Element mapping = doc.createElement("mapping");
		vocabulary.appendChild(mapping);
		for (Iterator iterator = parameterObject.getColumns().iterator(); iterator.hasNext();) {
			String[] row = (String[]) iterator.next();
			Element col = doc.createElement("col");
			mapping.appendChild(col);
			
			int i=0;
			col.setAttribute("id", row[i++]);
			if(i<row.length)
			col.setAttribute("name", row[i++]);
			if(i<row.length)
				col.setAttribute("script", row[i++]);
			if(i<row.length)
				col.setAttribute("del", row[i++]);
		}
		
		// adding source
		Element src = doc.createElement("src");
		vocabulary.appendChild(src);
		
		src.setAttribute("src", parameterObject.getVars().getProperty("id") + ".txt");
		src.setAttribute("enc", parameterObject.getVars().getProperty("enc", "UTF-8"));
		if(parameterObject.getVars().containsKey("lfmt"))
			src.setAttribute("lfmt", parameterObject.getVars().getProperty("lfmt"));
		
		// adding modes
		Element modes = doc.createElement("modes");
		vocabulary.appendChild(modes);
		int i = 1;
		for (Iterator iterator = parameterObject.getModes().iterator(); iterator.hasNext();) {
			String[] row = (String[]) iterator.next();
			Element mode = doc.createElement("mode");
			modes.appendChild(mode);
			
			mode.setAttribute("id", String.valueOf(i++));
			mode.setAttribute("short", row[0]);
			if(row.length>1)
				mode.setAttribute("name", row[1]);
		}
		
		return doc;
	}
	
	/**
	 * Creates an <code>Element</code> with name <code>name</code> and
	 * textual body <code>value</code>.
	 * @param doc
	 * @param parent
	 * @param name
	 * @param value
	 * @return
	 */
	public static Element addProperty(Document doc, Element parent, String name, String value){
		Element child = doc.createElement(name);
		child.appendChild(doc.createTextNode(value));
		parent.appendChild(child);
		return child;
	}
	
	public static void main(String[] args){
		try {
			Document d = test("web/WEB-INF/subs/test/es.esoes.properties");
			VocabularyXMLExporter.writeXmlFile(d, "/Users/pht/Desktop/test.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Document test(String filename) throws FileNotFoundException, IOException, ParserConfigurationException{
		Properties props = new Properties();
		props.load(new FileInputStream(filename));
		return export(props, ":");
	}
	
	public static SimpleVocabularyConfiguration configurationFromFile(String filename) throws FileNotFoundException, IOException, ParserConfigurationException{
		Properties props = new Properties();
		props.load(new FileInputStream(filename));
		return new SimpleVocabularyConfiguration(props);
	}
	
	public static List<String[]> readTable(String string, String separator) {
		if(string==null || string.length()==0){
			return Collections.EMPTY_LIST;
		}
		List ret = new ArrayList();
		try{
		BufferedReader in = new BufferedReader(new StringReader(string));
		for(String l = in.readLine(); l != null; l = in.readLine()){
			if(l.length()==0)
				continue;
			String[] arr = l.split(separator);
			ret.add(arr);
		}
		}catch(IOException ex){
			// this really should not happen!
			ex.printStackTrace();
		}
		return ret;
	}

}
