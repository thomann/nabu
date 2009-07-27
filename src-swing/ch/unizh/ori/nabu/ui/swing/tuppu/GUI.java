/*
 * Created on 05.01.2005
 *
 */
package ch.unizh.ori.nabu.ui.swing.tuppu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ch.unizh.ori.nabu.ui.swing.AppFrame;
import ch.unizh.ori.tuppu.Plotter;
import ch.unizh.ori.tuppu.StringPlotter;


public class GUI extends AppFrame implements ActionListener {

	private ch.unizh.ori.nabu.ui.swing.tuppu.PlotLabel plotLabel = new ch.unizh.ori.nabu.ui.swing.tuppu.PlotLabel();

	private JTextField plotterTextField = new JTextField(
			StringPlotter.class.getName());

	private JTextField textTextField = new JTextField("Hello World!", 20);

	private JTextArea paramsTextArea = new JTextArea();

	public GUI() {
		super("Plotting", GUI.class.getName(), new Dimension(200, 100));
		getContentPane().add(plotLabel);

		JPanel p = new JPanel(new BorderLayout());
		JPanel p2 = new JPanel(new GridLayout(0, 1));
		p2.add(plotterTextField);
		p2.add(textTextField);
		p.add(p2, BorderLayout.WEST);
		p.add(new JScrollPane(paramsTextArea));
		JButton b = new JButton("Update");
		b.setMnemonic('u');
		p.add(b, BorderLayout.EAST);
		getContentPane().add(p, BorderLayout.SOUTH);

		b.addActionListener(this);
		plotterTextField.addActionListener(this);
		textTextField.addActionListener(this);

		doUpdate();
	}

	public void doUpdate() {
		try {
			Plotter p = (Plotter) Class.forName(plotterTextField.getText())
					.newInstance();

			Properties props = new Properties();
			props.load(new ByteArrayInputStream(paramsTextArea.getText()
					.getBytes()));
			plotLabel.setAll(p, textTextField.getText(), props);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new GUI().setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		doUpdate();
	}

}