/*
 * Created on 08.03.2004
 */
package ch.unizh.ori.common.text;

/**
 * @author pht
 */
public abstract class DefaultEncoding implements Encoding {
	
	private String id;
	private String name;
	private String description;
	private Script script;
	

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
	public Script getScript() {
		return script;
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
	 * @param script
	 */
	public void setScript(Script script) {
		this.script = script;
	}

	/**
	 * @param text
	 * @return
	 */
	public abstract Object convert(Text text);

	/**
	 * @param param
	 * @return
	 */
	public abstract Text create(Object param);

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

}
