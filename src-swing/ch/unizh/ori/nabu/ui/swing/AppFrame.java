/*
 * Created on 12.04.2004
 */
package ch.unizh.ori.nabu.ui.swing;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

/**
 * @author pht
 */
public class AppFrame extends JFrame implements ComponentListener {
	
	private String key;
	
	public AppFrame(String title, String key, Dimension d){
		super(title);
		this.key = key;
		
		Preferences prefs = getPreferences();
		String value = prefs.get(key, null);
		if(value != null){
			try{
				String[] arr = value.split(",");
				int[] is = new int[4];
				for(int i=0; i<4; i++){
					is[i] = Integer.parseInt(arr[i]);
				}
				setBounds(is[0], is[1], is[2], is[3]);
			}catch(Throwable t){
				t.printStackTrace();
			}
			addComponentListener(this);
		}else{
			addComponentListener(this);
			if(d != null){
				setSize(d);
			}else{
				pack();
				d = getSize();
			}
			Dimension s = getToolkit().getScreenSize();
			setLocation((s.width-d.width)/2, (s.height-d.height)/2);
		}
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public static Preferences getPreferences(){
		Preferences prefs = Preferences.userNodeForPackage(AppFrame.class);
		return prefs;
	}

	private void saveLocation() {
		Rectangle r = getBounds();
		String s = r.x +","+ r.y +","+ r.width +","+ r.height;
//		System.err.println("saveLocation: "+s);
		getPreferences().put(key, s);
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
		saveLocation();
	}

	public void componentResized(ComponentEvent e) {
		saveLocation();
	}

	public void componentShown(ComponentEvent e) {
	}


}
