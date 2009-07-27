/**
 * @author pht
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package ch.unizh.ori.common.text.helper;
import java.io.*;

import javax.swing.*;

import ch.unizh.ori.common.text.OldStringText;
import ch.unizh.ori.common.text.OldText;
import ch.unizh.ori.common.text.Script;
import ch.unizh.ori.common.text.ScriptUtilities;
import ch.unizh.ori.common.text.Transliteration;
import ch.unizh.ori.nabu.core.DefaultDescriptable;

import java.awt.*;
import java.awt.event.*;


public class AkkadianHelper extends DefaultDescriptable implements Transliteration {

	public static String ascii2unicode(String in){
		char[] arr = in.toCharArray();
		for(int i=0; i<arr.length; i++){
			switch (arr[i]) {
				case '.' :
					if(i+1 < arr.length && "st".indexOf(arr[i+1])>=0 ){
						arr[i] = arr[i+1];
						arr[i+1] = '\u0323';
					}
					break;

				case '_' :
					if(i+1 < arr.length){
						if( "aeiou".indexOf(arr[i+1])>=0 ){
							arr[i] = arr[i+1];
							arr[i+1] = '\u0304';
						}else if(arr[i+1]=='h'){
							arr[i] = 'h';
							arr[i+1] = '\u0331';
						}
					}
					break;

				case '^' :
					if(i+1 < arr.length){
						if( "aeiou".indexOf(arr[i+1])>=0 ){
							arr[i] = arr[i+1];
							arr[i+1] = '\u0302';
						}else if(arr[i+1]=='s'){
							arr[i] = 's';
							arr[i+1] = '\u030c';
						}
					}
					break;

				default :
					break;
			}
		}
		return ScriptUtilities.compose(new String(arr));
	}

	public static String unicode2ascii(String in){
		char[] arr = in.toCharArray();
		for(int i=1; i<arr.length; i++){
			switch (arr[i]) {
				case '\u0323' :
					if("st".indexOf(arr[i-1])>=0 ){
						arr[i] = arr[i-1];
						arr[i-1] = '.';
					}
					break;

				case '\u0304' :
					if( "aeiou".indexOf(arr[i-1])>=0 ){
						arr[i] = arr[i-1];
						arr[i-1] = '_';
					}
					break;
					
				case '\u0320' :
					if(arr[i-1]=='h'){
						arr[i] = '_';
						arr[i-1] = 'h';
					}
					break;

				case '\u0302' :
					if( "aeiou".indexOf(arr[i-1])>=0 ){
						arr[i] = arr[i-1];
						arr[i-1] = '^';
					}
					break;
				case '\u030c' :
					if(arr[i-1]=='s'){
						arr[i] = 's';
						arr[i-1] = '^';
					}
					break;

				default :
					break;
			}
		}
		return new String(arr);
	}
        
        public static OldText string2Text(String str){
            return new OldStringText(ascii2unicode(str));
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
		private JTextField asciiL = new JTextField();
		private JTextField unicodeL = new JTextField();
		
		public static void main(String[] args) {
			JFrame f = new GuiTest();
			f.setSize(400,200);
			f.setLocation(20,50);
			f.setVisible(true);
		}
		
		public void actionPerformed(ActionEvent e){
			String in = inTF.getText();
			asciiL.setText(unicode2ascii(in));
			unicodeL.setText(ascii2unicode(in));
		}
		
		public GuiTest(){
			super("Accadian-Helper");
			//asciiL.setFont(new Font("Courier", Font.PLAIN, 12));
			//unicodeL.setFont(new Font("Courier", Font.PLAIN, 12));
			
			JPanel p = new JPanel(new GridLayout(3,2));
			p.add(new JLabel("Input:"));
			p.add(inTF);
			p.add(new JLabel("Unicode:"));
			p.add(unicodeL);
			p.add(new JLabel("Ascii"));
			p.add(asciiL);
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

	public Object toStandard(Object foreign) {
		return unicode2ascii((String) foreign);
	}

	public Object toForeign(Object standard) {
		return ascii2unicode((String)standard);
	}
	
	private Script script;

	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
	}
}
