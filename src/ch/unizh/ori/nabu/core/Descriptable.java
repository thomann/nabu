/*
 * Created on 31.03.2004
 */
package ch.unizh.ori.nabu.core;

import java.io.Serializable;

/**
 * @author pht
 */
public interface Descriptable extends Serializable {

	public String getId();
	public void setId(String id);

	public String getName();
	public void setName(String name);

	public String getDescription();
	public void setDescription(String description);

}
