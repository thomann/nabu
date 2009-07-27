/*
 * Created on 31.03.2004
 */
package ch.unizh.ori.nabu.voc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ch.unizh.ori.nabu.core.Central;
import ch.unizh.ori.nabu.core.DefaultDescriptable;
import ch.unizh.ori.nabu.core.DefaultQuestionIterator;
import ch.unizh.ori.nabu.core.ListQuestionProducer;
import ch.unizh.ori.nabu.core.QuestionProducer;
import ch.unizh.ori.nabu.core.Utilities;
import ch.unizh.ori.nabu.ui.DefaultRendererChooser;
import ch.unizh.ori.nabu.ui.Renderer;

/**
 * @author pht
 */
public class Vocabulary extends DefaultDescriptable{
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(Vocabulary.class);
	
	private Central central;
	private URL base;
	
	private List lections;
	private Map modes;
	
	private int count;
	
	private Map sounds = new HashMap();
	
	private List columns;
	private Map columnsMap;
	
	private Source src;
	
	private boolean decorate = false;
	
	/** Name of the configuring XML file.
	 * Relative to base.
	 */
	private String xmlFilename;
	
	/** Name of the original generating properties file.
	 * Relative to base.
	 */
	
	private String propertiesFilename;
	
	/**
	 * Name of the author. It is used for authorization.
	 */
	private String author;
	
	/**
	 * Indicates whether the vocabulary can and should be downloaded.
	 */
	private boolean downloadable = true;
	
	public Vocabulary(){
		//lections = new HashMap();
		modes = new TreeMap();
		columns = new ArrayList();
		columnsMap = new HashMap();
	}
	
	@Override
	public void setId(String id) {
		super.setId(id);
	}
	
