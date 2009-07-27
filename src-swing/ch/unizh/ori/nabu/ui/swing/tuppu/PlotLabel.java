/*
 * HieroLabel.java
 *
 * Created on 8. März 2003, 18:43
 */

package ch.unizh.ori.nabu.ui.swing.tuppu;

//import ch.unizh.ori.nabu.tupp.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ch.unizh.ori.tuppu.Plottable;
import ch.unizh.ori.tuppu.Plotter;
import ch.unizh.ori.tuppu.StringPlotter;

/**
 *
 * @author  pht
 */
public class PlotLabel extends JPanel {

	/** Holds value of property text. */
	private String text;
	private Map params;

	private Plotter plotter = null;
	private Plottable plottable = null;

	/** Creates new form HieroLabel */
	public PlotLabel() {
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Dimension s = getSize();
		Dimension d = getPlottable().getSize();
		g2.translate((s.width-d.width)/2, (s.height-d.height)/2);
		Plotter.plot(g2, getPlottable());
		g2.drawLine(-10,-10,10,-10);
	}

	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	public Dimension getMaximumSize() {
		return getMinimumSize();
	}

	public Dimension getMinimumSize() {
		Dimension dim = getPlottable().getSize();
		return dim;
	}

	public static class MyDocumentListener implements DocumentListener {

		private PlotLabel pl;

		public MyDocumentListener(PlotLabel hl) {
			this.pl = hl;
		}

		public void changedUpdate(DocumentEvent documentEvent) {
			change(documentEvent);
		}

		public void insertUpdate(DocumentEvent documentEvent) {
			change(documentEvent);
		}

		public void removeUpdate(DocumentEvent documentEvent) {
			change(documentEvent);
		}

		public void change(DocumentEvent documentEvent) {
			javax.swing.text.Document doc = documentEvent.getDocument();
			try {
				pl.setText(doc.getText(0, doc.getLength()));
			} catch (javax.swing.text.BadLocationException ex) {
				ex.printStackTrace();
			}
		}

	}

	public PlotLabel.MyDocumentListener documentListener = new MyDocumentListener(
			this);
	
	protected void doUpdate(){
		plottable = null;
		if(plotter != null){
			try {
				plottable = plotter.createPlottable(getText(), getParams());
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			plottable.getGraphicsProperties().configure(getParams(), "");
		}
		if(plottable == null)
			plottable = new Plottable.EmptyPlottable();
		revalidate();
		repaint();
	}

	public Plottable getPlottable() {
		if(plottable == null){
			plottable = new Plottable.EmptyPlottable();
		}
		return plottable;
	}
	public Plotter getPlotter() {
		return plotter;
	}
	public void setPlotter(Plotter plotter) {
		this.plotter = plotter;
		plotter.init();
		doUpdate();
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
		doUpdate();
	}
	public Map getParams() {
		return params;
	}
	public void setParams(Map params) {
		this.params = params;
		doUpdate();
	}
	
	public void setAll(Plotter plotter, String text, Map params){
		if(plotter != null){
			this.plotter = plotter;
			plotter.putInitParams(params);
			plotter.init();
		}else
			this.plotter = new StringPlotter();
		this.text = text;
		this.params = params;
		doUpdate();
	}
}