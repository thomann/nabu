/*
 * NabuSession.java
 *
 * Created on 13. April 2003, 15:45
 */

package ch.unizh.ori.nabu.ui.swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.stat.Statistics;

/**
 * 
 * @author pht
 */
public class NabuSession extends javax.swing.JPanel {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(NabuSession.class);

	/** Creates new form NabuSession */
	public NabuSession() {
		initComponents();

		// if(Speak.doSpeak()){
		// speakButton = new javax.swing.JButton();
		// speakButton.setMnemonic('p');
		// speakButton.setText("Speak");
		// speakButton.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// speak();
		// }
		// });
		// buttPanel.add(speakButton);
		// }
	}

	public void postAdd() {
		okButton.setDefaultCapable(true);
		SwingUtilities.getRootPane(this).setDefaultButton(okButton);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() { // GEN-BEGIN:initComponents
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(1);
		gridLayout.setHgap(0);
		gridLayout.setVgap(0);
		rendererPanel = new javax.swing.JPanel();
		southPanel = new javax.swing.JPanel();
		buttPanel = new javax.swing.JPanel();
		okButton = new javax.swing.JButton();
		showSolutionButton = new javax.swing.JButton();
		problemsOnlyCB = new javax.swing.JCheckBox();
		playCB = new javax.swing.JCheckBox();
		statusPanel = new javax.swing.JPanel();
		// suppose it's no problem
		maxProblems = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));

		setLayout(new java.awt.BorderLayout());

		rendererPanel.setLayout(new java.awt.BorderLayout());

		add(rendererPanel, java.awt.BorderLayout.CENTER);

