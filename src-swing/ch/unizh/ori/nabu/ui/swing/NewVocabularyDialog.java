package ch.unizh.ori.nabu.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import ch.unizh.ori.nabu.ui.VocabularyXMLExporter;
import ch.unizh.ori.nabu.voc.VocabularyConfigurator;
import ch.unizh.ori.nabu.voc.VocabularyConfigurator.SimpleVocabularyConfiguration;

public class NewVocabularyDialog extends JDialog {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(NewVocabularyDialog.class);

	private VocabularyConfigurator.SimpleVocabularyConfiguration result = null;
	
	private int numColumns = 0;
	private JPanel columnsPanel;
	private List<List<JComponent>> columnsComponents = new ArrayList<List<JComponent>>();
	private List<JTextField> modesTFs = new ArrayList<JTextField>();
	private JTextField nameTF;
	private JTextArea description;
	private JCheckBox emptyLinesCB;
	private JComboBox lessonFormatCB;
	private JComboBox encodingCB;
	
	private File vocFile;
	private VocabularyConfigurator.SimpleVocabularyConfiguration origSvc;

	private int springLayoutRows = 0;
	private JTextArea vocabularyTextArea;
	private int tentativeColumns;

	private Map scripts = new HashMap();
	private String[] scriptsCodes = new String[0];
	