	/**
	 * @return
	 */
	public List getLections() {
		if (lections == null) {
			try {
				lections = getSrc().readLections(base);
//				for (Iterator iter = lectionsList.iterator(); iter.hasNext();) {
//					AbstractFieldStream fs = (AbstractFieldStream) iter.next();
//					lections.put(fs.getId(), fs);
//				}
				int total = 0;
				for (Iterator iter = lections.iterator(); iter.hasNext();) {
					FieldStream fs = (FieldStream) iter.next();
					total += fs.getCount();
				}
				setCount(total);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lections;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @param map
	 */
	public void setLections(List list) {
		lections = list;
	}

	/**
	 * @return
	 */
	public Map getModes() {
		return modes;
	}

	/**
	 * @param map
	 */
	public void setModes(Map map) {
		modes = map;
	}
	
	public void addMode(Mode m){
		modes.put(m.getId(), m);
	}
	
	public void addCol(Column col){
		if(col.getColumn()<0){
			int prevIndex;
			if(columns.size() >= 1){
				prevIndex = ((Column)columns.get(columns.size()-1)).getColumn();
			}else{
				prevIndex = -1;
			}
			col.setColumn(prevIndex + 2 + col.getColumn());
		}
		col.setCentral(central);
		columns.add(col);
		columnsMap.put(col.getId(), col);
	}
	
	public void addSnd(SndColumn sc){
		addCol(sc);
		sounds.put(sc.getAd(), sc);
	}

	public Map createQuestion(String[] arr, String lection, int id){
		Map ret = new HashMap(columns.size());
		if(decorate){
			ret.put("id", new Integer(id));
			ret.put("lesson", lection);
		}
		for (Iterator iter = columns.iterator(); iter.hasNext();) {
			Column col = (Column) iter.next();
			ret.put(col.getId(), col.map(arr));
		}
		return ret;
	}
	
	public QuestionProducer createProducer(List lections, Filter filter, Mode mode){
		return createProducer(lections, filter, mode, true);
	}
	
	public QuestionProducer createProducer(List lections, Filter filter, Mode mode, boolean shuffle){
		List voc = createVocList(lections);
		if(filter != null){
			for (Iterator iter = voc.iterator(); iter.hasNext();) {
				Map v = (Map) iter.next();
				if(!filter.accept(v)){
					iter.remove();
				}
			}
		}
		if(mode != null){
			voc = mode.unify(voc);
		}
		if(shuffle)
			Collections.shuffle(voc);
		ListQuestionProducer qp = new ListQuestionProducer(voc);
		return qp;
	}
	
	public List createVocList(List lections){
		List ret = new ArrayList();
		for (Iterator iter = lections.iterator(); iter.hasNext();) {
			FieldStream fs = (FieldStream) iter.next();
			try {
				createVoc(fs, ret);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
	
	public void createVoc(FieldStream fs, Collection voc) throws Exception{
		Object param = fs.start();
		String[] arr;
		int id = 0;
		while( (arr = fs.next(param)) != null ){
			Map m = createQuestion(arr, fs.getId(), ++id);
			voc.add(m);
		}
		fs.stop(param);
	}
	
	/**
	 * @return
	 */
	public Central getCentral() {
		return central;
	}

	/**
	 * @param central
	 */
	public void setCentral(Central central) {
		this.central = central;
	}

	/**
	 * @return
	 */
	public List getColumns() {
		if(columns == null && getSrc().getHeader() != null){
			List cols = Utilities.split(getSrc().getHeader(), "\t");
			columns = new ArrayList(cols.size());
			for (Iterator iter = cols.iterator(); iter.hasNext();) {
				String colString = (String) iter.next();
				
				String[] data = colString.split(":");
				int i=0;
				if(i>=data.length)
					continue;
				StringColumn col = new StringColumn();
				col.setId(data[i++]);
				columns.add(col);
				if(i>=data.length)
					continue;
				col.setName(data[i++]);
				if(i>=data.length)
					continue;
				col.setScript(data[i++]);
			}
		}
		return columns;
	}
	
	public Column getColumn(String key){
		return (Column)columnsMap.get(key);
	}
	
	public Sotm getSound(String key){
		Sotm sotm = (Sotm)sounds.get(key);
		if(sotm == null){
			if (getColumn(key) instanceof StringColumn) {
				StringColumn col = (StringColumn) getColumn(key);
				String script = col.getScript();
				if(script != null)
					sotm = getCentral().getSotm(script);
			}
		}
		return sotm;
	}

	/**
	 * @return
	 */
	public Source getSrc() {
		return src;
	}

	/**
	 * @param list
	 */
	public void setColumns(List list) {
		columns = list;
	}

	/**
	 * @param source
	 */
	public void setSrc(Source source) {
		src = source;
	}

	/**
	 * @return
	 */
	public URL getBase() {
		return base;
	}

	/**
	 * @param url
	 */
	public void setBase(URL url) {
		base = url;
		propertiesFilename = doDefaultFilename(url, ".properties", propertiesFilename);
		xmlFilename = doDefaultFilename(url, ".xml", xmlFilename);
		log.debug(String.format("Base: %s, props: %s, xml: %s, files: %s",base,propertiesFilename, xmlFilename, getFilenames()));
		log.debug(String.format("configurable: %s, fileSrc: %s", isConfigurable(), isFileSrc()));
	}

	private String doDefaultFilename(URL base, String suffix, String defaultName) {
		if(defaultName == null && "file".equals(base.getProtocol())){
			try {
				String filename = getId()+suffix;
				File file = new File(new URL(base, filename).getFile());
				if(file.exists())
					return filename;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return defaultName;
	}
	
	public boolean isConfigurable(){
		return getPropertiesFilename() != null;
	}
	
	public boolean isFileSrc(){
		return getSrc() instanceof AbstractFileSource;
	}

	public DefaultQuestionIterator createIter(List lections, Renderer r) {
		return createIter(lections, r, null, null);
	}
	public DefaultQuestionIterator createIter(List lections, Renderer r, Filter filter, Mode mode) {
		QuestionProducer prod = this.createProducer(lections, filter, mode);
		Map map = new HashMap();
		map.put(prod, r);
		DefaultRendererChooser chooser = new DefaultRendererChooser();
		chooser.setRendererMap(map);
		
		DefaultQuestionIterator iter = new DefaultQuestionIterator();
		iter.addProducer(prod);
		iter.setRendererChooser(chooser);
		
//		iter.next();
		return iter;
	}
	
	/**
	 * @return
	 */
	public Map getColumnsMap() {
		return columnsMap;
	}

	/**
	 * @return
	 */
	public Map getSounds() {
		return sounds;
	}
	
	/**
	 *
	 */

	public String toString() {
		return getName();
	}


	/**
	 * @return Returns the decorate.
	 */
	public boolean isDecorate() {
		return decorate;
	}
	/**
	 * @param decorate The decorate to set.
	 */
	public void setDecorate(boolean decorate) {
		this.decorate = decorate;
	}

	public String getXmlFilename() {
		return xmlFilename;
	}

	public void setXmlFilename(String xmlFilename) {
		this.xmlFilename = xmlFilename;
	}

	public String getPropertiesFilename() {
		return propertiesFilename;
	}

	public void setPropertiesFilename(String propertiesFilename) {
		this.propertiesFilename = propertiesFilename;
	}

	/**
	 * Property getter for name of the author. It is used for authorization.
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Property setter for name of the author. It is used for authorization.
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Indicates whether the vocabulary can and should be downloaded.
	 * 
	 * @return the downloadable
	 */
	public boolean isDownloadable() {
		return downloadable;
	}

	/**
	 * Indicates whether the vocabulary can and should be downloaded.
	 * 
	 * @param downloadable the downloadable to set
	 */
	public void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}
	
	public List<String> getFilenames(){
		ArrayList<String> ret = new ArrayList<String>();
		if(getXmlFilename() != null)
			ret.add(getXmlFilename());
		if(getPropertiesFilename() != null)
			ret.add(getPropertiesFilename());
		if(src instanceof AbstractFileSource && src.getSrc() != null)
			ret.add(src.getSrc());
		return ret;
	}
}
