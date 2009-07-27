/*
 * StringPlotServlet.java
 *
 * Created on 21. September 2002, 11:26
 */

package ch.unizh.ori.nabu.ui.http.tuppu;

import javax.servlet.*;
import javax.servlet.http.*;

import ch.unizh.ori.tuppu.Box;
import ch.unizh.ori.tuppu.Plotter;
import ch.unizh.ori.tuppu.hieroglyph.HieroPlotter;

import java.awt.*;
import java.awt.geom.*;

/**
 *
 * @author  pht
 * @version
 */
public class HieroPlotServlet extends ImageServlet {
    
    public static final int SIZE = 65;
    
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
        String text = request.getParameter("text");
        HieroPlotter hp = new HieroPlotter(getDefaultGraphics(), SIZE);
        Box r = hp.constructBox(getDefaultGraphics(),
            text, new Font("Dialog", Font.PLAIN, SIZE), null);
        request.setAttribute("hp", hp);
        request.setAttribute("r", r);
        super.processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Plots a hieroglyphic String";
    }
    
    public Rectangle2D getBounds(HttpServletRequest request, HttpServletResponse response) {
        Graphics2D g = getDefaultGraphics();
        Box r = (Box)request.getAttribute("r");
        Dimension d = r.getSize();
        return new Rectangle2D.Double(0,0, d.width, d.height);
    }
    
    public void plot(Graphics2D g, HttpServletRequest request, HttpServletResponse response) {
        g.setPaint(Color.black);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        HieroPlotter hp = (HieroPlotter)request.getAttribute("hp");
        Box r = (Box)request.getAttribute("r");
        Plotter.paint(g, r);
    }
    
    /*protected HieroPlotter.Box getRenderer(HttpServletRequest request){
        HieroPlotter.Box ret = (HieroPlotter.Box) request.getAttribute("string");
        if(ret == null){
            String code = request.getParameter("string");
            HieroPlotter hp = new HieroPlotter();
            ret = HieroPlotter.createRenderer(getDefaultGraphics(), 
        }
        return ret;
    }*/
    
}
