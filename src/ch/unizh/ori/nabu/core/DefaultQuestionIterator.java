/*
 * QuestionIterator.java
 *
 * Created on 23. August 2002, 19:37
 */

package ch.unizh.ori.nabu.core;

import ch.unizh.ori.nabu.stat.Statistics;
import ch.unizh.ori.nabu.ui.Renderer;
import ch.unizh.ori.nabu.ui.RendererChooser;
import java.util.*;

/**
 *
 * @author  pht
 */
public class DefaultQuestionIterator
	implements QuestionIterator, java.io.Serializable {

	public List listProducers = new ArrayList();
	public List infiniteProducers = new ArrayList();

	private Object question;
	private Renderer renderer;

	private Statistics statistics = new Statistics(this);

	/** Holds value of property currentProducer. */
	private QuestionProducer producer;

	/** Holds value of property rendererChooser. */
	private RendererChooser rendererChooser;

	/** Holds value of property user. */
	private User user;

	private ProblemProducer problemProducer = new ProblemProducer();
	
	private List problemsList = new LinkedList();

	/** Holds value of property problemsOnly. */
	private boolean problemsOnly;
	private Renderer lastRenderer;

	/** Creates a new instance of QuestionIterator */
	public DefaultQuestionIterator() {
		addProducer(problemProducer);
	}

	/*    public boolean hasNext(){
	        return false;
	    }*/

	public void next() {
		QuestionProducer p = chooseProducer();
		setProducer(p);
		if (p == null) {
			setQuestion(null);
			// keep the last renderer which was supposedly the only one...
			lastRenderer = getRenderer();
			setRenderer(null);
			return;
		}
		Object q = p.produceNext();
		setQuestion(q);
		if (p.isList() && p != problemProducer && p.countQuestions() <= 0) {
			listProducers.remove(p);
		}

		Renderer r = null;
		if (p == problemProducer && q != null) {
			r = problemProducer.getRendererFor(q);
		} else if (p != null) {
			r = getRendererChooser().chooseRenderer(this);
		}

		setRenderer(r);

		getStatistics().startQuestion();
	}

	public int countQuestions() {
		if (infiniteProducers.size() >= 1)
			return -1;
		int sum = 0;
		for (Iterator i = listProducers.iterator(); i.hasNext();) {
			sum += ((QuestionProducer) i.next()).countQuestions();
		}
		return sum;
	}

	protected QuestionProducer chooseProducer() {
		boolean hasProblems = (problemProducer.countQuestions() > 0);

		if (isProblemsOnly()) {
			return (hasProblems) ? problemProducer : null;
		}
		double r = Math.random();
		int numL = listProducers.size();
		if (!hasProblems)
			numL--;
		int numI = infiniteProducers.size();

		if ((numL == 0) && (numI == 0)) {
			return null;
		}

		double d = numL / (double) (numL + numI);

		if (r < d) {
			// choose from the Lists
			int[] counts = new int[listProducers.size()];
			int sum = 0;
			for (int i = 0; i < counts.length; i++) {
				QuestionProducer qp = (QuestionProducer) listProducers.get(i);
				sum += qp.countQuestions();
				counts[i] = sum;
			}
			int index = (int) Math.floor(Math.random() * sum);
//			System.out.println("index: "+index);
			for (int i = 0; i < counts.length; i++) {
				if (index < counts[i]) {
					return (QuestionProducer) listProducers.get(i);
				}
			}
		} else {
			// choose from the "infinites"
			int i = (int) Math.floor(Math.random() * infiniteProducers.size());
			return (QuestionProducer) infiniteProducers.get(i);
		}
		return null;
	}

	protected void accountProblems(boolean answerWasCorrect) {
		Object question = getQuestion();
		int i = getTimesForProblem(question);
//		System.out.print("Problem-Voc: " + question + ": " + i);
		if (answerWasCorrect) {
			if (i <= 0) {
				// Do nothing
			} else if (i == 1) {
				removeProblem(question);
			} else {
				setTimesForProblem(question, i - 1);
			}
		} else {
			if (i <= 0) {
				addProblem(question, getRenderer(), 3);
			} else {
				setTimesForProblem(question, i + 1);
			}
		}
//		System.out.println(" -> " + getTimesForProblem(question));
		getStatistics().finishQuestion(answerWasCorrect, i);
	}
	protected void addProblem(Object question, Renderer renderer) {
		addProblem(question, renderer, 3);
	}
	protected void addProblem(Object question, Renderer renderer, int times) {
		problemProducer.add(question, renderer, times);
		problemsList.add(question);
	}
	public int getTimesForProblem(Object question) {
		return problemProducer.getTimesFor(question);
	}
	protected void setTimesForProblem(Object question, int times) {
		problemProducer.setTimesFor(question, times);
	}
	protected void removeProblem(Object question) {
		problemProducer.remove(question);
	}

	public void addProducer(QuestionProducer qp) {
		if (qp.isList()) {
			listProducers.add(qp);
		} else {
			infiniteProducers.add(qp);
		}
	}

	public void removeProducer(QuestionProducer qp) {
		if (qp == problemProducer)
			return;
		if (qp.isList()) {
			listProducers.remove(qp);
		} else {
			infiniteProducers.remove(qp);
		}
	}

	/** Getter for property user.
	 * @return Value of property user.
	 */
	public User getUser() {
		return this.user;
	}

	/** Setter for property user.
	 * @param user New value of property user.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/** Getter for property rendererChooser.
	 * @return Value of property rendererChooser.
	 */
	public RendererChooser getRendererChooser() {
		return this.rendererChooser;
	}

	/** Setter for property rendererChooser.
	 * @param rendererChooser New value of property rendererChooser.
	 */
	public void setRendererChooser(RendererChooser rendererChooser) {
		this.rendererChooser = rendererChooser;
	}

	public QuestionProducer getProducer() {
		return producer;
	}

	public Object getQuestion() {
		return question;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	/** Setter for property producer.
	 * @param producer New value of property producer.
	 */
	protected void setProducer(QuestionProducer producer) {
		this.producer = producer;
	}

	/** Setter for property question.
	 * @param question New value of property question.
	 */
	protected void setQuestion(Object question) {
		this.question = question;
	}

	/** Setter for property renderer.
	 * @param renderer New value of property renderer.
	 */
	protected void setRenderer(Renderer renderer) {
		if (this.renderer != null) {
			this.renderer.clear();
		}
		this.renderer = renderer;
		if (renderer != null) {
			renderer.setQuestion(getQuestion());
		}
	}

	/** Getter for property problemsOnly.
	 * @return Value of property problemsOnly.
	 */
	public boolean isProblemsOnly() {
		return this.problemsOnly;
	}

	/** Setter for property problemsOnly.
	 * @param problemsOnly New value of property problemsOnly.
	 */
	public void setProblemsOnly(boolean problemsOnly) {
		this.problemsOnly = problemsOnly;
	}

	public int countProblems() {
		return problemProducer.countQuestions();
	}

	public static class ProblemProducer extends QuestionProducer {

		private Map problemTimes = new HashMap();
		private Map problemRenderers = new HashMap();
		private List problems = new ArrayList();

		public int countQuestions() {
			return problems.size();
		}

		public void initSession() {
		}

		public boolean isList() {
			return true;
		}

		public Object produceNext() {
			if (countQuestions() == 0)
				return null;
			int i = (int) Math.floor(Math.random() * countQuestions());
			return problems.get(i);
		}

		public void add(Object q, Renderer renderer, int times) {
			problems.add(q);
			problemTimes.put(q, new Integer(times));
			problemRenderers.put(q, renderer);
		}

		public int getTimesFor(Object question) {
			Integer i = (Integer) problemTimes.get(question);
			if (i == null)
				return -1;
			return i.intValue();
		}

		public Renderer getRendererFor(Object question) {
			return (Renderer) problemRenderers.get(question);
		}

		public void setTimesFor(Object question, int times) {
			problemTimes.put(question, new Integer(times));
		}

		public void remove(Object q) {
			problems.remove(q);
			problemTimes.remove(q);
			problemRenderers.remove(q);
		}

		public void finishSession() {
//			problems.removeAll(problems);
			problemTimes.clear();
			problemRenderers.clear();
		}

	}

	public Statistics getStatistics() {
		return statistics;
	}

	public boolean doEvaluate() {
		Renderer r = getRenderer();
		boolean correct = r.isCorrect();
		boolean showSolution = r.isShowSolution();
		if (!showSolution) {
//			System.out.println(r + " => " + correct);
			if (correct) {
				accountProblems(true);
				next();
				return true;
			} else {
				getStatistics().unsuccessfulTry();
			}
		} else {
			accountProblems(false);
		}
		return false;
	}

	public void init() {
		int total = 0;
		for (Iterator iter = listProducers.iterator(); iter.hasNext();) {
			QuestionProducer qp = (QuestionProducer) iter.next();
			qp.initSession();
			total += qp.countQuestions();
		}
		for (Iterator iter = infiniteProducers.iterator(); iter.hasNext();) {
			QuestionProducer qp = (QuestionProducer) iter.next();
			qp.initSession();
		}
		getStatistics().setTotal(total);
	}

	public void destroy() {
		for (Iterator iter = listProducers.iterator(); iter.hasNext();) {
			QuestionProducer qp = (QuestionProducer) iter.next();
			qp.finishSession();
		}
		for (Iterator iter = infiniteProducers.iterator(); iter.hasNext();) {
			QuestionProducer qp = (QuestionProducer) iter.next();
			qp.finishSession();
		}
	}

	public List getProblemsList() {
		return problemsList;
	}

	public Renderer getLastRenderer() {
		return lastRenderer;
	}

}
