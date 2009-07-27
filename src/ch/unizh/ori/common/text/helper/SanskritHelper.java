/**
 * @author pht
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package ch.unizh.ori.common.text.helper;
import java.util.*;
import java.io.*;

import javax.swing.*;

import ch.unizh.ori.common.text.OldStringText;
import ch.unizh.ori.common.text.OldText;
import ch.unizh.ori.common.text.Script;
import ch.unizh.ori.common.text.Transliteration;
import ch.unizh.ori.nabu.core.DefaultDescriptable;

import java.awt.*;
import java.awt.event.*;


public class SanskritHelper extends DefaultDescriptable implements Transliteration {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(SanskritHelper.class);
	
	private final static Object LOCK = new Object();

    private static final int DEVA_BASE = 0x0900;
    private static final char VIRAMA = 0x094d;

    private static final String[] ASCII = {
        null, "~","M","H", null,
        "a", "aa", "i", "ii", "u", "uu", 
        "R", "L",
        "e?", "e??", "e", "ai",
        "o?", "o??", "o", "au",
        "k", "kh", "g", "gh", "~n",
        "c", "ch", "j", "jh", "~N",
        "T", "Th", "D", "Dh", "N",
        "t", "th", "d", "dh", "n", "n?",
        "p", "ph", "b", "bh", "m",
        "y", "r", "rr", "l", "ll", "lll", "v",
        "sh", "S", "s", "h",
        null, null, ".", "\'"
    };

	public static final String[] TR = {
		null, "~","m\u0323","h\u0323", null,
		"a", "a\u0304", "i", "\u012b", "u", "u\u0304", 
		"r\u0323", "l\u0323",
		"e?", "e??", "e", "ai",
		"o?", "o??", "o", "au",
		"k", "kh", "g", "gh", "n\u0307",
		"c", "ch", "j", "jh", "n\u0303",
		"t\u0323", "t\u0323h", "d\u0323", "d\u0323h", "n\u0323",
		"t", "th", "d", "dh", "n", "n?",
		"p", "ph", "b", "bh", "m",
		"y", "r", "rr", "l", "l\u0323", "lll", "v",
		"s\u0301", "s\u0323", "s", "h",
		null, null, ".", "\'"
	};
    
	public static final String[] TZ = {
		null, ":m",".m",".h", null,
		"a", "-a", "i", "-i", "u", "-u", 
		".r", ".l",
		"e?", "e??", "e", "ai",
		"o?", "o??", "o", "au",
		"k", "kh", "g", "gh", "~n",
		"c", "ch", "j", "jh", "~N",
		".t", ".th", ".d", ".dh", ".n",
		"t", "th", "d", "dh", "n", "n?",
		"p", "ph", "b", "bh", "m",
		"y", "r", "rr", "l", "ll", "lll", "v",
		"/s", ".s", "s", "h",
		null, null, ".", "\'",
		null, null, null, null, null, null, null, null,
		null, null, null, null, null, null, null, null,
		null, null, null, null, null, null, null, null,
		null, null,
		"q", "qh", "x", "z", ":r", ":rh", "f", "yy" 
	};
    
    private String[] lookup;
    
    private Map inverse;
    
    public SanskritHelper() {
		super();
	}

	public SanskritHelper(String id, String name) {
		super(id, name);
	}

	private SanskritHelper(String[] lookup){
    	this.lookup = lookup;
    }
    
    private Map getInverseMap(){
    	if (inverse == null) {
    		synchronized(LOCK){
    			if(inverse != null)
    				return inverse;
				inverse = new HashMap(lookup.length);
				for(int i=0; i<lookup.length; i++){
					char c = (char)(DEVA_BASE+i);
					if(lookup[i] != null){
						inverse.put(lookup[i], new Character(c));
					}
				}
    		}
		}
		return inverse;
    }
    
    private char getInverse(String str, int start){
    	Map ai = getInverseMap();
    	int len = 1;
    	char c = '\u0000';
    	while(start+len <= str.length()){
    		Object o = ai.get(str.substring(start, start+len));
    		if(o != null){
    			c = ((Character)o).charValue();
    			++len;
    		}else{
    			break;
    		}
    	}
    	return c;
    }
    
    private int getLength(char c){
    	int i = c-DEVA_BASE;
    	if( 0<=i && i < lookup.length){
    		String str = lookup[i];
    		return (str==null)?0:str.length();
    	}else{
    		return 0;
    	}
    }
    
    public static boolean isConsonant(char ch){
    	return (0x0915<=ch && ch<=0x0939) || (0x0958<=ch && ch<=0x095f);
    }
    
    public static boolean isVocal(char ch){
    	return 0x0905<=ch && ch<=0x0914;
    }
    
    public void initTables(){
    	getInverseMap();
    }
    
	public String toUnicode(String in){
		StringBuffer out = new StringBuffer(in.length());
		boolean wasCons = false;
		for(int i=0; i<in.length(); i++){
			char ch = getInverse(in, i);
			if(ch == '\u0000'){
				out.append(in.charAt(i));
                                wasCons = false;
				continue;
			}
			int len = getLength(ch);
			if(len == 0){
				log.error("SanskritHelper: len=0 !");
				len = 1;
			}
			
			if(isConsonant(ch)){
				wasCons = true;
				out.append(ch);
				char next = getInverse(in, i+len);
				if(next == '\u0905'){
					++len; // should be an 'a'!
					wasCons = false;
				}else if(!isVocal(next)){
					out.append(VIRAMA);
				}
			}else if(isVocal(ch)){
				if(wasCons){
					ch += '\u0038';
				}
				out.append(ch);
				wasCons = false;
			}else{
				wasCons = false;
				out.append(ch);
			}
			i += len - 1;
			
		}
		return out.toString();
	}

	public String fromUnicode(String in){
		StringBuffer out = new StringBuffer(in.length());
		for(int i=0; i<in.length(); i++){
			char ch = in.charAt(i);
			if(ch == VIRAMA){
				continue;
			}
			if(isVocal((char)(ch - '\u0038'))){
				ch -= '\u0038';
			}
			int ch_i = ch - 0x0900;
			if(0>ch_i || ch_i>lookup.length){
				out.append(ch);
				continue;
			}
			String s = lookup[ch_i];
			out.append(s);
			if(isConsonant(ch)){
				if(i == in.length()-1){
					out.append(lookup[5]); // should be "a".
					continue;
				}
				char next = in.charAt(i+1);
				if(next != VIRAMA && !isVocal((char)(next - '\u0038'))){
					out.append(lookup[5]); // should be "a".
				}
			}
		}
		
		return out.toString();
	}
	
	public void setId(String string) {
		super.setId(string);
		if("ascii".equals(string)){
			this.lookup = ASCII;
		}else if("pretty".equals(string)){
			this.lookup = TR;
		}else if("tz".equals(string)){
			this.lookup = TZ;
		}else{
			throw new IllegalArgumentException("Unknown transliteration: "+string);
		}
	}
	
	public Object toStandard(Object foreign) {
		return toUnicode((String) foreign);
	}

	public Object toForeign(Object standard) {
		return fromUnicode((String) standard);
	}
	private static SanskritHelper asciiHelper = new SanskritHelper(ASCII);
	private static SanskritHelper translitHelper = new SanskritHelper(TR);
	private static SanskritHelper tzHelper = new SanskritHelper(TZ);
	static{
		tzHelper.getInverseMap().put("-", new Character('-'));
		tzHelper.getInverseMap().put("/", new Character('/'));
		tzHelper.getInverseMap().put(".", new Character('.'));
		tzHelper.getInverseMap().put(":", new Character(':'));
		tzHelper.getInverseMap().put(";", new Character(';'));
		tzHelper.getInverseMap().put("?", new Character('?'));
	}

	public static String ascii2unicode(String in){
		return asciiHelper.toUnicode(in);
	}
	public static String unicode2ascii(String in){
		return asciiHelper.fromUnicode(in);
	}
	
	public static String translit2unicode(String in){
		return translitHelper.toUnicode(in);
	}
	public static String unicode2translit(String in){
		return translitHelper.fromUnicode(in);
	}
        
	public static String tz2unicode(String in){
		String ret = tzHelper.toUnicode(in);
		if(ret!=null){
			ret = ret.replaceAll("\\:\u0907","\u0907");
			ret = ret.replaceAll("\\:\u0908","\u0908");
			ret = ret.replaceAll("\\:\u0909","\u0909");
			ret = ret.replaceAll("\\:\u090a","\u090a");
		}
		return ret;
	}
	public static String unicode2tz(String in){
		return tzHelper.fromUnicode(in);
	}
        
        public static OldText string2Text(String str){
            String uni = ascii2unicode(str);
            return new OldStringText(uni+ " ("+str+", "+unicode2translit(uni)+")");
        }

	public static void main(String[] args) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line;
		try {
			while( (line = in.readLine()) != null){
				String l2 = ascii2unicode(line);
				System.out.println(l2);
				String l3 = unicode2ascii(l2);
				System.out.println(l3);
				System.out.println(l3.equals(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class GuiTest extends JFrame implements ActionListener{
		
		private JTextField inTF = new JTextField();
		private JTextField asciiCodeL = new JTextField();
		private JTextField asciiUnicodeL = new JTextField();
		private JTextField translitCodeL = new JTextField();
		private JTextField transalitUnicodeL = new JTextField();
		private JTextField tzCodeL = new JTextField();
		private JTextField tzUnicodeL = new JTextField();
		
		public static void main(String[] args) {
			JFrame f = new GuiTest();
			f.setSize(400,200);
			f.setLocation(20,50);
			f.setVisible(true);
		}
		
		public void actionPerformed(ActionEvent e){
			String in = inTF.getText();
			asciiCodeL.setText(unicode2ascii(in));
			asciiUnicodeL.setText(ascii2unicode(in));
			translitCodeL.setText(unicode2translit(in));
			transalitUnicodeL.setText(translit2unicode(in));
			tzCodeL.setText(unicode2tz(in));
			tzUnicodeL.setText(tz2unicode(in));
		}
		
		public GuiTest(){
			super("Indian-Helper");
			//asciiL.setFont(new Font("Courier", Font.PLAIN, 12));
			//unicodeL.setFont(new Font("Courier", Font.PLAIN, 12));
			
			JPanel p = new JPanel(new GridLayout(0,3));
			p.add(new JLabel("Input:"));
			p.add(inTF);
			p.add(new JPanel());
			
			p.add(new JLabel("Ascii:"));
			p.add(asciiCodeL);
			p.add(asciiUnicodeL);
			p.add(new JLabel("Translit:"));
			p.add(translitCodeL);
			p.add(transalitUnicodeL);
			p.add(new JLabel("TZ:"));
			p.add(tzCodeL);
			p.add(tzUnicodeL);
			getContentPane().add(p, BorderLayout.CENTER);
			
			p = new JPanel();
			JButton b = new JButton("Convert");
			p.add(b);
			getContentPane().add(p, BorderLayout.SOUTH);
			
			inTF.addActionListener(this);
			b.addActionListener(this);
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
	}

	private Script script;

	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
	}

}
