/*
 * Created on 01.04.2004
 */
package ch.unizh.ori.nabu.ui.http.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author pht
 */
public class CheckboxTag extends TagSupport {
	
	private String name;
	private Boolean value;
	
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try{
			out.print("<input type=CHECKBOX name=\"");
			out.print(name);
			out.print("\" id=\"");
			out.print(name);
			out.print("\"");
			
			if(value != null && value.booleanValue()){
				out.print(" checked");
			}
			out.print(">");
		}catch(IOException ex){
			throw new JspException(ex);
		}
		return SKIP_BODY;
	}

	public void setName(String string) {
		name = string;
	}

	public void setValue(Boolean s) {
		value = s;
	}

}
