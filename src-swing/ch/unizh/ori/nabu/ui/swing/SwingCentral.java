/*
 * Created on 31.03.2004
 */
package ch.unizh.ori.nabu.ui.swing;

import ch.unizh.ori.nabu.core.Central;

/**
 * @author pht
 */
public class SwingCentral extends Central {
	
	private static SwingCentral sw;
	
	public static SwingCentral getDefault(){
		if (sw == null) {
			sw = new SwingCentral();
		}
		return sw;
	}
	
	public SwingCentral(){
	}

	public void readFiles(){
		readDir("etc");
	}

}
