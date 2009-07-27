/*
 * CartesianMorphQuestionProducer.java
 *
 * Created on 12. Oktober 2002, 06:40
 */

package ch.unizh.ori.nabu.morph;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import ch.unizh.ori.nabu.core.QuestionProducer;

/**
 *
 * @author  pht
 */
public class CartesianMorphQuestionProducer extends QuestionProducer {
    
    public List roots = new ArrayList();
    public String[][] space = new String[0][];
    public String[] coordinateNames = new String[0];
    
    /** Creates a new instance of CartesianMorphQuestionProducer */
    public CartesianMorphQuestionProducer() {
    }
    
    public Object produceNext() {
        int r = (int) Math.floor(Math.random() * roots.size());
        String root = (String)roots.get(r);
        
        String[] coords = new String[space.length];
        for(int i=0; i<coords.length; i++){
            int s = (int) Math.floor(Math.random() * space[i].length);
            coords[i] = space[i][s];
        }
        
        String form = form(root, coords);
        
        if(Math.random() >= 0.5 && true){
            return new CartesianMorphQuestion.Analyse(root, coords, form);
        }else{
            return new CartesianMorphQuestion.Construct(root, coords, form);
        }
    }
    
    public boolean isList() {
        return false;
    }
    
    public int countQuestions() {
        throw new IllegalArgumentException("Should not be called");
    }
    
    public void initSession() {
    }
    
    public String[] getCoordinateNames(){
        return coordinateNames;
    }
    
    public String[][] getSpace(){
        return space;
    }
    
    public String getCoordinateLabel(int i){
        return (String)labels.get(String.valueOf(i));
    }
    
    public String getValueLabel(int i, int j){
        return (String)labels.get(i+","+j);
    }
    
// **************************************************************************
// *****************************  Form Generation ***************************
// **************************************************************************
    
    protected Map rootClasses = new HashMap();
    protected Map stemsMap = new HashMap();
    protected Map formatters;
    protected Map labels = new HashMap();
    
    protected String form(String root, String[] coords){
        String rootClass = rootClass(root);
        MessageFormat formatter = formatter(rootClass, coords);
        if(formatter == null)
            return null;
        
        return formatter.format(stems(root));
    }
    
    protected String rootClass(String root){
        return (String)rootClasses.get(root);
    }
    
    protected String[] stems(String root){
        return (String[])stemsMap.get(root);
    }
    
    protected MessageFormat formatter(String rootClass, String[] coords){
        StringBuffer key = new StringBuffer(rootClass);
        for(int i=0; i<coords.length; i++)
            key.append( (i==0)?"_":"." ).append(coords[i]);
        return (MessageFormat)formatters.get(key.toString());
    }
    
// **************************************************************************
// *****************************  Data Loading ***************************
// **************************************************************************
    
    public void loadForms(Properties forms){
        coordinateNames = forms.getProperty("product").split(",");
        forms.remove("product");
        space = new String[coordinateNames.length][];
        for(int i=0; i<space.length; i++){
            String key = "set."+coordinateNames[i];
            space[i] = forms.getProperty(key).split(",");
            forms.remove(key);
            String label = "label."+coordinateNames[i];
            labels.put(String.valueOf(i), forms.getProperty(label));
            for(int j=0; j<space[i].length; j++){
                String s = label+"."+space[i][j];
                labels.put(i+","+j, forms.getProperty(s));
            }
        }
        // Rest of entries are MessageFormats ...
        for(Enumeration enumeration=forms.keys(); enumeration.hasMoreElements(); ){
            String key = (String)enumeration.nextElement();
            String mp = forms.getProperty(key);
            forms.put(key, new MessageFormat(mp));
        }
        formatters = forms;
    }
    
    public void loadRoots(BufferedReader in) throws IOException {
        String line;
        while( (line=in.readLine()) != null ){
            if( line.equals("") || line.startsWith("#") )
                continue;
            //System.out.println(line);
            String[] fields = line.split("\t");
            //System.out.println(Arrays.asList(fields).toString());
            if(fields.length < 2){
                System.out.println("Problem with voc");
                continue;
            }
            String root = fields[0];
            roots.add(root);
            rootClasses.put(root, fields[1]);
            String[] stems = new String[fields.length - 2];
            System.arraycopy(fields, 2, stems, 0, stems.length);
            stemsMap.put(root, stems);
        }
    }
    
    public static CartesianMorphQuestionProducer load(Properties forms, BufferedReader in)
                                                        throws IOException {
        CartesianMorphQuestionProducer ret = new CartesianMorphQuestionProducer();
        ret.loadForms(forms);
        ret.loadRoots(in);
        return ret;
    }
    
    public static CartesianMorphQuestionProducer load(String formsUrl, String rootsUrl,
                            String encoding)
                            throws IOException {
        CartesianMorphQuestionProducer ret = null;
        
        Properties forms = new Properties();
        InputStream formsIn = null;
        try{
            formsIn = new FileInputStream(formsUrl);
            forms.load(formsIn);
            formsIn.close();
        }catch(IOException ex){
            if(formsIn != null)
                try{
                    formsIn.close();
                }catch(Throwable t){}
            throw ex;
        }
        
        BufferedReader rootsIn = null;
        try{
            rootsIn = new BufferedReader(new InputStreamReader(new FileInputStream(rootsUrl), encoding));
            ret = load(forms, rootsIn);
            rootsIn.close();
        }catch(IOException ex){
            if(rootsIn != null)
                try{
                    rootsIn.close();
                }catch(Throwable t){}
            throw ex;
        }
        
        return ret;
    }
    
    public static void main(String[] args){
        try{
        String formsUrl = "/home/pht/projekte/nabu/web/vocs/sanskritforms.properties";
        String rootsUrl = "/home/pht/projekte/nabu/web/vocs/sanskritroots.txt";
        CartesianMorphQuestionProducer p = load(formsUrl, rootsUrl, "UTF-8");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while( !in.readLine().startsWith("q") ){
            Object q = p.produceNext();
            System.out.println("q: "+q.toString());
            if(in.readLine().startsWith("q"))
                return;
            System.out.println("a: "+q.toString());
        }
        }catch(Throwable t){
            t.printStackTrace();
        }
    }

	public void finishSession() {
	}
}
