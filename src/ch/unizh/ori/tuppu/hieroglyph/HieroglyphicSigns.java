/*
 * HieroglyphicSigns.java
 *
 * Created on 25. Juli 2003, 16:17
 */

package ch.unizh.ori.tuppu.hieroglyph;

import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author  pht
 */
public class HieroglyphicSigns {
    
    private static HieroglyphicSigns _default = null;
    public static HieroglyphicSigns getDefault(){
        if(_default == null){
            _default = new HieroglyphicSigns();
        }
        return _default;
    }
    
    private URL base;
    
    
    /** Creates a new instance of HieroglyphicSigns */
    public HieroglyphicSigns() {
        this("/seshSource.txt");
    }
    
    public HieroglyphicSigns(String filename) {
    	this(find(filename));
    }
    
    public HieroglyphicSigns(URL filename){
    	base = filename;
        readEntries(filename);
    }
    
    public static URL find(String name){
    	URL ret = null;
    	ret = HieroglyphicSigns.class.getResource(name);
    	if(ret == null){
    		File f = new File(name);
    		if(f.exists() && f.canRead()){
    			try {
					ret = f.toURL();
				} catch (MalformedURLException ex) {
					ex.printStackTrace();
				}
    		}
    	}
    	return ret;
    }
    
    public static class Donne {
        public String entry;
        public String phone;
        public String font;
        public char ch;
        public Donne(){
        }
        public String getGardiner(){
            return font + (ch-31);
        }
        public String toString(){
            return getGardiner() + " " + entry + " " + phone;
        }
    }
    
    private Map donnes;
    public Map getDonnes(){
        return donnes;
    }
    
    public Donne getDonne(String e){
        return (Donne)getDonnes().get(e);
    }
    
    private Map phons;
    public List getPhon(String phon){
        return (List)phons.get(phon);
    }
    private List addPhon(String phon, Donne d){
        List l = getPhon(phon);
        if(l == null){
            l = new ArrayList();
            phons.put(phon, l);
        }
        l.add(d);
        return l;
    }
    
    private Map codes;
    public List getCodes(Donne d){
        return getCodes(d.getGardiner());
    }
    public List getCodes(String gard){
        return (List)codes.get(gard);
    }
    private List addCode(Donne d){
        List l = getCodes(d);
        if(l == null){
            l = new ArrayList();
            codes.put(d.getGardiner(), l);
        }
        l.add(d);
        return l;
    }
    
    private Map classes;
    public List getClasses(Donne d){
        return getClasses(d.font);
    }
    public List getClasses(String s){
        return (List)classes.get(s);
    }
    private void addClasses(Donne d){
        List l = getClasses(d);
        if(l == null){
            l = new ArrayList();
            classes.put(d.font, l);
            l.add(d);
        }else{
            Donne last = (Donne)l.get(l.size() - 1);
            if(last.ch == d.ch){
                l.set(l.size()-1, d);
            }else{
                l.add(d);
            }
        }
    }
    
    private void readEntries(URL filename){
        donnes = new HashMap();
        phons = new HashMap();
        codes = new HashMap();
        classes = new HashMap();
        InputStream in = null;
        try{
//            System.out.println(HieroPlotter.class.getResource(filename));
//            for(Enumeration enum = FontMapper.class.getClassLoader().getResources(filename); enum.hasMoreElements();){
//            	System.out.println(enum.nextElement());
//            }
            in = filename.openStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String line;
            while( (line=r.readLine()) != null ){
                StringTokenizer st = new StringTokenizer(line, "\t");
                Donne d = new Donne();
                d.entry = nextToken(st).trim();
                d.phone = nextToken(st);
                d.font = nextToken(st);
                try{
                    d.ch = (char)(Integer.parseInt(nextToken(st))+31);
                }catch(NumberFormatException ex){}
                if(d.entry != null){
                    donnes.put(d.entry, d);
                    if(d.phone != null){
                        addThePhons(d);
                        addCode(d);
                        addClasses(d);
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(in != null){
                try{
                    in.close();
                }catch(Exception ex){}
            }
        }
    }
    
    private void addThePhons(Donne d){
        StringTokenizer st = new StringTokenizer(d.phone, "|");
        while(st.hasMoreElements()){
            String p = st.nextToken();
            addPhon(p, d);
        }
    }
    
    private static String nextToken(StringTokenizer st){
        if(!st.hasMoreTokens())
            return null;
        return st.nextToken();
    }
    
	public URL getBase() {
		return base;
	}
}
