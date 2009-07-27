/*
 * Created on 01.04.2004
 */
package ch.unizh.ori.nabu.ui.http.taglib;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.Sotm;
import ch.unizh.ori.nabu.voc.StringColumn;
import ch.unizh.ori.nabu.voc.Voice;

/**
 * @author pht
 */
public class SoundTag extends TagSupport {
	
	private ModeField mf;
	private String var;
	
	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) pageContext.getSession().getAttribute("iter");

		Sotm sotm = mf.getColumn().getVoc().getSound(mf.getKey());
		boolean hasSotm = sotm != null && sotm.getVoices().size()>0;
		if(hasSotm)
			pageContext.getRequest().setAttribute("hasSotm", Boolean.TRUE);
		
		if(pageContext.getRequest().getParameter("play") == null)
			return SKIP_BODY;		
		
		if(hasSotm){
			int i = (int)(Math.random()*sotm.getVoices().size());
			Voice v = (Voice) sotm.getVoices().get(i);
			Map q = (Map)iter.getQuestion();
			String toSay = null;
			if (mf.getColumn() instanceof StringColumn) {
				StringColumn sc = (StringColumn) mf.getColumn();
				toSay = (String) q.get(mf.getColumn().getId());
			}
			String name = sotm.getUtterance(toSay, q);
			String prefix = (String)pageContext.getSession().getAttribute("prefix."+mf.getColumn().getId());
			if(prefix == null){
				prefix = v.getPrefix();
			}
			
			if(name != null){
				String s = prefix + name;
				pageContext.setAttribute(var, s);
				return EVAL_BODY_INCLUDE;
//				try {
//					out.write( "<a href=\""+response.encodeURL(s)+"\">"+name+"</a>");
//					out.write(s);
//				} catch (IOException e) {
//					throw new JspException(e);
//				}
			}
		}
		
		return SKIP_BODY;
	}

//	public static String soundString(String prefix, String name){
//		String str = "<a href=\"\">"+name+"</a>";
//		return str;
//	}

	public void setModeField(ModeField mf) {
		this.mf = mf;
	}

	/**
	 * @param string
	 */
	public void setVar(String string) {
		var = string;
	}

}
