/*
 * Created on 01.04.2004
 */
package ch.unizh.ori.nabu.ui.http.taglib;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import ch.unizh.ori.common.text.PlotterPresentation;
import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.ui.http.HttpCentral;
import ch.unizh.ori.nabu.voc.ImgColumn;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.StringColumn;

/**
 * @author pht
 */
public class ImageTag extends TagSupport {
	
	private ModeField mf;
	private String var;
	
	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) pageContext.getSession().getAttribute("iter");
//		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		JspWriter out = pageContext.getOut();
		
		Map q = (Map)iter.getQuestion();
		String url = null;
		if(mf.getColumn() instanceof ImgColumn){
			ImgColumn ic = (ImgColumn)mf.getColumn();
			if(ic != null){
				String name = (String)q.get(ic.getId());
				String prefix = ic.getPrefix();
				
				if(name != null){
					url = prefix + name;
				}
			}
		}else if (mf.getColumn() instanceof StringColumn) {
			StringColumn sc = (StringColumn) mf.getColumn();
			PlotterPresentation p = (PlotterPresentation) mf.getPresentation();
			HttpCentral httpCentral = ((HttpCentral)sc.getVoc().getCentral());
			url = httpCentral.getPlotterUrl(p, (String)sc.getScript(), q.get(mf.getKey()), sc.getTransliteration());
		}
		
		if(url != null){
			pageContext.setAttribute(var, url);
			return EVAL_BODY_INCLUDE;
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
