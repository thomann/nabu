/*
 * Created on 02.04.2004
 */
package ch.unizh.ori.nabu.ui.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import ch.unizh.ori.nabu.core.MappingRenderer;
import ch.unizh.ori.nabu.voc.Mode;

/**
 * @author pht
 */
public class HttpMappingRenderer extends MappingRenderer implements HttpRenderer {
	
	private String jspPath = "/WEB-INF/renderers/MappingRenderer.jsp";
	private Map lastAnswer;
	
	public HttpMappingRenderer(Mode mode){
		super(mode);
	}

	public String getJspPath() {
		return jspPath;
	}

	public void setJspPath(String string) {
		jspPath = string;
	}
	
	public void setQuestion(Object q) {
		super.setQuestion(q);
		if(lastAnswer == null){
			lastAnswer = new HashMap();
		}else{
			lastAnswer.clear();
		}
	}

	/**
	 * @return
	 */
	public Map getLastAnswer() {
		return lastAnswer;
	}

	/**
	 * @param map
	 */
	public void setLastAnswer(Map map) {
		lastAnswer.clear();
		lastAnswer.putAll(map);
	}

	public void processRequest(HttpServletRequest request, boolean showSolution) {
		for (Iterator iter = getAnswerKeys().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			String answer = request.getParameter("q_"+key);
			setUserAnswerValue(key, answer);
		}
		setDirty(!getUserAnswer().equals(getLastAnswer()));
//		System.err.println("dirty: "+isDirty());
//		System.err.println("getLastAnswer(): "+getLastAnswer());
		setLastAnswer(getUserAnswer());
		process(showSolution);
	}

}
