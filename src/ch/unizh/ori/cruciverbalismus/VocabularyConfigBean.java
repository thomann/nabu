package ch.unizh.ori.cruciverbalismus;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import ch.unizh.ori.nabu.core.QuestionProducer;
import ch.unizh.ori.nabu.voc.*;


public class VocabularyConfigBean extends ConfigBean {
	
	private Vocabulary voc;
	private Mode mode;
	private List lessons = new ArrayList();
	
	public VocabularyConfigBean() {
	}
	
	public String getLexicon() {
		if(super.getLexicon() == null || super.getLexicon().length() == 0){
			try {
				StringBuffer lexicon = new StringBuffer();
				StringBuffer answer = new StringBuffer();
				StringBuffer given = new StringBuffer();
				Mode mode = getMode();
				for (Iterator iterator = lessons.iterator(); iterator.hasNext();) {
					FieldStream lesson = (FieldStream) iterator.next();
					String[] q;
					Object i = lesson.start();
					while((q=lesson.next(i)) != null){
						answer.setLength(0);
						given.setLength(0);
						for (Iterator iterator2 = mode.createModeFields().iterator(); iterator2
								.hasNext();) {
							ModeField mf = (ModeField) iterator2.next();
							StringBuffer sb = (mf.isAsking())?given:answer;
							if(sb.length()!=0){
								sb.append(", ");
							}
							int column = mf.getColumn().getColumn();
							if(q.length>column)
								sb.append(q[column]);
						}
						lexicon.append(answer).append("\t").append(given).append("\n");
					}
				}
				super.setLexicon(lexicon.toString());
			} catch (Exception e) {
				e.printStackTrace();
				super.setLexicon("");
			}
		}
		return super.getLexicon();
	}
	public Vocabulary getVoc() {
		return voc;
	}
	public void setVoc(Vocabulary voc) {
		this.voc = voc;
	}
	public Mode getMode() {
		return mode;
	}
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	public List getLessons() {
		return lessons;
	}
	public void setLessons(List lessons) {
		this.lessons = lessons;
	}
}

