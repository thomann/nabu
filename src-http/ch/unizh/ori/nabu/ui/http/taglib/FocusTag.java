/*
 * Created on 01.04.2004
 */
package ch.unizh.ori.nabu.ui.http.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.ui.http.HttpRenderer;

/**
 * @author pht
 */
public class FocusTag extends TagSupport {
	
	private String ques;
	
	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) pageContext.getSession().getAttribute("iter");
		HttpRenderer r = (HttpRenderer) iter.getRenderer();
		JspWriter out = pageContext.getOut();
		
		if(r == null){
			return SKIP_BODY;
		}
		
		String focus;
		if(r.isShowSolution()){
			focus = "ok"; 
		}else{
			focus = "q_"+r.getFocusKey();
		}
		try {
			out.write(focusString(ques, focus));
		} catch (IOException e) {
			throw new JspException(e);
		}
		
		return SKIP_BODY;
	}

	public static String focusString(String ques, String focus){
		String str = "<script language=\"JavaScript\">\n" +			"<!--\n" +			"if (document."+ques+" && document."+ques+"."+focus+")\n" +			"  document."+ques+"."+focus+".focus();\n" +
			((focus=="ok")?"":"  document."+ques+"."+focus+".select();\n") +
			"//-->\n" +			"</script>";
		return str;
	}

	/**
	 * @return
	 */
	public String getQues() {
		return ques;
	}

	/**
	 * @param string
	 */
	public void setQues(String string) {
		ques = string;
	}

}
