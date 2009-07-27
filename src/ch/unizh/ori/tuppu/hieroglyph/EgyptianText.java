/*
 * EgyptianText.java
 *
 * Created on 25. September 2002, 10:46
 */

package ch.unizh.ori.tuppu.hieroglyph;

import ch.unizh.ori.common.text.*;

/**
 *
 * @author  pht
 */
public class EgyptianText{
    
    private String manCode;
    private Text translit;

    public static String imageUrl;

    /** Creates a new instance of EgyptianText */
    public EgyptianText(String manCode, String translit) {
        this.manCode = manCode;
        this.translit = new StringText(null, translit);
    }
    
    public String getAsciiString() {
        return manCode;
    }
    
    public String getImageURL() {
        return getImageURL(manCode);
    }
    
    public static String getImageURL(String string) {
        try{
            return imageUrl + java.net.URLEncoder.encode(string, "UTF-8");
        }catch(java.io.UnsupportedEncodingException ex){
            return "";
        }
    }
    
    public static void setImageURL(String imageUrl){
        EgyptianText.imageUrl = imageUrl;
    }
    
    public String getUnicodeString() {
        return manCode;
    }
    
    public Text getTransliteration() {
        return translit;
    }
    
//    public static Text string2Text(String code, String translit){
//        return new EgyptianText(code, translit);
//    }

//    public void speak() {
//        Speak.getDefault().speak(string2speech(translit..getUnicodeString()));
//    }
    
    private static final String SCHWA = "e";
    public static String string2speech(String s){
    	if(s == null) return null;
        StringBuffer ret = new StringBuffer(s.length());
        boolean wasCons = false;
        theLoop: for(int i=0; i<s.length(); i++){
            char c = s.charAt(i);
            switch(c){
                case 'a':
                case 'A':
                    if(comes("aA",s,i)){
                        ret.append("a ");
                    }else{
                        ret.append("a");
                    }
                    wasCons = false;
                    break;
                case 'i':
                case 'j':
                case 'y':
                    if(!comes("aAijw",s,i)){
                        ret.append("ee");
                        wasCons = false;
                    }else{
                        ret.append("y");
                        wasCons = true;
                    }
                    break;
                case 'w':
                    if(!comes("aAijw",s,i)){
                        ret.append("ooh");
                        wasCons = false;
                    }else{
                        ret.append("wh");
                        wasCons = true;
                    }
                    break;
                case 'h':
                case 'H':
                case 'x':
                case 'X':
                    if(wasCons){ret.append(SCHWA);}
                    ret.append("kh");
                    wasCons = true;
                    break;
                case 'q':
                    if(wasCons){ret.append(SCHWA);}
                    ret.append("k");
                    wasCons = true;
                    break;
                case 'S':
                    if(wasCons){ret.append(SCHWA);}
                    ret.append("sh");
                    wasCons = true;
                    break;
                case 'T':
                    if(wasCons){ret.append(SCHWA);}
                    ret.append("ty");
                    wasCons = true;
                    break;
                case 'D':
                    if(wasCons){ret.append(SCHWA);}
                    ret.append("j");
                    wasCons = true;
                    break;
                case 'b':
                case 'p':
                case 'f':
                case 'm':
                case 'n':
                case 'r':
                case 'z':
                case 's':
                case 'k':
                case 'g':
                case 't':
                case 'd':
                    if(wasCons){ret.append(SCHWA);}
                    ret.append(c);
                    wasCons = true;
                    break;
                case '(':
                    break theLoop;
                default:
                    ret.append(c);
                    wasCons = false;
                    break;
            }
        }
//        if(DEBUG)System.err.println("eg: "+s+" -> "+ret);
        return ret.toString();
    }
    
    private static boolean comes(String pattern, String s, int i){
        if(i+1==s.length()) return false;
        return pattern.indexOf(s.charAt(i+1)) >= 0;
    }
    
}
