/*
 * Created on 31.03.2004
 */
package ch.unizh.ori.nabu.voc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.unizh.ori.common.text.DefaultScript;
import ch.unizh.ori.common.text.Script;
import ch.unizh.ori.nabu.core.DefaultDescriptable;
import ch.unizh.ori.nabu.core.Utilities;

/**
 * @author pht
 */
public class Mode extends DefaultDescriptable {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(Mode.class);
	
	private Vocabulary voc;
	
	private String myShort;
	
	private List modeFields;
	
	private Filter filter;
	
	private boolean unified = true;
	
	/**
	 * @return
	 */
	public String getShort() {
		return myShort;
	}

	/**
	 * @param string
	 */
	public void setShort(String string) {
		myShort = string;
	}

	private List createFromShort(String myShort) {
		List l = Utilities.split(myShort, "=");
		List ret = new ArrayList(l.size());
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			String s = (String) iter.next();
			ModeField mf = new ModeField();
			if(s.startsWith("?")){
				mf.setAsking(true);
				s = s.substring(1);
			}
			String presentationId = null;
			int indexOf = s.indexOf("#");
			if(indexOf >= 0){
				presentationId = s.substring(indexOf + 1);
				s = s.substring(0, indexOf);
			}
			mf.setMode(this);
			mf.setKey(s);
			mf.setColumn(getVoc().getColumn(mf.getKey()));
			
			if(mf.getColumn() == null)
				continue; // we don't add it, because of implications...
			if (mf.getColumn() instanceof StringColumn) {
				StringColumn column = (StringColumn) mf.getColumn();
				Script script = getVoc().getCentral().getScript(column.getScript());
				if(script != null){
					if ((presentationId == null || presentationId.equals(""))
							&& script instanceof DefaultScript) {
						DefaultScript defaultScript = (DefaultScript) script;
						if (mf.isAsking()) {
							presentationId = defaultScript
									.getDefaultEditablePresentation();
						} else {
							presentationId = defaultScript
									.getDefaultViewOnlyPresentation();
						}
					}
					mf.setPresentation(script.getPresentation(presentationId));
				}
			}
			ret.add(mf);
		}
		return ret;
	}
	
	public List unify(List voc){
		if(!isUnified()){
			return voc;
		}
		Map newVoc = new HashMap(voc.size());
		for (Iterator iter = voc.iterator(); iter.hasNext();) {
			Object o = (Object) iter.next();
			putIntoMap(newVoc, o);
		}
		return new ArrayList(newVoc.values());
	}
	
	protected void putIntoMap(Map voc, Object question){
		Map q = (Map) question;
		Map key = new HashMap();
		for (Iterator iter = getModeFields0().iterator(); iter.hasNext();) {
			ModeField mf = (ModeField) iter.next();
			if(!mf.isAsking()){
				key.put(mf.getKey(), q.get(mf.getKey()));
			}
		}
		Map q2 = (Map) voc.get(key);
		if(q2 != null){
			for (Iterator iter = getModeFields0().iterator(); iter.hasNext();) {
				ModeField mf = (ModeField) iter.next();
				if(mf.isAsking()){
					String key2 = mf.getKey();
					Object oldVal = q2.get(key2);
					Object newVal = q.get(key2);
					String val = oldVal + " ; "+newVal;
					q2.put(key2, val);
				}
			}
			log.trace(q2);
		}else{
			voc.put(key, q);
		}
	}

	/**
	 * @return
	 */
	public List createModeFields() {
		getModeFields0();
		List l = new ArrayList(modeFields.size());
		for (int i=0; i<l.size(); i++) {
			ModeField m = (ModeField) l.get(i);
			l.set(i, m.copy());
		}
		return modeFields;
	}

	private List getModeFields0() {
		if(modeFields == null){
			setModeFields(createFromShort(myShort));
		}
		return modeFields;
	}
	
	public String getName() {
		String name = super.getName();
		if (name == null && getModeFields0() != null) {
			name = createNiceName();
			super.setName(name);
		}
		return name;
	}


	/**
	 * @return
	 */
	private String createNiceName() {
		StringBuffer given = new StringBuffer();
		StringBuffer var = new StringBuffer();
		for (Iterator iter = modeFields.iterator(); iter.hasNext();) {
			ModeField mf = (ModeField) iter.next();
			StringBuffer s = (!mf.isAsking())?given:var;
			if(s.length() > 0){
				s.append(", ");
			}
			String colName = mf.getKey();
			if(mf.getColumn() != null)
				colName = mf.getColumn().getName();
			s.append(colName);
		}
		return given.append(" -> ").append(var).toString();
	}

	/**
	 * @param list
	 */
	public void setModeFields(List list) {
		modeFields = list;
	}

	/**
	 * @return
	 */
	public Vocabulary getVoc() {
		return voc;
	}

	/**
	 * @param vocabulary
	 */
	public void setVoc(Vocabulary vocabulary) {
		voc = vocabulary;
	}

	/**
	 * @return Returns the filter.
	 */
	public Filter getFilter() {
		return filter;
	}
	/**
	 * @param filter The filter to set.
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	public boolean isUnified() {
		return unified;
	}
	public void setUnified(boolean unified) {
		this.unified = unified;
	}
}
