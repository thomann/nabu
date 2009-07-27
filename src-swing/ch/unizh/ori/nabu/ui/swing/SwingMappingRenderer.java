/*
 * DefaultSwingRenderer.java
 *
 * Created on 13. April 2003, 16:12
 */

package ch.unizh.ori.nabu.ui.swing;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import ch.unizh.ori.common.text.Script;
import ch.unizh.ori.nabu.core.MappingRenderer;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.Sotm;
import ch.unizh.ori.nabu.voc.StringColumn;
import ch.unizh.ori.nabu.voc.Voice;

/**
 *
 * @author  pht
 */
public class SwingMappingRenderer extends MappingRenderer implements SwingRenderer, ActionListener{
	
	private NabuSession session;
	private JPanel theComponent = new JPanel();
    
    private Map comps = new HashMap();
    
    public SwingMappingRenderer(Mode m) {
    	super(m);
        //initComponents();
        initTheComponent();
    }
    
    protected void initTheComponent(){
    	theComponent.setLayout(new GridBagLayout());
    	
    	GridBagConstraints gcC = new GridBagConstraints();
    	gcC.weighty = 1;
		gcC.weightx = 1;
		gcC.gridwidth = GridBagConstraints.REMAINDER;
		gcC.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints gcL = new GridBagConstraints();
		gcL.weighty = 1;
		gcL.gridwidth = 1;
		gcL.weightx = 0;
		gcL.fill = GridBagConstraints.HORIZONTAL;
		gcL.insets = new Insets(0, 0, 0, 5);
		
    	for (Iterator iter = getModeFields().iterator(); iter.hasNext();) {
			ModeField mf = (ModeField) iter.next();
			String label = mf.getLabel();
			JLabel labelLabel = new JLabel(label+":");
			JComponent c = null;
			Voice v = null;
			if(mf.isAsking()){
				JTextField tf = new JTextField(){
				    public void paintComponent(Graphics g) {
						Graphics2D g2D = (Graphics2D) g;
						if (true) {
							g2D.setRenderingHint(
									RenderingHints.KEY_ANTIALIASING,
									RenderingHints.VALUE_ANTIALIAS_ON);
						} else {
							g2D.setRenderingHint(
									RenderingHints.KEY_ANTIALIASING,
									RenderingHints.VALUE_ANTIALIAS_OFF);
						}
						super.paintComponent(g2D);
					}
				};
				c = tf;
				tf.addActionListener(this);
				tf.addKeyListener(doubleSpaceListener);
				StringColumn col = (StringColumn)mf.getMode().getVoc().getColumn(mf.getKey());
				Script s = mf.getMode().getVoc().getCentral().getScript(col.getScript());
				if(s != null && s.getLocale() != null){
					c.addFocusListener(new LocaleSettingFocusListener(s));
					c.setLocale(s.getLocale());
				}
				if(label.length() > 0){
					labelLabel.setDisplayedMnemonic(label.charAt(0));
				}
				c.setFont(c.getFont().deriveFont(48f));
			}else{
				c = new JLabel(){
				    public void paintComponent(Graphics g) {
						Graphics2D g2D = (Graphics2D) g;
						if (true) {
							g2D.setRenderingHint(
									RenderingHints.KEY_ANTIALIASING,
									RenderingHints.VALUE_ANTIALIAS_ON);
						} else {
							g2D.setRenderingHint(
									RenderingHints.KEY_ANTIALIASING,
									RenderingHints.VALUE_ANTIALIAS_OFF);
						}
						super.paintComponent(g2D);
					}
				};
				c.setFont(c.getFont().deriveFont(48f));
				Sotm sotm = mf.getColumn().getVoc().getSound(mf.getKey());
				if(sotm != null && sotm.getVoices().size()>0){
					v =  (Voice) sotm.getVoices().get(0);
				}
			}
			comps.put(mf.getKey(), c);
			labelLabel.setLabelFor(c);
			
			theComponent.add(labelLabel, gcL);
			if(v == null){
				theComponent.add(c, gcC);
			}else{
				gcC.gridwidth = GridBagConstraints.RELATIVE;
				theComponent.add(c, gcC);
				gcC.gridwidth = GridBagConstraints.REMAINDER;

				String playName = "Play";
				char mnemonic = 0;
				if(label.length() > 0){
					mnemonic = label.charAt(0);
					playName += " "+label;
				}
				JButton b = new JButton(playName);
				if(mnemonic != 0){
					b.setMnemonic(mnemonic);
				}
				
				b.addActionListener(new PlayListener(this, mf));
				gcL.gridwidth = GridBagConstraints.REMAINDER;
				theComponent.add(b, gcL);
				gcL.gridwidth = 1;
//				labelLabel.setLabelFor(b);
			}
		}
    }
    
    private static final class LocaleSettingFocusListener implements
			FocusListener {
    	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
				.getLogger(LocaleSettingFocusListener.class);
    	private Script script;
		public void focusGained(FocusEvent e) {
			Locale locale = script.getLocale();
			log.debug(locale +" <- "+ e.getComponent().getInputContext().getLocale());
			boolean b = e.getComponent().getInputContext().selectInputMethod(locale);
//			System.out.println(b);
		}

		public void focusLost(FocusEvent e) {
		}

		public LocaleSettingFocusListener(Script script) {
			super();
			this.script = script;
		}
	}

	public static class PlayListener implements ActionListener{
    	
    	private ModeField mf;
    	private SwingMappingRenderer renderer;
    	
    	public PlayListener(SwingMappingRenderer renderer, ModeField mf){
    		this.renderer = renderer;
    		this.mf = mf;
    	}

