/*
 * StringPlotServlet.java
 *
 * Created on 21. September 2002, 11:26
 */

package ch.unizh.ori.nabu.ui.http.tuppu;

import javax.servlet.*;
import javax.servlet.http.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;

/**
 *
 * @author  pht
 * @version
 */
public class StringPlotServlet extends ImageServlet {
    
    //private Font font;
    
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
        request.setCharacterEncoding("UTF-8");
        super.processRequest(request, response);
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
        return "Plots a String";
    }
    
    /*public int getHeight(HttpServletRequest request, HttpServletResponse response) {
        return getDefaultGraphics().getFontMetrics(getFont()).getHeight();
    }
    
    public int getWidth(HttpServletRequest request, HttpServletResponse response) {
        return getDefaultGraphics().getFontMetrics(getFont()).stringWidth(getString(request));
    }*/
    
    public Rectangle2D getBounds(HttpServletRequest request, HttpServletResponse response) {
        Graphics2D g = getDefaultGraphics();
        Font f = getFont(request);
        //GlyphVector v = f.createGlyphVector(g.getFontRenderContext(), getString(request));
        TextLayout tl = new TextLayout(getString(request), f, g.getFontRenderContext());
        //double width = v.getLogicalBounds().getWidth();
        double width = g.getFontMetrics(f).getStringBounds(getString(request), g).getWidth();
        double height = tl.getBounds().getHeight();
        //double height = v.getVisualBounds().getHeight();
        return new Rectangle2D.Double(0,0,width, height);
        //return v.getLogicalBounds().createUnion(v.getVisualBounds());
        //return new Rectangle2D.Double(0,0,getWidth(request, response), getHeight(request,response));
    }
    
    public Font getFont(HttpServletRequest request){
        Font font = (Font)request.getAttribute("font");
        if(font == null){
            int size = 18;
            try{
                size = Integer.parseInt(request.getParameter("size"));
            }catch(NumberFormatException ex){}
            font = new Font("Lucida Sans Regular", Font.PLAIN, size);
            request.setAttribute("font", font);
        }
        return font;
    }
    
    public void plot(Graphics2D g, HttpServletRequest request, HttpServletResponse response) {
        String str = getString(request);
        Font font = getFont(request);
        g.setColor(Color.blue);
        g.setFont(font);
        //GlyphVector v = font.createGlyphVector(g.getFontRenderContext(), str);
        //FontMetrics fm = g.getFontMetrics(getFont(request));
        //int y = fm.getAscent();
        TextLayout tl = new TextLayout(getString(request), font, g.getFontRenderContext());
        Rectangle2D bounds = tl.getBounds();
        //System.out.println(bounds);
        int y = (int)(-bounds.getY());
        g.drawString(str, 0, y);
    }
    
    protected String getString(HttpServletRequest request){
        String ret = request.getParameter("string");
        //ret = "\u0905\u0930\u094D\u092A";
        if(ret == null) return "";
        return ret;
    }
    
}