	public NewVocabularyDialog(Frame parent, Map scripts, String title, VocabularyConfigurator.SimpleVocabularyConfiguration svc)
		throws HeadlessException {
		this(parent, scripts, title, (File)null);
		populateFromConfiguration(svc);
	}
	public NewVocabularyDialog(Frame parent, Map scripts, String title, File file)
		throws HeadlessException {
		super(parent, title, true);
		setScripts(scripts);
		if(file != null && file.getName().endsWith(".txt"))
			this.vocFile = file;
		initComponents();
		if(file != null && file.getName().endsWith(".properties")){
			try {
				origSvc = new VocabularyConfigurator.SimpleVocabularyConfiguration(file);
				populateFromConfiguration(origSvc);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void populateFromConfiguration(SimpleVocabularyConfiguration svc) {
		nameTF.setText(svc.getName());
		description.setText(svc.getDescription());
		encodingCB.setSelectedItem(svc.getEncoding());
		lessonFormatCB.setSelectedItem(svc.getLessonFormat());
		emptyLinesCB.setSelected(svc.isEmptyline());
		
		clear();
		
		for (String[] arr : svc.getColumns()) {
			addColumn();
			List<JComponent> cc = columnsComponents.get(numColumns-1);
			int i=0;
			for (JComponent component : cc) {
				if(arr.length <= i)
					break;
				if (component instanceof JComboBox) {
					JComboBox cb = (JComboBox) component;
					cb.setSelectedItem(arr[i]);
				}else{
					JTextField tf = (JTextField) component;
					tf.setText(arr[i]);
				}
				i++;
			}
		}
		
		for(String[] arr : svc.getModes()){
			addMode();
			if(arr.length > 1)
				modesTFs.get(modesTFs.size()-1).setText(arr[1]);
			HashSet given = new HashSet();
			HashSet asked = new HashSet();
			for(String s : arr[0].split("=")){
				if(s.startsWith("?"))
					asked.add(s.substring(1));
				else
					given.add(s);
			}
			
			for(List<JComponent> cc : columnsComponents) {
				String id = ((JTextField) cc.get(0)).getText();
				JComboBox cb = (JComboBox) cc.get(cc.size()-1);
				if(given.contains(id)){
					cb.setSelectedIndex(1);
				}else if(asked.contains(id)){
					cb.setSelectedIndex(2);
				}
			}
		}
		
	}
	
	public SimpleVocabularyConfiguration populateToConfiguration(){
		List<String[]> columns = new ArrayList<String[]>(numColumns);
		List<String[]> modes = new ArrayList<String[]>(modesTFs.size());
		
		for(List<JComponent> cc : columnsComponents){
			String[] col = new String[5];
			for(int i=0; i<5; i++){
				if (cc.get(i) instanceof JComboBox) {
					JComboBox cb = (JComboBox) cc.get(i);
					col[i] = (String) cb.getSelectedItem();
				} else {
					col[i] = ((JTextField)cc.get(i)).getText();
				}
			}
			columns.add(trimArray(col));
		}
		
		for(int i=0; i<modesTFs.size(); i++){
			String name = modesTFs.get(i).getText();
			List given = new ArrayList();
			List asked = new ArrayList();
			for(List<JComponent> cc : columnsComponents){
				String id = ((JTextField) cc.get(0)).getText();
				JComboBox cb = (JComboBox) cc.get(5+i);
				switch (cb.getSelectedIndex()) {
				case 1:
					given.add(id);
					break;
				case 2:
					asked.add("?"+id);
					break;
				}
			}
			given.addAll(asked);
			String[] arr;
			if(name != null && name.length() > 0)
				arr = new String[]{ VocabularyConfigurator.join(given,"="), name };
			else
				arr = new String[]{ VocabularyConfigurator.join(given,"=") };
			modes.add(trimArray(arr));
		}
		
		VocabularyConfigurator.SimpleVocabularyConfiguration svc = origSvc;
		if(origSvc != null) {
			svc.setColumns(columns);
			svc.setModes(modes);
		} else {
			svc = new VocabularyConfigurator.SimpleVocabularyConfiguration(null, columns, modes);
			if(vocFile != null){
				String filename = vocFile.getName();
				int i = filename.lastIndexOf(".");
				svc.setId(filename.substring(0, i>0?i:filename.length()));
			}
		}
		
		svc.setName(nameTF.getText());
		svc.setDescription(description.getText());
		svc.setLessonFormat((String) lessonFormatCB.getSelectedItem());
		svc.setEmptyline(emptyLinesCB.isSelected());
		svc.setEncoding((String) encodingCB.getSelectedItem());
		
		svc.setChangeUser(System.getProperty("user.name"));
		svc.setChangeDate(new Date().toString());
		try {
			svc.setChangeIp(InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return svc;
		
	}
	
	public static String[] trimArray(String[] arr){
		int j;
		for(j=arr.length; j>0; j--){
			if(!"".equals(arr[j-1]))
				break;
		}
		if(j != arr.length) {
			String[] tmp = new String[j];
			System.arraycopy(arr, 0, tmp, 0, j);
			arr = tmp;
		}
		return arr;
	}
	
	private void initComponents() {
		JPanel contentPanel = new JPanel(new SpringLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		nameTF = new JTextField();
		addLabeledComponent(contentPanel, "Name:", nameTF);
		
		description = new JTextArea(3,50);
		addLabeledComponent(contentPanel, "Description:", description);
		description.setDocument(nameTF.getDocument());
		
		if(vocFile!=null && vocFile.canRead()){
			vocabularyTextArea = new JTextArea(4,80);
			vocabularyTextArea.setEditable(false);
			addLabeledComponent(contentPanel, "Vocabulary", vocabularyTextArea);
			vocFileHeuristics();
		}
		
		addLabeledComponent(contentPanel, "Columns", createColumnsPanel());
		
//		addLabeledComponent(contentPanel, "Modes", createModesPanel());
		
		emptyLinesCB = new JCheckBox("Lessons divided by empty lines");
		addLabeledComponent(contentPanel, "", emptyLinesCB);
		lessonFormatCB = new JComboBox(new String[]{"Lesson {0}","Verbs,Nouns,Pronomina"});
		lessonFormatCB.setEditable(true);
		addLabeledComponent(contentPanel, "Lesson-Format", lessonFormatCB);
		encodingCB = new JComboBox(new String[]{"UTF-8","ISO-8859-1"});
		encodingCB.setEditable(true);
		addLabeledComponent(contentPanel, "Encoding", encodingCB);
		
		SpringUtilities.makeCompactGrid(contentPanel,
                springLayoutRows, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		
		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		JButton cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result = null;
				setVisible(false);
			}
		});
		
		JButton OKButton = new JButton("OK");
		buttonPanel.add(OKButton);
		OKButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result = populateToConfiguration();
				setVisible(false);
			}
		});
		
		JButton savePropertiesButton = new JButton("Save");
//		buttonPanel.add(savePropertiesButton);
		savePropertiesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		
		KeyStroke escKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		
		getRootPane().registerKeyboardAction(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				result = null;
				setVisible(false);
			}
		}, escKeyStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		getRootPane().registerKeyboardAction(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				result = populateToConfiguration();
				setVisible(false);
			}
		}, enterKeyStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		setSize(800, 800);
//		pack();
	}
	

	private void vocFileHeuristics() {
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(vocFile), "UTF-8"));
			String line = in.readLine();
			if(line == null)
				return;
			tentativeColumns = Math.max(line.split("\t").length, 1);
			vocabularyTextArea.setText(line);
			line = in.readLine();
			if(line == null)
				return;
			vocabularyTextArea.append("\n"+line);
			line = in.readLine();
			if(line == null)
				return;
			vocabularyTextArea.append("\n"+line);
			line = in.readLine();
			if(line == null)
				return;
			vocabularyTextArea.append("\n...");
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	private JComponent createColumnsPanel() {
		columnsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.anchor = GridBagConstraints.EAST;
		
		columnsPanel.add(new JLabel("ID"),gc);
		gc.gridy++;
		columnsPanel.add(new JLabel("Label"),gc);
		gc.gridy++;
		columnsPanel.add(new JLabel("Script"),gc);
		gc.gridy++;
		columnsPanel.add(new JLabel("Delimiters"),gc);
		gc.gridy++;
		columnsPanel.add(new JLabel("Sound"),gc);
		
		JScrollPane scrollPane = new JScrollPane(columnsPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel ret = new JPanel(new BorderLayout());
		ret.add(scrollPane, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		JButton addColumnButton = new JButton("+");
		addColumnButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				addColumn();
			}
		});
		buttonsPanel.add(addColumnButton);
		JButton removeColumnButton = new JButton("-");
		buttonsPanel.add(removeColumnButton);
		removeColumnButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				removeLastColumn();
			}
		});
		
		for(int i=0; i<tentativeColumns; i++)
			addColumn();

		JButton addModeButton = new JButton("add mode");
		addModeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				addMode();
			}
		});
		buttonsPanel.add(addModeButton);
		
		JButton removeModeButton = new JButton("remove last mode");
		removeModeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				removeLastMode();
			}
		});
		buttonsPanel.add(removeModeButton);
		
		addMode();
		
		ret.add(buttonsPanel, BorderLayout.EAST);
		
		return ret;
	}

	private void clear() {
		while(numColumns>0){
			removeLastColumn();
		}
		for(JTextField mode : modesTFs){
			columnsPanel.remove(mode);
		}
		modesTFs.clear();
	}
	
	private void addColumn() {
		numColumns ++;
		List<JComponent> cc = new ArrayList<JComponent>(5);
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridy = 0;
		gc.gridx = numColumns;
		
		for(int i=0; i<5; i++){
			JComponent comp;
			if(i != 2) {
				comp = new JTextField(9);
			}else{
				JComboBox cb = new JComboBox(scriptsCodes);
				cb.setRenderer(new DefaultListCellRenderer(){
					@Override
					public Component getListCellRendererComponent(JList list,
							Object value, int index, boolean isSelected,
							boolean cellHasFocus) {
						if(value != null)
							value = scripts.get(value);
						return super.getListCellRendererComponent(list, value, index, isSelected,
								cellHasFocus);
					}
				});
				comp = cb;
			}
			cc.add(comp);
			columnsPanel.add(comp,gc);
			gc.gridy++;
		}
		for(int i=0; i<modesTFs.size(); i++){
			JComboBox cb = new JComboBox(new String[]{"Not visible", "Given", "Asked"});
			cc.add(cb);
			columnsPanel.add(cb,gc);
			gc.gridy ++ ;
		}
		columnsPanel.revalidate();
		columnsPanel.repaint();
		columnsComponents.add(cc);
	}

	protected void removeLastColumn() {
		if(numColumns==0){
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		List<JComponent> cc = columnsComponents.remove(--numColumns);
		for (JComponent component : cc) {
			columnsPanel.remove(component);
		}
		columnsPanel.revalidate();
		columnsPanel.repaint();
	}
	
	protected void addMode(){
		JTextField tf = new JTextField(6);
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridy = 5 + modesTFs.size();
		gc.gridx = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;
		columnsPanel.add(tf, gc);
		for (List<JComponent> components : columnsComponents) {
			JComboBox cb = new JComboBox(new String[]{"Not visible", "Given", "Asked"});
			components.add(cb);
			gc.gridx ++ ;
			columnsPanel.add(cb,gc);
		}
//		gc.gridx = 100;
//		columnsPanel.add(new JButton("-"),gc);
		modesTFs.add(tf);
		columnsPanel.revalidate();
		columnsPanel.repaint();
	}
	
	protected void removeLastMode(){
		for (List<JComponent> components : columnsComponents) {
			columnsPanel.remove(components.remove(5+modesTFs.size()-1));
		}
		columnsPanel.remove(modesTFs.remove(modesTFs.size()-1));
		columnsPanel.revalidate();
		columnsPanel.repaint();
	}
	
	private void addLabeledComponent(JPanel contentPanel, String label, JComponent component) {
		JLabel jlabel;
		jlabel = new JLabel(label, JLabel.TRAILING);
		contentPanel.add(jlabel);
		jlabel.setLabelFor(component);
		contentPanel.add(component);
		springLayoutRows++;
	}
	
	public void save() {
		try {
			SimpleVocabularyConfiguration svc = populateToConfiguration();
			svc.getVars().store(new BufferedOutputStream(new FileOutputStream("/Users/pht/Desktop/test.properties")), "Does it work?");
			Document d = VocabularyConfigurator.export(svc);
			VocabularyXMLExporter.writeXmlFile(d, "/Users/pht/Desktop/test.xml");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	public static void main(String[] args) {

		try {
			SimpleVocabularyConfiguration svc = VocabularyConfigurator.configurationFromFile("web/WEB-INF/subs/test/es.esoes.properties");
			new NewVocabularyDialog(null, null, "New Vocabulary", svc).setVisible(true);
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A 1.4 file that provides utility methods for
	 * creating form- or grid-style layouts with SpringLayout.
	 * These utilities are used by several programs, such as
	 * SpringBox and SpringCompactGrid.
	 */
	public static class SpringUtilities {
	    /**
	     * A debugging utility that prints to stdout the component's
	     * minimum, preferred, and maximum sizes.
	     */
	    public static void printSizes(Component c) {
	        System.out.println("minimumSize = " + c.getMinimumSize());
	        System.out.println("preferredSize = " + c.getPreferredSize());
	        System.out.println("maximumSize = " + c.getMaximumSize());
	    }

	    /**
	     * Aligns the first <code>rows</code> * <code>cols</code>
	     * components of <code>parent</code> in
	     * a grid. Each component is as big as the maximum
	     * preferred width and height of the components.
	     * The parent is made just big enough to fit them all.
	     *
	     * @param rows number of rows
	     * @param cols number of columns
	     * @param initialX x location to start the grid at
	     * @param initialY y location to start the grid at
	     * @param xPad x padding between cells
	     * @param yPad y padding between cells
	     */
	    public static void makeGrid(Container parent,
	                                int rows, int cols,
	                                int initialX, int initialY,
	                                int xPad, int yPad) {
	        SpringLayout layout;
	        try {
	            layout = (SpringLayout)parent.getLayout();
	        } catch (ClassCastException exc) {
	            System.err.println("The first argument to makeGrid must use SpringLayout.");
	            return;
	        }

	        Spring xPadSpring = Spring.constant(xPad);
	        Spring yPadSpring = Spring.constant(yPad);
	        Spring initialXSpring = Spring.constant(initialX);
	        Spring initialYSpring = Spring.constant(initialY);
	        int max = rows * cols;

	        //Calculate Springs that are the max of the width/height so that all
	        //cells have the same size.
	        Spring maxWidthSpring = layout.getConstraints(parent.getComponent(0)).
	                                    getWidth();
	        Spring maxHeightSpring = layout.getConstraints(parent.getComponent(0)).
	                                    getWidth();
	        for (int i = 1; i < max; i++) {
	            SpringLayout.Constraints cons = layout.getConstraints(
	                                            parent.getComponent(i));

	            maxWidthSpring = Spring.max(maxWidthSpring, cons.getWidth());
	            maxHeightSpring = Spring.max(maxHeightSpring, cons.getHeight());
	        }

	        //Apply the new width/height Spring. This forces all the
	        //components to have the same size.
	        for (int i = 0; i < max; i++) {
	            SpringLayout.Constraints cons = layout.getConstraints(
	                                            parent.getComponent(i));

	            cons.setWidth(maxWidthSpring);
	            cons.setHeight(maxHeightSpring);
	        }

	        //Then adjust the x/y constraints of all the cells so that they
	        //are aligned in a grid.
	        SpringLayout.Constraints lastCons = null;
	        SpringLayout.Constraints lastRowCons = null;
	        for (int i = 0; i < max; i++) {
	            SpringLayout.Constraints cons = layout.getConstraints(
	                                                 parent.getComponent(i));
	            if (i % cols == 0) { //start of new row
	                lastRowCons = lastCons;
	                cons.setX(initialXSpring);
	            } else { //x position depends on previous component
	                cons.setX(Spring.sum(lastCons.getConstraint(SpringLayout.EAST),
	                                     xPadSpring));
	            }

	            if (i / cols == 0) { //first row
	                cons.setY(initialYSpring);
	            } else { //y position depends on previous row
	                cons.setY(Spring.sum(lastRowCons.getConstraint(SpringLayout.SOUTH),
	                                     yPadSpring));
	            }
	            lastCons = cons;
	        }

	        //Set the parent's size.
	        SpringLayout.Constraints pCons = layout.getConstraints(parent);
	        pCons.setConstraint(SpringLayout.SOUTH,
	                            Spring.sum(
	                                Spring.constant(yPad),
	                                lastCons.getConstraint(SpringLayout.SOUTH)));
	        pCons.setConstraint(SpringLayout.EAST,
	                            Spring.sum(
	                                Spring.constant(xPad),
	                                lastCons.getConstraint(SpringLayout.EAST)));
	    }

	    /* Used by makeCompactGrid. */
	    private static SpringLayout.Constraints getConstraintsForCell(
	                                                int row, int col,
	                                                Container parent,
	                                                int cols) {
	        SpringLayout layout = (SpringLayout) parent.getLayout();
	        Component c = parent.getComponent(row * cols + col);
	        return layout.getConstraints(c);
	    }

	    /**
	     * Aligns the first <code>rows</code> * <code>cols</code>
	     * components of <code>parent</code> in
	     * a grid. Each component in a column is as wide as the maximum
	     * preferred width of the components in that column;
	     * height is similarly determined for each row.
	     * The parent is made just big enough to fit them all.
	     *
	     * @param rows number of rows
	     * @param cols number of columns
	     * @param initialX x location to start the grid at
	     * @param initialY y location to start the grid at
	     * @param xPad x padding between cells
	     * @param yPad y padding between cells
	     */
	    public static void makeCompactGrid(Container parent,
	                                       int rows, int cols,
	                                       int initialX, int initialY,
	                                       int xPad, int yPad) {
	        SpringLayout layout;
	        try {
	            layout = (SpringLayout)parent.getLayout();
	        } catch (ClassCastException exc) {
	            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
	            return;
	        }

	        //Align all cells in each column and make them the same width.
	        Spring x = Spring.constant(initialX);
	        for (int c = 0; c < cols; c++) {
	            Spring width = Spring.constant(0);
	            for (int r = 0; r < rows; r++) {
	                width = Spring.max(width,
	                                   getConstraintsForCell(r, c, parent, cols).
	                                       getWidth());
	            }
	            for (int r = 0; r < rows; r++) {
	                SpringLayout.Constraints constraints =
	                        getConstraintsForCell(r, c, parent, cols);
	                constraints.setX(x);
	                constraints.setWidth(width);
	            }
	            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
	        }

	        //Align all cells in each row and make them the same height.
	        Spring y = Spring.constant(initialY);
	        for (int r = 0; r < rows; r++) {
	            Spring height = Spring.constant(0);
	            for (int c = 0; c < cols; c++) {
	                height = Spring.max(height,
	                                    getConstraintsForCell(r, c, parent, cols).
	                                        getHeight());
	            }
	            for (int c = 0; c < cols; c++) {
	                SpringLayout.Constraints constraints =
	                        getConstraintsForCell(r, c, parent, cols);
	                constraints.setY(y);
	                constraints.setHeight(height);
	            }
	            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
	        }

	        //Set the parent's size.
	        SpringLayout.Constraints pCons = layout.getConstraints(parent);
	        pCons.setConstraint(SpringLayout.SOUTH, y);
	        pCons.setConstraint(SpringLayout.EAST, x);
	    }
	}

	public VocabularyConfigurator.SimpleVocabularyConfiguration getResult() {
		return result;
	}
	public void setScripts(Map scripts) {
		if(scripts == null){
			scripts = new HashMap();
		}
		this.scripts = scripts;
		scriptsCodes = (String[]) scripts.keySet().toArray(new String[0]);
	}

}
