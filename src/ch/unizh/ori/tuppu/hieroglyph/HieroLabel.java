/*
 * HieroLabel.java
 *
 * Created on 8. M�rz 2003, 18:43
 */

package ch.unizh.ori.tuppu.hieroglyph;

//import ch.unizh.ori.nabu.tupp.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import ch.unizh.ori.tuppu.Box;
import ch.unizh.ori.tuppu.Plotter;

/**
 *
 * @author  pht
 */
public class HieroLabel extends javax.swing.JPanel {
    
    /** Holds value of property text. */
    private String text;
    
    private HieroPlotter plotter = null;
    private Box renderer = null;
    
    /** Holds value of property fontSize. */
    private int fontSize = 20;
    
    private HieroPlotter getPlotter(Graphics2D fontGraphics){
        if(plotter == null){
            plotter = new HieroPlotter(fontGraphics, getFontSize());
        }
        return plotter;
    }
    
    private Box getRenderer(Graphics2D fontGraphics){
        if(renderer == null && getText() != null){
            renderer = getPlotter(fontGraphics).constructBox(fontGraphics, getText(), null, null);
        }
        return renderer;
    }
    
    /** Creates new form HieroLabel */
    public HieroLabel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        
        setLayout(new java.awt.BorderLayout());
        
    }//GEN-END:initComponents

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(getText() == null)
            return;
        Plotter.paint(g2, getRenderer(g2));
    }
    
    public Dimension getPreferredSize(){
        return getMinimumSize();
    }
    
    public Dimension getMaximumSize(){
        return getMinimumSize();
    }
    
    public Dimension getMinimumSize(){
        if(getText() == null){
            return new Dimension(1,1);
        }
        Graphics2D g2 = (Graphics2D)getGraphics();
        Dimension dim = getRenderer(g2).getSize();
        g2.dispose();
        return dim;
    }
    
    public static class MyDocumentListener implements DocumentListener{
        
        private HieroLabel hl;
        
        public MyDocumentListener(HieroLabel hl){
            this.hl = hl;
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
        
        public void change(DocumentEvent documentEvent){
            javax.swing.text.Document doc = documentEvent.getDocument();
            try{
            hl.setText(doc.getText(0,doc.getLength()));
            }catch(javax.swing.text.BadLocationException ex){
                ex.printStackTrace();
            }
        }
        
    }
    public HieroLabel.MyDocumentListener documentListener = new MyDocumentListener(this);
    
    public void addTextField(javax.swing.text.JTextComponent tc){
        tc.getDocument().addDocumentListener(documentListener);
    }
    
    /** Getter for property text.
     * @return Value of property text.
     */
    public String getText() {
        return this.text;
    }
    
    /** Setter for property text.
     * @param text New value of property text.
     */
    public void setText(String text) {
        if(text == null || !text.equals(this.text)){
            Graphics2D g2 = (Graphics2D)getGraphics();
            if(g2 != null){
                renderer = getPlotter(g2).constructBox(g2, text);
            }
        }
        this.text = text;
        repaint();
    }    
    
    /** Getter for property fontSize.
     * @return Value of property fontSize.
     */
    public int getFontSize() {
        return this.fontSize;
    }
    
    /** Setter for property fontSize.
     * @param fontSize New value of property fontSize.
     */
    public void setFontSize(int fontSize) {
        if(fontSize != this.fontSize){
            plotter = null;
            renderer = null;
        }
        this.fontSize = fontSize;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args){
        String code = "sDm-n:f";
        if(args.length == 1){
            code = args[0];
        }
        JFrame f = new JFrame("Hiero: "+code);
        final HieroLabel l = new HieroLabel();
        final JTextField tf = new JTextField(code);
        f.getContentPane().add(l);
        f.getContentPane().add(tf, BorderLayout.NORTH);
        f.setSize(400,400);
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation((screenSize.width-400)/2,(screenSize.height-400)/2);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        tf.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                l.setText(tf.getText());
            }
        });

        f.setVisible(true);
        
        l.setBackground(Color.white);
        l.setFontSize(40);
        l.setText(code);
    }
    
}
