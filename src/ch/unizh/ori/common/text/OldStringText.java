/*
 * StringText.java
 *
 * Created on 25. September 2002, 11:06
 */

package ch.unizh.ori.common.text;

/**
 *
 * @author  pht
 */
public class OldStringText extends DefaultText {
    
    private String string;
    public static String imageUrl = "/string?string=";
    
    /** Creates a new instance of StringText */
    public OldStringText(String string) {
        this(string, null);
    }
    
    /** Creates a new instance of StringText */
    public OldStringText(String string, OldScript script) {
        this.string = string;
        setScript(script);
    }
    
    public String getAsciiString() {
        return string;
    }
    
    public String getImageURL() {
        return getImageURL(string);
    }
    
    public static String getImageURL(String string) {
        try{
            String str = imageUrl + java.net.URLEncoder.encode(string, "UTF-8");
            System.out.println("imageUrl: "+str);
            return str;
        }catch(java.io.UnsupportedEncodingException ex){
            return "";
        }
    }
    
    public static void setImageURL(String imageUrl){
        OldStringText.imageUrl = imageUrl;
    }
    
    public OldText getTransliteration() {
        return this;
    }
    
    public String getUnicodeString() {
        return string;
    }
    
        public static OldText string2Text(String str){
            return new OldStringText(str);
        }

}
