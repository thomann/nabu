/*
 * Manager.java
 *
 * Created on 13. April 2003, 15:51
 */

package ch.unizh.ori.nabu.ui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;

import ch.unizh.ori.nabu.catalog.QuestionProducerDescription;
import ch.unizh.ori.nabu.core.Central;
import ch.unizh.ori.nabu.core.DefaultQuestionIterator;
import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.ui.VocabularyXMLExporter;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.Vocabulary;
import ch.unizh.ori.nabu.voc.VocabularyConfigurator;
import ch.unizh.ori.nabu.voc.VocabularyConfigurator.SimpleVocabularyConfiguration;

/**
 *
 * @author  pht
 */
public class Manager extends JPanel implements ListSelectionListener {
	
	private SwingCentral central;

	private JList vocList;
	private JList modesList;
	private JList lessonsList;
	
	private JButton startButton;

	private JButton openFolderButton = null;
	
	private JButton newVocabularyButton = null;

	private JButton editVocabularyButton;

	private JButton downloadButton;

	private JButton uploadButton;

	/** Creates new form Manager */
	public Manager() {
		initComponents();
		
		this.central = new SwingCentral();
		reload();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		JScrollPane sp;
		
		vocList = new JList();
		vocList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		vocList.addListSelectionListener(this);
		sp = new JScrollPane(vocList);
		decorateWithBorder(sp, "Vocabularies:");
		add(sp, BorderLayout.LINE_START);
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		
		modesList = new JList();
		modesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		modesList.addListSelectionListener(this);
		sp = new JScrollPane(modesList);
		decorateWithBorder(sp, "Modes:");
		p.add(sp);
		
		lessonsList = new JList();
		lessonsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		lectionsList.addListSelectionListener(this);
		sp = new JScrollPane(lessonsList);
		decorateWithBorder(sp, "Lections:");
		p.add(sp);
		
		add(p, BorderLayout.CENTER);
		
		p = new JPanel(new FlowLayout(FlowLayout.CENTER));

		startButton = new JButton("Start Session");
		startButton.setMnemonic('s');
		p.add(startButton);
		startButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				startSession();
			}
		});
		
		JButton reloadButton = new JButton("Reload");
		reloadButton.setMnemonic('r');
		p.add(reloadButton);
		p.add(getOpenFolderButton(), null);
		reloadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				reload();
			}
		});
		
		newVocabularyButton = new JButton("New Vocabulary");
		newVocabularyButton.setMnemonic('n');
		p.add(newVocabularyButton);
		newVocabularyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				newVocabulary();
			}
		});
		
		editVocabularyButton = new JButton("Edit Vocabulary Configuration");
		editVocabularyButton.setMnemonic('e');
		p.add(editVocabularyButton);
		editVocabularyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				editVocabulary();
			}
		});
		
		downloadButton = new JButton("Download");
		downloadButton.setMnemonic('d');
		p.add(downloadButton);
		downloadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new Download(Manager.this);
			}
		});
		
		uploadButton = new JButton("Upload");
		uploadButton.setMnemonic('u');
		p.add(uploadButton);
		uploadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				upload();
			}
		});
		
		add(p, BorderLayout.SOUTH);
	}
	
	private File lastUsedDirectory = new File(System.getProperty("user.home"), "vocs");

	protected void newVocabulary() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileFilter(){

			public boolean accept(File f) {
				return f.getName().endsWith(".txt");
			}

			public String getDescription() {
				return "Text-Files";
			}
			
		});
		fc.setCurrentDirectory(lastUsedDirectory);
		if(fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		lastUsedDirectory = fc.getSelectedFile().getParentFile();
		NewVocabularyDialog nvd = new NewVocabularyDialog(appFrame, central.getScripts(), "New Vocabulary", fc.getSelectedFile());
		nvd.setVisible(true);
		SimpleVocabularyConfiguration svc = nvd.getResult();
		if(svc == null){
			return;
		}
		fc.setSelectedFile(new File(lastUsedDirectory, svc.getId()+".properties"));
		if(fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		lastUsedDirectory = fc.getSelectedFile().getParentFile();
		try {
			File propertiesFile = fc.getSelectedFile();
			svc.storeProperties(propertiesFile, "Created using NABU");
			String name = propertiesFile.getName();
			if(name.endsWith(".properties"))
				name = name.substring(0,name.length()-".properties".length());
			Document d = VocabularyConfigurator.export(svc);
			VocabularyXMLExporter.writeXmlFile(d, new File(lastUsedDirectory, name+".xml").getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	protected void editVocabulary() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileFilter(){

			public boolean accept(File f) {
				return f.getName().endsWith(".properties");
			}

			public String getDescription() {
				return "Properties-Files";
			}
			
		});
		fc.setCurrentDirectory(lastUsedDirectory);
		if(fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		lastUsedDirectory = fc.getSelectedFile().getParentFile();
		NewVocabularyDialog nvd = new NewVocabularyDialog(appFrame, central.getScripts(), "Edit Vocabulary Configuration", fc.getSelectedFile());
		nvd.setVisible(true);
		SimpleVocabularyConfiguration svc = nvd.getResult();
		if(svc == null){
			return;
		}
		try {
			File propertiesFile = fc.getSelectedFile();
			svc.storeProperties(propertiesFile, "Created using NABU");
			String name = propertiesFile.getName();
			if(name.endsWith(".properties"))
				name = name.substring(0,name.length()-".properties".length());
			Document d = VocabularyConfigurator.export(svc);
			VocabularyXMLExporter.writeXmlFile(d, new File(lastUsedDirectory, name+".xml").getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	private void decorateWithBorder(JScrollPane sp, String title) {
		TitledBorder b = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title);
	sp.setBorder(b);
	}

	private void myInit() {
		startButton.setDefaultCapable(true);
		MouseAdapter listener = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					startSession();
				}
			}
		};
		lessonsList.addMouseListener(listener);
		modesList.addMouseListener(listener);
	}

	private void postAdd() {
		SwingUtilities.getRootPane(this).setDefaultButton(startButton);
	}

	public void addAll(DefaultMutableTreeNode node, java.util.List l) {
		if (l == null)
			return;
		for (Iterator i = l.iterator(); i.hasNext();) {
			QuestionProducerDescription qpd =
				(QuestionProducerDescription) i.next();
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(qpd);
			node.add(node2);
			addAll(node2, qpd.getSubQuestionProducerDescriptions());
		}
	}
	
	public void startSession(){
		Vocabulary voc = (Vocabulary) vocList.getSelectedValue();
		Mode m = (Mode) modesList.getSelectedValue();
		List lections = Arrays.asList(lessonsList.getSelectedValues());
		if(voc != null && m != null && lections != null && lections.size()>0){
			createNabuSession(voc, m, lections);
		}
	}
	
	public void upload(){
		Vocabulary voc = (Vocabulary) vocList.getSelectedValue();
		new Upload(voc);
	}
	
	private List addedDirs = new ArrayList();

	private JFrame appFrame;

	public void reload(){
		getCentral().clear();
		getCentral().readResource();
		getCentral().readDir(System.getProperty("user.home")+"/vocs/");
//		central.readFiles(System.getProperty("user.dir")+"/vocs/");
		if(System.getProperty("vocs")!=null){
			getCentral().readDirs(System.getProperty("vocs"));
		}
		if(System.getenv("vocs")!=null){
			getCentral().readDirs(System.getenv("vocs"));
		}
		for (Iterator iter = addedDirs.iterator(); iter.hasNext();) {
			File dir = (File) iter.next();
			getCentral().readDir(dir.getAbsolutePath());
		}
		updateLists();
	}

	private void updateLists() {
		DefaultListModel listModel = new DefaultListModel();
		for (Iterator iter = getCentral().getVocs().keySet().iterator(); iter.hasNext();) {
			Object voc = iter.next();
			listModel.addElement(getCentral().getVocs().get(voc));
		}
		vocList.setModel(listModel);
		revalidate();
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == vocList){
			Vocabulary v = (Vocabulary) vocList.getSelectedValue();
			if(v != null){
				DefaultListModel listModel = new DefaultListModel();
				for (Iterator iter = v.getModes().keySet().iterator(); iter.hasNext();) {
					Object m = iter.next();
					listModel.addElement(v.getModes().get(m));
				}
				modesList.setModel(listModel);
				if(listModel.size() > 0){
					modesList.setSelectedIndex(0);
				}
				
				listModel = new DefaultListModel();
				for (Iterator iter = v.getLections().iterator(); iter.hasNext();) {
					listModel.addElement(iter.next());
				}
				lessonsList.setModel(listModel);
				if(listModel.size() > 0){
					lessonsList.setSelectedIndex(0);
				}
			}else{
				((DefaultListModel)modesList.getModel()).removeAllElements();
				((DefaultListModel)lessonsList.getModel()).removeAllElements();
			}
		}
	}
	
	private static void createNabuSession(Vocabulary voc, Mode m, List lections) {
		SwingMappingRenderer r = new SwingMappingRenderer(m);
		DefaultQuestionIterator iter = voc.createIter(lections, r, m.getFilter(), m);
		createNabuSession(iter);
	}

	public static void createNabuSession(QuestionIterator iter) {
		JFrame f = new AppFrame("Nabu Session", "nabu.session", new Dimension(500,300));
		NabuSession n = new NabuSession();
		n.setIter(iter);
		f.getContentPane().add(n, BorderLayout.CENTER);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		n.postAdd();

		n.init();
		iter.next();
		n.updateRenderer();

//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		Dimension dialogSize = f.getSize();
//		f.setLocation(
//			(screenSize.width - dialogSize.width) / 2,
//			(screenSize.height - dialogSize.height) / 2);
		f.setVisible(true);
		((SwingRenderer)iter.getRenderer()).activate();
	}

	/**
	 * This method initializes openFolderButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOpenFolderButton() {
		if (openFolderButton == null) {
			openFolderButton = new JButton();
			openFolderButton.setText("Open Folder");
			openFolderButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					openFolder();
				}
			});
		}
		return openFolderButton;
	}

	protected void openFolder() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			File dir = fc.getSelectedFile();
			addedDirs.add(dir);
			getCentral().readDir(dir.getAbsolutePath());
			updateLists();
		}
	}

	public static void main(String[] args) {
		if(args.length == 1 && args[0].equals("-test")){
			test();
		}else{
			Manager m = new Manager();
			m.createFrame();
		}
	}

	private void createFrame() {
		appFrame = new AppFrame("Nabu", "nabu.manager", new Dimension(500,300));
		appFrame.getContentPane().add(this, BorderLayout.CENTER);

		URL iconLoc = getClass().getResource("/nabulogo8.gif");
		getAppFrame().setIconImage(Toolkit.getDefaultToolkit().createImage(iconLoc));
		
		appFrame.setVisible(true);
	}

	private static void test() {
		Central c = new Central();
		try {
			c.digestXML("etc/voc.xml");
			Vocabulary voc = ((Vocabulary) c.getVocs().get("sw"));
			Mode m = (Mode) voc.getModes().get("1");
			List lections = Collections.singletonList(voc.getLections().get(0));
			createNabuSession(voc, m, lections);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JFrame getAppFrame() {
		return appFrame;
	}

	public SwingCentral getCentral() {
		return central;
	}

}
