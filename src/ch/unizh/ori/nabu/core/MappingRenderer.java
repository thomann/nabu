/*
 * Created on 02.04.2004
 */
package ch.unizh.ori.nabu.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import ch.unizh.ori.common.text.StringPresentation;
import ch.unizh.ori.common.text.StringText;
import ch.unizh.ori.nabu.ui.Renderer;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.StringColumn;

/**
 * @author pht
 */
public abstract class MappingRenderer implements Renderer{
	
	private boolean showSolution = false;
	private boolean onlySubset = false;
	
	private Mode mode;
	private List modeFields = new ArrayList();
	
	private List answerModeFields = new ArrayList();
	private List answerKeys = new ArrayList();
	private String focusKey;
	private Map soundCols; 
	
	private Map question;
	
	private Map presentedQuestion = new HashMap();
	
	private Map userAnswer = new HashMap();
	private Map correctAnswer = new HashMap();
	
	private boolean dirty = false;
	
	public MappingRenderer(Mode mode){
		this.mode = mode;
		this.modeFields = mode.createModeFields();
		for (Iterator iter = modeFields.iterator(); iter.hasNext();) {
			ModeField mf = (ModeField) iter.next();
			if(mf.isAsking()){
				answerModeFields.add(mf);
				answerKeys.add(mf.getKey());
				if(focusKey == null){
					focusKey = mf.getKey();
				}
			}
		}
		setQuestion(null);
	}

	public void setQuestion(Object q) {
		this.question = (Map) q;
		
		// clear old things
		userAnswer.clear();
//		lastAnswer.clear();
		correctAnswer.clear();
		presentedQuestion.clear();
		setShowSolution(false);
		setDirty(false);
		
		if(q != null){
			for (Iterator iter = modeFields.iterator(); iter.hasNext();) {
				ModeField mf = (ModeField) iter.next();
				String key = mf.getKey();
				Object value = this.question.get(key);
				if (mf.getPresentation() instanceof StringPresentation
						&& mf.getColumn() instanceof StringColumn) {
					StringPresentation sp = (StringPresentation) mf.getPresentation();
					StringColumn sc = (StringColumn)mf.getColumn();
					String outText = sp.getOutText(value, sc.getTransliteration());
					presentedQuestion.put(key, outText);
				}else if(mf.getPresentation() == null){
					presentedQuestion.put(key, value);
				}
			}
			for (Iterator iter = getAnswerKeys().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				Object value = ((Map)q).get(key);
				if(presentedQuestion.containsKey(key)){
					value = presentedQuestion.get(key);
				}
				if(value != null && value instanceof String && ((String)value).length() > 0){
					correctAnswer.put(key, value);
				}else{
					correctAnswer.remove(key);
				}
			}
		}
//		for (Iterator iter = getModeFields().iterator(); iter.hasNext();) {
//			ModeField mf = (ModeField) iter.next();
//			mf.setQuestion(question);
//		}
	}
	
	public Map getQuestion(){
		return question;
	}
	
	public boolean isShowSolution() {
		return showSolution;
	}

	public void clear() {
		setQuestion(null);
	}
	
	

	/**
	 * @return
	 */
	public Map getCorrectAnswer() {
		return correctAnswer;
	}

	/**
	 * @return
	 */
	public String getFocusKey() {
		return focusKey;
	}

	/**
	 * @return
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * @return
	 */
	public boolean isOnlySubset() {
		return onlySubset;
	}

	/**
	 * @return
	 */
	public Map getUserAnswer() {
		return userAnswer;
	}

	/**
	 * @param map
	 */
	public void setCorrectAnswer(Map map) {
		correctAnswer = map;
	}

	/**
	 * @param string
	 */
	public void setFocusKey(String string) {
		focusKey = string;
	}

	/**
	 * @param b
	 */
	public void setOnlySubset(boolean b) {
		onlySubset = b;
	}

	/**
	 * @param b
	 */
	public void setShowSolution(boolean b) {
		showSolution = b;
	}

	/**
	 * @param map
	 */
	public void setUserAnswer(Map map) {
		userAnswer = map;
	}

	/**
	 * @return
	 */
	public List getModeFields() {
		return modeFields;
	}

	/**
	 * @return
	 */
//	public List getAnswerModeFields() {
//		return answerModeFields;
//	}

	public String toString() {
		return getUserAnswer() + " vs. " + getCorrectAnswer();
	}

	
	// ****************************************************
	// ****************************************************
	//             things to test if correct
	// ****************************************************
	// ****************************************************

	public boolean isCorrect() {
		boolean ret = true;
		for (Iterator iter = answerModeFields.iterator(); iter.hasNext();) {
			ModeField m = (ModeField) iter.next();
			if(!correct(m)){
				ret = false;
				break;
			}
		}
		if(!ret && !isDirty()){
			setShowSolution(true);
		}
		setDirty(false);
		return ret;
	}
	
	private boolean correct(ModeField m){
		Set user = toSet(m, userAnswer.get(m.getKey()));
		Set corr = toSet(m, presentedQuestion.get(m.getKey()));
		if(onlySubset){
			return user.size()>=1 && corr.containsAll(user);
		}else{
			return user.equals(corr);
		}
	}
	
	private Set toSet(ModeField m, Object o){
		String str = null;
		if(o instanceof String){
			str = (String)o;
		}else if(o instanceof StringText){
			str = o.toString();
		}
		String del = ((StringColumn)m.getColumn()).getDel();
		if(del == null){
			del = ",;/";
		}
		List l = Utilities.split(str, del);
		if(l == null){
			return Collections.EMPTY_SET;
		}
		Set ret = new HashSet(l.size());
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			String s = (String) iter.next();
			if(s!=null && s.trim().length() >= 1){
				ret.add(s.trim());
			}
		}
		return ret;
	}

	/**
	 * @return
	 */
	public List getAnswerKeys() {
		return answerKeys;
	}
	
	private static Logger userInputLogger = Logger.getLogger(MappingRenderer.class.getName() + ".[userInput]");

	protected void process(boolean showSolution) {
		setShowSolution(showSolution);
		userInputLogger.info("getCorrectAnswer(): " + getCorrectAnswer());
		userInputLogger.info("getUserAnswer(): " + getUserAnswer());
		userInputLogger.info("isShowSolution(): " + isShowSolution());
		userInputLogger.info("getPresentedQuestion(): " + getPresentedQuestion());
	}
	
	protected void setUserAnswerValue(String key, String answer) {
		if(answer != null && answer.length() > 0){
			getUserAnswer().put(key, answer);
		}else{
			getUserAnswer().remove(key);
		}
	}

	public boolean isDirty() {
		return dirty;
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public Map getPresentedQuestion() {
		return presentedQuestion;
	}

	public void setPresentedQuestion(Map presentedQuestion) {
		this.presentedQuestion = presentedQuestion;
	}
}
