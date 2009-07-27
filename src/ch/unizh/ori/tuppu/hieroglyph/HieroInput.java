package ch.unizh.ori.tuppu.hieroglyph;

//import ch.unizh.ori.nabu.tupp.HieroPlotter;

import java.util.*;
import java.util.List;

import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class HieroInput extends JFrame implements DocumentListener, ActionListener {

    //JLabel hieroL = new JLabel();
    //HieroComponent hieroL = new HieroComponent();
    HieroLabel hieroL = new HieroLabel();
    JTextField inputTF = new JTextField();
    
    public static final int SIZE = 46;
    
    Map classnames = null;
    List codes = null;
    
    public static class InsertAction extends AbstractAction{
        private JTextField field;
        private String str;
        public InsertAction(JTextField field, String str, String name){
            super(name);
            this.field = field;
            this.str = str;
        }
        public void actionPerformed(ActionEvent e){
            int offset=0, len=0;
            offset = field.getSelectionStart();
            len = field.getSelectionEnd() - offset;
            try{
                if(len > 0)
                    field.getDocument().remove(offset, len);
                field.getDocument().insertString(offset, str, null);
                field.requestFocus();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
    public static class Code {
        public String entree, phon, caractere, symbole;
        public Code(String e, String p, String c, String s){
            entree = e;
            phon = p;
            caractere = c;
            symbole = s;
        }
        public Code(String[] args){
            int i=0;
            entree = args[i++];
            phon = args[i++];
            caractere = args[i++];
            symbole = args[i++];
        }
    }
    
    /*public static class HieroComponent extends JComponent{
        
        public String text = "";
        
        public void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            HieroPlotter hp = new HieroPlotter(g2, HieroInput.SIZE);
            HieroPlotter.Box r = hp.constructRenderer(g2,
                text, new Font("Dialog", Font.PLAIN, HieroInput.SIZE), null);
            hp.paint(g2, r);
        }
        
    }*/

    public HieroInput(){
        super("HieroInput");
        getContentPane().setLayout(new BorderLayout());
        
        JPanel textP = new JPanel(new GridLayout(0,1));
        textP.add(hieroL);
        hieroL.setPreferredSize(new Dimension(100, SIZE*2));
        textP.add(inputTF);
        getContentPane().add(textP, BorderLayout.NORTH);
        
        getContentPane().add(getTabbedPane(), BorderLayout.CENTER);
        
        /*JMenuBar menuBar = new JMenuBar();
        JMenu hieroMenu = new JMenu("Hiero");
        menuBar.add(hieroMenu);
        populateMenu(hieroMenu);
        this.setJMenuBar(menuBar);*/
        
        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);
        
        inputTF.addActionListener(this);
        inputTF.getDocument().addDocumentListener(this);
    }
    
    public JComponent getTabbedPane(){
    	JTabbedPane tab = new JTabbedPane(JTabbedPane.LEFT);
        Map map = readClassnames();
        Map tabs = new HashMap();
        Iterator iter = new TreeSet(map.keySet()).iterator();
        while(iter.hasNext()){
            String cl = (String)iter.next();
            String name = (String)map.get(cl);
            JPanel pan = new JPanel(new GridLayout(0,6));
            tabs.put(cl, pan);
            tab.addTab(cl+": "+name, new JScrollPane(pan, 
            	JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        }
        Map itemMap = new HashMap();
        String imgSrc = "signs/icons/";
        List list = readCodes();
        iter = list.iterator();
        while(iter.hasNext()){
            Code c = (Code)iter.next();
            String key = c.caractere + c.symbole;
            //Action action = (Action)itemMap.get(key);
            JButton butt = (JButton)itemMap.get(key);
            if(butt == null){
                //Icon icon = new ImageIcon(imgSrc+key+".jpg");
                ActionListener action = new InsertAction(inputTF, c.entree, c.entree);
                butt = new JButton();
                itemMap.put(key, butt);
                butt.addActionListener(action);
                //System.out.println(c.caractere+" "+c.symbole+",  "+HieroPlotter.FontMapper.getBaseFont(c.caractere));
				//butt.setFont(FontMapper.getBaseFont(c.caractere).deriveFont(30f));
				butt.setFont(null);
                butt.setText(String.valueOf((char)(Integer.parseInt(c.symbole)+31)));
                butt.setToolTipText(c.entree);
                ((JPanel)tabs.get(c.caractere)).add(butt);
                //((JPanel)tabs.get(c.caractere)).add(new JButton(action));
            }else{
                //action.putValue(Action.NAME,action.getValue(Action.NAME)+", "+c.entree);
                butt.setToolTipText(butt.getToolTipText() + ", " + c.entree);
            }
        }
        ToolTipManager.sharedInstance().setInitialDelay(0);
        return tab;
    }
    
/*    protected void populateMenu(JMenu menu){
        Iterator iter;
        Map map = readClassnames();
        iter = new TreeSet(map.keySet()).iterator();
        Map menuMap = new HashMap();
        while(iter.hasNext()){
            String cl = (String)iter.next();
            JMenu subMenu = new JMenu(cl+": "+map.get(cl));
            menu.add(subMenu);
            menuMap.put(cl, subMenu);
        }
        
        Map itemMap = new HashMap();
        String imgSrc = "signs/icons/";
        List list = readCodes();
        iter = list.iterator();
        while(iter.hasNext()){
            Code c = (Code)iter.next();
            String key = c.caractere + c.symbole;
            JMenuItem item = (JMenuItem)itemMap.get(key);
            if(item == null){
                Icon icon = new ImageIcon(imgSrc+key+".jpg");
                InsertAction act = new InsertAction(inputTF, key, key, icon);
                item = new JMenuItem(act);
                ((JMenu)menuMap.get(c.caractere)).add(item);
            }else{
                Action action = item.getAction();
                action.putValue(Action.NAME,action.getValue(Action.NAME)+", "+c.entree);
            }
        }
    }*/
    
    protected Map readClassnames(){
    	if(classnames != null){
        	return classnames;
        }
        String filename = "classnames.txt";
        Map map = new HashMap();
        try{
            //BufferedReader in = new BufferedReader(new FileReader(filename));
            BufferedReader in = new BufferedReader(new InputStreamReader(HieroInput.class.getResourceAsStream(filename)));
            String line;
            char ch = 'A';
            while( (line=in.readLine()) != null ){
            	if(line.length() >= 1){
                	map.put(String.valueOf(ch), line);
                }
                ch++;
            }
            ch--;
            map.put("Aa",map.remove(String.valueOf(ch)));
            in.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return classnames = map;
    }
    
    protected List readCodes(){
    	if(codes != null){
        	return codes;
        }
        List list = new ArrayList();
        String filename = "seshSource.txt";
        try{
           //BufferedReader in = new BufferedReader(new FileReader(filename));
           BufferedReader in = new BufferedReader(new InputStreamReader(HieroInput.class.getResourceAsStream(filename)));
           String line;
           while( (line=in.readLine()) != null ){
                String[] fields = line.split("\\s+");
                Code c = new Code(fields);
                list.add(c);
            }
            in.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return codes = list;
    }
    
    public void changedUpdate(DocumentEvent e){
        updateLabel();
    }
    public void insertUpdate(DocumentEvent e){
        updateLabel();
    }
    public void removeUpdate(DocumentEvent e){
        updateLabel();
    }
    public void actionPerformed(ActionEvent e){
        updateLabel();
    }
    
    public void updateLabel(){
        String text = inputTF.getText();
        hieroL.setText(text);
        hieroL.repaint();
        /*try{
            URL url = new URL("http://127.0.0.1/cgi-bin/hiero/hiero.pl?text="+text);
            hieroL.setIcon(new ImageIcon(url,"text"));
        }catch(Exception ex){
            ex.printStackTrace();
        }*/
    }


    public static void main(String[] args){
        HieroInput hi = new HieroInput();
        hi.setBounds(100,100,300,100);
        hi.setVisible(true);
        
        hi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

