/*
 * Created on Aug 6, 2004
 *
 */
package ch.unizh.ori.nabu.voc;

/**
 * @author pht
 *
 */
public class ImgColumn extends AbstractColumn {
	
	private String prefix = "";

	/**
	 * @return Returns the prefix.
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * @param prefix The prefix to set.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public boolean isImage(){
		return true;
	}
	
	public Object map(String[] arr) {
		if(getColumn() >= arr.length){
			return null;
		}
		return arr[getColumn()];
	}

}
