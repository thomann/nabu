package ch.unizh.ori.nabu.ui.http.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 *  Generated tag class.
 */
public class HeaderTag extends BodyTagSupport {


    public HeaderTag() {
        super();
    }
    
    
//    private void printA(String url, String text)throws IOException{
//        JspWriter out = pageContext.getOut();
//        out.print("<a href=\"");
//        url = ((HttpServletRequest)pageContext.getRequest()).getContextPath() + url;
//        out.write(((HttpServletResponse)pageContext.getResponse()).encodeURL(url));
//        out.print("\">");
//        out.write(text);
//        out.print("</a>");
//    }
    
    public int doStartTag() throws JspException, JspException {
//		ServletContext application = pageContext.getServletContext();
//		if(application.getAttribute("central")==null){
//			ch.unizh.ori.nabu.ui.http.HttpCentral c = new ch.unizh.ori.nabu.ui.http.HttpCentral(application);
//			c.readFiles();
//			application.setAttribute("central", c);
//		}
    
//		  try{
//			  JspWriter out = pageContext.getOut();
//			  out.print("<table><tr><td style='background-color: #FFF0E0;'>");
//			  printA("/startSession.jsp", "New Session");
//			  out.print(" -- ");
//			  if(pageContext.getSession().getAttribute("iter") != null){
//				  printA("/showQuestion.jsp", "Question");
//			  }else{
//				  out.print("Question");
//			  }
//			  out.print(" -- ");
//			  printA("/prefs.jsp", "Preferences");
//			  out.println("</td></tr>");
//			  out.print("<tr><td style='font-size: small; background-color: #FFF0E0;'>DB-Edit: ");
//			  for(Iterator iter=DBServlet.getMap().keySet().iterator(); iter.hasNext(); ){
//				  String s = (String)iter.next();
//				  printA("/edit-db/"+s+"/show.jsp", s);
//				  if(iter.hasNext()){
//					  out.print(" -- ");
//				  }
//			  }
//			  out.println("</td></tr>");
//			  out.println("</table>");
//		  }catch(IOException ex){
//			  pageContext.getServletContext().log("HeaderTag", ex);
//		  }
		return SKIP_BODY;
    }
    
}