		southPanel.setLayout(new java.awt.BorderLayout());

		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ok();
			}
		});

		buttPanel.add(okButton);

		showSolutionButton.setMnemonic('s');
		showSolutionButton.setText("Show Solution");
		showSolutionButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						process(true);
					}
				});

		buttPanel.add(showSolutionButton);

		problemsOnlyCB.setMnemonic('p');
		problemsOnlyCB.setText("Problems Only");
		problemsOnlyCB
				.addChangeListener(new javax.swing.event.ChangeListener() {
					public void stateChanged(javax.swing.event.ChangeEvent evt) {
						if (getIter() != null) {
							getIter().setProblemsOnly(
									problemsOnlyCB.isSelected());
						}
					}
				});

		buttPanel.add(problemsOnlyCB);

		playCB.setMnemonic('p');
		playCB.setText("Play Sound");
		playCB.setSelected(true);
		buttPanel.add(playCB);

		buttPanel.add(maxProblems);

		southPanel.add(buttPanel, java.awt.BorderLayout.CENTER);

		statusPanel.setLayout(gridLayout);
		statusPanel.add(getCountPanel(), null);
		statusPanel.add(getLeftStatusPanel(), null);
		southPanel.add(statusPanel, java.awt.BorderLayout.SOUTH);

		add(southPanel, java.awt.BorderLayout.SOUTH);

	} // GEN-END:initComponents

	public void updateRenderer() {
		SwingRenderer r = (SwingRenderer) getIter().getRenderer();
		if(r == null && iter.isProblemsOnly()){
			problemsOnlyCB.setSelected(false);
			iter.next();
			r = (SwingRenderer) getIter().getRenderer();
		}
		rendererPanel.removeAll();
		if (r != null) {
			r.setSession(this);
			rendererPanel.add(r.getComponent(), BorderLayout.CENTER);
			r.activate();
			speak();
		} else {
			okButton.setEnabled(false);
			showSolutionButton.setEnabled(false);
			problemsOnlyCB.setEnabled(false);
		}
		updateStats();
		repaint();
	}

	public void process(boolean shouldShowSolution) {
		SwingRenderer r = (SwingRenderer) getIter().getRenderer();
		r.process(shouldShowSolution);
		if (iter.doEvaluate()) {
			updateRenderer();
		} else if (r.isShowSolution()) {
			r.showSolution();
			okButton.requestFocus();
			updateStats();
		} else {
			r.activate();
		}

	}

	public void init() {
		getIter().init();
	}

	public void ok() {
		int mp = ((Integer) maxProblems.getValue()).intValue();
		if (mp > 0) {
			problemsOnlyCB.setSelected(iter.countProblems() >= mp);
		}
		if (iter.getRenderer().isShowSolution()) {
			getIter().next();
			updateRenderer();
		} else {
			process(false);
		}
	}

	public void showSolution() {
		process(true);
	}

	private int total;

	private int asked;

	public void updateStats() {
		// statLabel.setText(asked+" / "+total+" / "+getIter().countProblems());
		Statistics stat = getIter().getStatistics();
		statLabel.setText(stat.getCompleted() + " / " + stat.getTotal() + " / "
				+ stat.getProblems());
		log.info(stat);
		if (getIter() != null && getIter().getQuestion() != null) {
			int i = getIter().getTimesForProblem(getIter().getQuestion());
			if (i > 0) {
				problemLabel.setText(String.valueOf(i));
			} else {
				problemLabel.setText("None");
			}
			finishedLabel.setText(String.valueOf(stat.getCompleted()));
			problemsLabel.setText(String.valueOf(stat.getProblems()));
			int rest = stat.getTotal()
					- stat.getCompleted() - stat.getProblems();
			restLabel.setText(String.valueOf(rest));
			int factor = 10;
			countPanelLayout.columnWeights = new double[]{factor*stat.getCompleted(),factor*stat.getProblems(),factor*rest};
		} else {
			problemLabel.setText("");
			finishedLabel.setText("-");
			problemsLabel.setText("-");
			restLabel.setText("");
			countPanelLayout.columnWeights = new double[]{1,1,1};
		}
	}

	public void speak() {
		// System.err.println("Speaking!");
		// if(getR() instanceof Speakable){
		// ((Speakable)getR()).speak();
		// }else{
		// //
		// Speak.getDefault().speak(getIter().getQuestion().getQuestionString());
		// }
		// if(getR() != null){
		// getR().activate();
		// }
	}

	/**
	 * Getter for property iter.
	 * 
	 * @return Value of property iter.
	 */
	public QuestionIterator getIter() {
		return this.iter;
	}

	/**
	 * Setter for property iter.
	 * 
	 * @param iter
	 *            New value of property iter.
	 */
	public void setIter(QuestionIterator iter) {
		this.iter = iter;
		if (iter != null) {
			total = iter.countQuestions();
			asked = 0;
		}
	}

	private javax.swing.JPanel statusPanel;

	private javax.swing.JPanel buttPanel;

	private javax.swing.JButton okButton;

	private javax.swing.JPanel southPanel;

	private javax.swing.JPanel rendererPanel;

	private javax.swing.JCheckBox problemsOnlyCB;

	private javax.swing.JCheckBox playCB;

	private javax.swing.JButton showSolutionButton;

	// End of variables declaration//GEN-END:variables

	private JSpinner maxProblems;

	private javax.swing.JButton speakButton;

	/** Holds value of property iter. */
	private QuestionIterator iter;

	private JPanel countPanel = null;

	private JPanel leftStatusPanel = null;

	private JLabel jLabel = null;

	private JLabel statLabel = null;

	private JLabel jLabel2 = null;

	private JLabel problemLabel = null;

	private JPanel problemColorPanel = null;

	private JPanel restColorPanel = null;

	private JPanel finishedColorPanel = null;

	private JLabel finishedLabel = null;

	private JLabel problemsLabel = null;

	private JLabel restLabel = null;

	private GridBagLayout countPanelLayout;

	public JButton getOkButton() {
		return okButton;
	}

	public boolean isPlay() {
		return playCB.isSelected();
	}

	/**
	 * This method initializes countPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCountPanel() {
		GridBagConstraints gridBagConstraints = null;
		GridBagConstraints gridBagConstraints1 = null;
		if (countPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.gridy = 1;
			restLabel = new JLabel();
			restLabel.setText("1");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 1;
			problemsLabel = new JLabel();
			problemsLabel.setText("1");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			finishedLabel = new JLabel();
			finishedLabel.setText("1");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 2;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			countPanel = new JPanel();
			countPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(2,2,2,2), javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray,1)));
			countPanelLayout = new GridBagLayout();
			countPanel.setLayout(countPanelLayout);
			countPanel.add(getProblemColorPanel(), gridBagConstraints);
			countPanel.add(getRestColorPanel(), gridBagConstraints1);
			countPanel.add(getFinishedColorPanel(), gridBagConstraints11);
			countPanel.add(finishedLabel, gridBagConstraints2);
			countPanel.add(problemsLabel, gridBagConstraints3);
			countPanel.add(restLabel, gridBagConstraints4);
		}
		return countPanel;
	}

	/**
	 * This method initializes leftStatusPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getLeftStatusPanel() {
		if (leftStatusPanel == null) {
			problemLabel = new JLabel();
			problemLabel.setFont(new Font("Courier", 0, 14));
			problemLabel.setText("0");
			jLabel2 = new JLabel();
			jLabel2.setText("Repetitions to do:");
			statLabel = new JLabel();
			statLabel.setFont(new Font("Courier", 0, 14));
			statLabel.setText("5/7");
			statLabel.setVisible(false);
			jLabel = new JLabel();
			jLabel.setText("Count:");
			jLabel.setVisible(false);
			leftStatusPanel = new JPanel();
			leftStatusPanel.add(jLabel, null);
			leftStatusPanel.add(statLabel, null);
			leftStatusPanel.add(jLabel2, null);
			leftStatusPanel.add(problemLabel, null);
		}
		return leftStatusPanel;
	}

	/**
	 * This method initializes problemColorPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProblemColorPanel() {
		if (problemColorPanel == null) {
			problemColorPanel = new JPanel();
			problemColorPanel.setBackground(java.awt.Color.red);
		}
		return problemColorPanel;
	}

	/**
	 * This method initializes restColorPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRestColorPanel() {
		if (restColorPanel == null) {
			restColorPanel = new JPanel();
			restColorPanel.setBackground(java.awt.Color.white);
		}
		return restColorPanel;
	}

	/**
	 * This method initializes finishedColorPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getFinishedColorPanel() {
		if (finishedColorPanel == null) {
			finishedColorPanel = new JPanel();
			finishedColorPanel.setBackground(java.awt.Color.black);
		}
		return finishedColorPanel;
	}

}