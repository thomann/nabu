/*
 * DBServlet.java
 *
 * Created on 19. Februar 2003, 10:10
 */

package ch.unizh.ori.nabu.input.db.http;

import javax.servlet.*;
import javax.servlet.http.*;

import ch.unizh.ori.common.text.*;

import java.util.*;

/**
 *
 * @author  pht
 * @version
 */
public class DBServlet extends HttpServlet {
    
    private static Map map;
    public static Map getMap(){ return map; }
    
    static{
        initMap();
    }
    
    private static void initMap(){
        map = new HashMap();
        map.put("accadian", new TwoColumnVocInfo("Accadian", "lexem", OldUnicodeScript.ACCADIAN, "english", null));
        map.put("arabic", new TwoColumnVocInfo("Arabic", "lexem", OldUnicodeScript.ARABIC, "german", new OldUnicodeScript("MacRoman","default")));
        map.put("ostia", new TwoColumnVocInfo("Ostia", "lexem", null, "german", null));
        map.put("frgrund", new TwoColumnVocInfo("FrGrund", "lexem", null, "german", null));
        map.put("englisch", new TwoColumnVocInfo("Englisch", "lexem", null, "german", null));
        map.put("jenni", new TwoColumnVocInfo("Jenni", "lexem", OldUnicodeScript.HEBREW,
                                                       "german", new OldUnicodeScript("UTF-8","default")));
        map.put("sanskrit", new VocInfo("Sanskrit",new ColumnInfo[]{
            new ColumnInfo("lexem",OldUnicodeScript.SANSKRIT,"Lexem"),
            new ColumnInfo("lexem_add",null,"Lexem_add"),
            new ColumnInfo("german",null,"Deutsch"),
            new ColumnInfo("lection",null, "Lektion")
        }));
    }
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        response.setContentType("text/html");
        
        java.io.PrintStream out = System.out;
        
        out.println("contextPath: " + request.getContextPath() );
        out.println("pathInfo: "+request.getPathInfo() );
        out.println("pathTranslated: "+ request.getPathTranslated() );
        out.println("QueryString: "+request.getQueryString() );
        out.println("getRequestURI: " + request.getRequestURI() );
        out.println("getServletPath: "+request.getServletPath() );

        String path = request.getPathInfo().substring("/".length());
        System.out.println("path: "+path);
        int i = path.indexOf('/');
        if(i<0){
            throw new IllegalArgumentException("Url to short");
        }
        String key = path.substring(0,i);
        System.out.println("key: "+key);
        VocInfo vi = (VocInfo)map.get(key);
        if(vi == null){
            throw new IllegalArgumentException("Url no good");
        }
        request.setAttribute("vocInfo", vi);
        String s = "/WEB-INF" + vi.getDispatchPath(path.substring(i), request, response);
        System.out.println("s: "+s);
        getServletContext().getRequestDispatcher(s).forward(request, response);
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
    public static class VocInfo {
        private static final ColumnInfo vidInfo = new ColumnInfo("vid", null, "vid");
        
        private String name, description;
        private String table;
        
        protected ColumnInfo[] columnInfo;
        
        private String colNames = null;
        private String colUpdateNames = null;
        
        public VocInfo(){
        }
        
        public VocInfo(String name){
            this.name = this.description = this.table = name;
        }
        
        public VocInfo(String name, ColumnInfo[] columnInfo){
            this(name);
            this.columnInfo = columnInfo;
        }
        
        public String getDispatchPath(String path, HttpServletRequest request, HttpServletResponse response){
            return  "/db" + path + "?" + request.getQueryString();
        }
        
        public String getColNames(){
            if(colNames == null){
                StringBuffer s = new StringBuffer();
                for(int i=0; i<getColumnInfos().length; i++){
                    if(i!=0){
                        s.append(",");
                    }
                    s.append(getColumnInfos()[i].getName());
                }
                colNames = s.toString();
            }
            return colNames;
        }
        
        public String getColUpdateNames(){
            if(colUpdateNames == null){
                StringBuffer s = new StringBuffer();
                for(int i=0; i<getColumnInfos().length; i++){
                    if(i!=0){
                        s.append(",");
                    }
                    s.append(getColumnInfos()[i].getName()).append("=?");
                }
                colUpdateNames = s.toString();
            }
            return colUpdateNames;
        }
        
        public ColumnInfo getVidInfo(){
            return vidInfo;
        }
        public ColumnInfo[] getColumnInfos() {
            return columnInfo;
        }
        
        public String getDescription() {
            return description;
        }
        
        public String getName() {
            return name;
        }
        
        public String getTableName() {
            return table;
        }
    }
    
    public static class ColumnInfo {
        private String name;
        private OldScript enc;
        private String label;
        
        public ColumnInfo(String name, OldScript enc, String label){
            this.name = name;
            if(enc != null){
                this.enc = enc;
            }else{
                this.enc = new OldUnicodeScript(null, null);
            }
            this.label = label;
        }
        
        public String getName(){
            return name;
        }
        public OldScript getEnc(){
            return enc;
        }
        public String getLabel(){
            return label;
        }
    }
    
    public static class TwoColumnVocInfo extends VocInfo{
        public TwoColumnVocInfo(String table, String colAName, OldScript colAEnc, String colBName, OldScript colBEnc){
            super(table);
            columnInfo = new ColumnInfo[]{
                new ColumnInfo(colAName, colAEnc, colAName),
                new ColumnInfo(colBName, colBEnc, colBName),
                new ColumnInfo("lection", null, "Lektion")
            };
        }
    }
    
}
