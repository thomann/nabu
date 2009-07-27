package ch.unizh.ori.nabu.ui.http.taglib;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.unizh.ori.common.text.*;
import ch.unizh.ori.nabu.core.Utilities;

import java.io.IOException;

/**
 *  Generated tag class.
 */
public class TextTag extends BodyTagSupport {

    /** property declaration for tag attribute: value.
     */    
    private OldText value;    

    /** Holds value of property mode. */
    private String mode;
    
    public TextTag() {
        super();
    }
    
    
    ////////////////////////////////////////////////////////////////
    ///                                                          ///
    ///   User methods.                                          ///
    ///                                                          ///
    ///   Modify these methods to customize your tag handler.    ///
    ///                                                          ///
    ////////////////////////////////////////////////////////////////
    
    
    //
    // methods called from doStartTag()
    //
    public void otherDoStartTagOperations()  {
    
        try{
            JspWriter out = pageContext.getOut();
            String renderMode = OldScript.AS_IS;
            if(getMode() != null){
                renderMode = getMode();
            }else{
                OldScript script = getValue().getScript();
                if(script == null){
                    out.print(getValue().getUnicodeString());
                    return;
                }
                renderMode = script.getRenderMode(pageContext);
            }
            if(OldScript.AS_XML_ENTITIES.equals(renderMode)){
                out.print(Utilities.htmlEntities(getValue().getUnicodeString()));
            }else if(OldScript.AS_IMAGE.equals(renderMode)){
                String path = getValue().getImageURL();
                path = ((HttpServletRequest)pageContext.getRequest()).getContextPath() + path;
                path = ((HttpServletResponse)pageContext.getResponse()).encodeURL(path);
                out.print( "<img src=\""+path+"\">");
            }else if(OldScript.AS_IS.equals(renderMode)){
                out.print(getValue().getUnicodeString());
            }else{
                pageContext.getServletContext().log("Unknown Render Mode");
            }
        }catch(IOException ex){
            pageContext.getServletContext().log("TextTag", ex);
        }

    }
    
    public boolean theBodyShouldBeEvaluated()  {
        return false;
    }
    
    
    //
    // methods called from doEndTag()
    //
    public void otherDoEndTagOperations()  {
    }
    
    public boolean shouldEvaluateRestOfPageAfterEndTag()  {
        return true;
    }
    
    
    ////////////////////////////////////////////////////////////////
    ///                                                          ///
    ///   Tag Handler interface methods.                         ///
    ///                                                          ///
    ///   Do not modify these methods; instead, modify the       ///
    ///   methods that they call.                                ///
    ///                                                          ///
    ////////////////////////////////////////////////////////////////
    
    
    /** .//GEN-BEGIN:doStartTag
     *
     * This method is called when the JSP engine encounters the start tag,
     * after the attributes are processed.
     * Scripting variables (if any) have their values set here.
     * @return EVAL_BODY_INCLUDE if the JSP engine should evaluate the tag body, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     */
    public int doStartTag() throws JspException, JspException {
        otherDoStartTagOperations();
        
        if (theBodyShouldBeEvaluated()) {
            return EVAL_BODY_BUFFERED;
        } else {
            return SKIP_BODY;
        }
    }//GEN-END:doStartTag
    
    /** .//GEN-BEGIN:doEndTag
     *
     *
     * This method is called after the JSP engine finished processing the tag.
     * @return EVAL_PAGE if the JSP engine should continue evaluating the JSP page, otherwise return SKIP_PAGE.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     */
    public int doEndTag() throws JspException, JspException {
        otherDoEndTagOperations();
        
        if (shouldEvaluateRestOfPageAfterEndTag()) {
            return EVAL_PAGE;
        } else {
            return SKIP_PAGE;
        }
    }//GEN-END:doEndTag
    
    public OldText getValue() {
        return value;
    }
    
    public void setValue(OldText value) {
        this.value = value;
    }
    
    public void writeTagBodyContent(JspWriter out, BodyContent bodyContent) throws IOException {
        bodyContent.writeOut(out);
        bodyContent.clearBody();
    }
    
    /** .
     *
     * Handles exception from processing the body content.
     *
     */    
    public void handleBodyContentException(Exception ex) throws JspException {
        // Since the doAfterBody method is guarded, place exception handing code here.
        throw new JspException("error in TextTag: " + ex);
    }
    //GEN-BEGIN:doAfterbody
    /** .
     *
     *
     * This method is called after the JSP engine processes the body content of the tag.
     * @return EVAL_BODY_AGAIN if the JSP engine should evaluate the tag body again, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     */    
    public int doAfterBody() throws JspException {
        try {
            //
            // This code is generated for tags whose bodyContent is "tagdependent"
            //
            
            JspWriter out = getPreviousOut();
            BodyContent bodyContent = getBodyContent();
            
            writeTagBodyContent(out, bodyContent);
        } catch (Exception ex) {
            handleBodyContentException(ex);
        }
        
        if (theBodyShouldBeEvaluatedAgain()) {
            return EVAL_BODY_AGAIN;
        } else {
            return SKIP_BODY;
        }
    }//GEN-END:doAfterbody
    
    /**
     * Fill in this method to determine if the tag body should be evaluated
     * again after evaluating the body.
     * Use this method to create an iterating tag.
     * Called from doAfterBody().
     *
     */    
    public boolean theBodyShouldBeEvaluatedAgain() {
        return false;
    }
    
    /** Getter for property mode.
     * @return Value of property mode.
     */
    public String getMode() {
        return this.mode;
    }
    
    /** Setter for property mode.
     * @param mode New value of property mode.
     */
    public void setMode(String mode) {
        this.mode = mode;
    }
    
}
