/*
 * Created on 19.10.2005
 *
 */
package ch.unizh.ori.common.prefs;

import ch.unizh.ori.nabu.core.Descriptable;

public interface Preferences extends Descriptable {
	
	public Preferences getParent();

	public Object getPref(String name, Object def);
	
	public Preferences providingPreferences(String name);
	
	public void setPref(String name, Object value);
	
	public PrefDesc getPrefDesc(String name);

	public String getPrefString(String name, String def);

	public int getPrefInt(String name, int def);
	
	public boolean isPref(String name, boolean def);
	
	public Object getDirectPref(String name);

}