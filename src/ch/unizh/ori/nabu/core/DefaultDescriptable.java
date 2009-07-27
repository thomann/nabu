/*
 * Created on 31.03.2004
 */
package ch.unizh.ori.nabu.core;

import java.io.Serializable;

/**
 * @author pht
 */
public class DefaultDescriptable implements Descriptable, Serializable {

	private String id, name, description;
	
	public DefaultDescriptable(){
	}

	public DefaultDescriptable(String id, String name) {
		setId(id);
		setName(name);
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}
	
	/**
	 *
	 */

	public String toString() {
		return getName();
	}


}
