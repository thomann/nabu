/*
 * Created on 04.03.2004
 */
package ch.unizh.ori.common.prefs;

import java.util.HashMap;
import java.util.Map;

import ch.unizh.ori.nabu.core.DefaultDescriptable;

/**
 * @author pht
 */
public class DefaultPreferences extends DefaultDescriptable implements Preferences {
	
	private Preferences parent = null;
	
	// Has this to be synchronized?
	private Map prefs = new HashMap();
	
	public Object getPref(String name, Object def){
		Preferences providingPreferences = providingPreferences(name);
		if(providingPreferences == null)
			return def;
		return providingPreferences.getDirectPref(name);
	}
	
	public void setPref(String name, Object value){
		if(value != null){
			prefs.put(name, value);
		}else{
			// we don't have null values!
			prefs.remove(name);
		}
	}
	
	public Preferences providingPreferences(String name){
		do{
			Preferences preferences = this;
			do{
				if(preferences.getDirectPref(name)!=null)
					break;
				preferences = preferences.getParent();
			}while(preferences != null);
			if(preferences != null)
				return preferences;
			int i = name.lastIndexOf(' ');
			if(i < 0)
				name = null;
			else
				name = name.substring(0, i);
		}while (name != null);
		return null;
	}
	
	public String getPrefString(String name, String def){
		Object o = getPref(name, def);
		return (o==null)?def:o.toString();
	}
	
	public int getPrefInt(String name, int def){
		Object o = getPref(name, null);
		if (o instanceof Number) {
			Number n = (Number) o;
			return n.intValue();
		}else if (o instanceof String) {
			String s = (String) o;
			return Integer.parseInt(s);
		}else if(o != null){
			throw new IllegalArgumentException();
		}
		return def;
	}

	public boolean isPref(String name, boolean def) {
		Object o = getPref(name, null);
		if(o==null)
			return def;
		if (o instanceof Boolean) {
			return ((Boolean) o).booleanValue();
		}else if (o instanceof String) {
			String s = (String) o;
			if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("on")
					|| s.equalsIgnoreCase("true"))
				return true;
			if (s.equalsIgnoreCase("no") || s.equalsIgnoreCase("off")
					|| s.equalsIgnoreCase("false"))
				return false;
			throw new IllegalArgumentException("Boolean value from String "+s+" not recognized");
		}else{
			throw new IllegalArgumentException("Cannot parse value "+o);
		}
	}

	public Preferences getParent() {
		return parent;
	}

	public void setParent(Preferences parent) {
		this.parent = parent;
	}

	public PrefDesc getPrefDesc(String name) {
		return null;
	}

	public Object getDirectPref(String name) {
		return prefs.get(name);
	}

}