		public void actionPerformed(ActionEvent e) {
			renderer.play(mf);
			renderer.focus();
		}
    }

    private void answerTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_answerTextFieldActionPerformed
        getSession().ok();
    }

    private Map lastAnswer = null;
    
    public void setQuestion(Object q){
        super.setQuestion(q);
        if(comps==null){
        	return;
        }
        for (Iterator iter = getModeFields().iterator(); iter.hasNext();) {
			ModeField mf = (ModeField) iter.next();
			if(!mf.isAsking()){
				JLabel c = (JLabel)comps.get(mf.getKey());
				String val = "";
				if(getPresentedQuestion() != null && getPresentedQuestion().get(mf.getKey()) != null){
					val = ((String)getPresentedQuestion().get(mf.getKey()));
				}
				c.setText(val);
			}else{
				JTextField c = (JTextField)comps.get(mf.getKey());
//				c.addKeyListener(dirtyListener);
//				c.addCaretListener(dirtyListener);
				if(q != null){
					c.getDocument().addDocumentListener(dirtyListener);
				}else{
					c.getDocument().removeDocumentListener(dirtyListener);
				}
				c.setText("");
				c.setEditable(true);
			}
		}
    }
    
    private DoubleSpaceListener doubleSpaceListener = new DoubleSpaceListener();
    private class DoubleSpaceListener implements KeyListener {
    	
    	private long lastHit;
    	private long threshold = 500;

		public void keyTyped(KeyEvent e) {
			if(e.getKeyChar() != ' '){
				lastHit = -1;
				return;
			}
			long time = e.getWhen();
			if(time-lastHit > threshold){
				lastHit = time;
			}else{
				lastHit = -1;
				JTextComponent component = (JTextComponent)e.getComponent();
				try {
					component.getDocument().remove(component.getCaretPosition()-1, 1);
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
				e.consume();
				getSession().ok();
			}
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}
    	
    }
    private DirtyListener dirtyListener = new DirtyListener();
    private class DirtyListener implements KeyListener, CaretListener, DocumentListener {

    	public void listen(Object o){
    		if (o instanceof KeyEvent) {
				KeyEvent k = (KeyEvent) o;
				if(k.getKeyChar() == KeyEvent.VK_ENTER){
					return;
				}
			}
    		setDirty(true);
//    		System.err.println("dirty = true; e: "+o);
    	}

		public void keyPressed(KeyEvent e) {
			listen(e);
		}
		public void keyReleased(KeyEvent e) {
			listen(e);
		}
		public void keyTyped(KeyEvent e) {
			listen(e);
		}
		public void caretUpdate(CaretEvent e) {
			listen(e);
		}

		public void changedUpdate(DocumentEvent e) {
			listen(e);
		}
		public void insertUpdate(DocumentEvent e) {
			listen(e);
		}
		public void removeUpdate(DocumentEvent e) {
			listen(e);
		}
    	
    }

	private void play(ModeField mf) {
		if(!getSession().isPlay())
			return;
		Sotm sotm = mf.getColumn().getVoc().getSound(mf.getKey());
		if(sotm != null && sotm.getVoices().size()>0){
			int i = (int)(Math.random()*sotm.getVoices().size());
			Voice v = (Voice) sotm.getVoices().get(i);
			String toSay = null;
			if (mf.getColumn() instanceof StringColumn) {
				StringColumn sc = (StringColumn) mf.getColumn();
				toSay = (String) getQuestion().get(mf.getColumn().getId());
			}
			String name = sotm.getUtterance(toSay, getQuestion());
			String prefix = v.getPrefix();
		
			if(name != null){
				String s = prefix + name;
				try {
					URL url = new URL(mf.getColumn().getVoc().getBase(),s);
					AudioClip ac = Applet.newAudioClip(url);
					new Thread(new PlayThread(ac)).start();
				} catch (MalformedURLException e) {
				}
			}
		}
	}
	
	public static class PlayThread implements Runnable{
		private AudioClip ac;
		
		private static final Object mutex = new Object();

		public PlayThread(AudioClip ac) {
			this.ac = ac;
		}
		
		public void run(){
			synchronized (mutex) {
				ac.play();
			}
		}
		
	}
    
    public void showSolution() {
		for (Iterator iter = getAnswerKeys().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
            JTextField c = (JTextField)comps.get(key);
            c.setText((String)getPresentedQuestion().get(key));
            c.setEditable(false);
        }
    }    
    
    public void activate() {
		focus();
        SwingUtilities.invokeLater(new Runnable(){
        	public void run() { playAll(); };
        });
    }

	private void focus() {
		if(getFocusKey() != null){
		    JTextField c = (JTextField)comps.get(getFocusKey());
		    c.selectAll();
		    c.requestFocus();
		}
	}
    
    public void playAll(){
    	if(getQuestion() == null){
    		return;
    	}
    	for (Iterator iter = getModeFields().iterator(); iter.hasNext();) {
			ModeField mf = (ModeField) iter.next();
			if(!mf.isAsking()){
				play(mf);
			}
		}
    }
    
	public JComponent getComponent() {
		return theComponent;
	}

	public void setSession(NabuSession session) {
		this.session = session;
	}
	
	public NabuSession getSession(){
		return session;
	}

	public void actionPerformed(ActionEvent e) {
		getSession().ok();
	}
    
	public void process(boolean showSolution) {
		for (Iterator iter = getAnswerKeys().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			JTextField tf = (JTextField) comps.get(key);
			setUserAnswerValue(key, tf.getText());
		}
		super.process(showSolution);
	}

}
