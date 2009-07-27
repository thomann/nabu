package ch.unizh.ori.nabu.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import ch.unizh.ori.nabu.ui.swing.webservice.NabuWS;
import ch.unizh.ori.nabu.ui.swing.webservice.NabuWSService;
import ch.unizh.ori.nabu.ui.swing.webservice.StringArray;



public class Download extends AppFrame {
	
	private static String NABU_SERVER_URL_SECURE = "https://localhost:8443/nabu";

	private static String NABU_SERVER_URL = "http://localhost:8080/nabu";

	private static final Logger log = Logger.getLogger(Download.class);
	
	private List<String> subs;
	
	private Map<String,String[]> data = new HashMap<String, String[]>();

	private JTree tree;

	private String vocsBase = System.getProperty("user.home")+"/vocs/";

	private static NabuWS nabuWS;

	private final Manager manager;

	public Download(Manager manager) {
		super("Download from NABU", Download.class.toString(), new Dimension(500,300));
		this.manager = manager;
		
		try{
			NabuWS nabuWS = Download.getNabuWebservice();
			subs = nabuWS.getSubs();
		}catch(Exception ex){
			JOptionPane.showMessageDialog(null, "Problem getting connection to server: "+ex.getMessage());
			ex.printStackTrace();
			return;
		}
		
		initComponents();
		setVisible(true);
	}
	
	static{
		try {
			InputStream in = Download.class.getResourceAsStream("/nabu.properties");
			Properties props = new Properties();
			props.load(in);
			in.close();
			
			if(props.getProperty("nabu.server.url")!= null)
				NABU_SERVER_URL = props.getProperty("nabu.server.url");
			if(props.getProperty("nabu.server.url.https")!= null)
				NABU_SERVER_URL_SECURE = props.getProperty("nabu.server.url.https");
		} catch (IOException e) {
			log.error(e);
		}
	}

	public static NabuWS getNabuWebservice() {
		if (nabuWS == null) {
			try {
				nabuWS = new NabuWSService(new URL(getNabuServerUrl() + "/NabuWS?wsdl"), new QName("http://webservices.http.ui.nabu.ori.unizh.ch/", "NabuWSService")).getNabuWSPort();
			} catch (MalformedURLException e) {
				log.error(e);
			}
		}
		return nabuWS;
	}

	/**
	 * Returns the Nabu web server base url string without trailing slash.
	 * In production environment this could be
	 * <code>http://nabu.uzh.ch/nabu</code>
	 * It is configured by the property...
	 * 
	 * @return the Nabu web server base url without trailing slash
	 */
	private static String getNabuServerUrl() {
		return NABU_SERVER_URL;
	}
	
	/**
	 * Returns the Nabu web server base https url without trailing slash.
	 * In production environment this could be
	 * <code>https://nabu.uzh.ch/nabu</code>
	 * 
	 * @return the Nabu web server base https url without trailing slash
	 */
	public static String getNabuServerUrlSecure() {
		return NABU_SERVER_URL_SECURE;
	}
	
	private void initComponents() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		node.add(new SubsNode(null));
		for(String sub : subs)
			node.add(new SubsNode(sub));
		tree = new JTree(node);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		ToolTipManager.sharedInstance().registerComponent(tree);
		tree.setCellRenderer(new DefaultTreeCellRenderer(){
			@Override
			public Component getTreeCellRendererComponent(JTree tree,
					Object value, boolean sel, boolean expanded, boolean leaf,
					int row, boolean hasFocus) {
				
				JComponent treeCellRendererComponent = (JComponent) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
								row, hasFocus);
				setToolTipText(null);
				if (value instanceof DefaultMutableTreeNode) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
					if (node.getUserObject() instanceof List) {
						List<String> list = (List<String>) node.getUserObject();
						if(list.size()>2){
							setToolTipText(list.get(2));
						}
					}
				}
				return treeCellRendererComponent;
			}
		});
		tree.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2)
					doDownloadSelected();
			}
		});
		this.getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		JButton button = new JButton("Download selected");
		button.setMnemonic('d');
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				doDownloadSelected();
			}
		});
		buttonPanel.add(button);
		
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}
	
		
	private void doDownloadSelected() {
		TreePath[] selectionPaths = tree.getSelectionPaths();
		if(selectionPaths == null)
			return;
		try{
			for(TreePath tp : selectionPaths){
				log.trace(tp);
				DefaultMutableTreeNode leaf = (DefaultMutableTreeNode) tp.getLastPathComponent();
				List<String> userObject = (List<String>) leaf.getUserObject();
				log.trace(userObject);
				String vocId = userObject.get(1);
				if(new File(vocsBase+"/"+vocId+".xml").exists()){
					if(JOptionPane.showConfirmDialog(this, "Vocabulary "+userObject.get(0)+" already seems to exist. Overwrite?")
							!= JOptionPane.OK_OPTION)
						continue;
				}
				HttpURLConnection con = null;
				ZipInputStream zin = null;
				try {
					URL url = new URL(NABU_SERVER_URL+"/getJar/Nabu-plain.jar?type=plain&id="+vocId);
					con = (HttpURLConnection) url.openConnection();
					if(true || con.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED){
						zin = new ZipInputStream(new BufferedInputStream(con.getInputStream()));
						ZipEntry z;
						while( (z = zin.getNextEntry()) != null){
							if(z.getName().startsWith("META-INF/") || z.getName().startsWith("/META-INF/") || z.getName().equals("nop")){
								continue;
							}
							copyToFile(zin, vocsBase + z.getName());
						}
						if(manager != null)
							manager.reload();
						setVisible(false);
						dispose();
					}else{
						log.error(con.getResponseMessage());
					}
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
					throw e;
				}finally{
					if(zin != null){
						try {
							zin.close();
						} catch (IOException e) {
							e.printStackTrace();
							throw e;
						}
					}
					if(con != null){
						con.disconnect();
					}
				}
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Problem in Downloading: "+e.getMessage());
		}
	}


	private void copyToFile(InputStream in, String filename) throws IOException {
		byte[] buffer = new byte[1024];
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
		int len;
		while( (len = in.read(buffer)) >= 0 ){
			out.write(buffer, 0, len);
		}
		out.close();
	}
	
	public static class SubsNode implements MutableTreeNode{
		private String name;
		
		private List<StringArray> organization;
		private List<TreeNode> organizationTitles;
		
		public SubsNode(String name){
			this.name = name;
		}
		
		protected List<TreeNode> getOrganizationTitles(){
			if (organizationTitles == null) {
				organizationTitles = new ArrayList<TreeNode>();
				
				NabuWS nabuWS = getNabuWebservice();
				organization = nabuWS.getVocs(name);

				DefaultMutableTreeNode node = null;
				for(StringArray item : organization){
					List<String> s = item.getItem();
					if(s.size()==1){
						// it's a title
						node = new DefaultMutableTreeNode(s.get(0));
						organizationTitles.add(node);
					}else{
						if(node == null){
							node = new DefaultMutableTreeNode(":-)");
							organizationTitles.add(node);
						}
						// add the download url
						s.add("http://localhost:8080/nabu/"+name+"/getJar/nabu.jar?type=plain&id="+s.get(1));
						log.trace(s);
						node.add(new DefaultMutableTreeNode(s){
							@Override
							public String toString() {
								return ((List<String>)getUserObject()).get(0);
							}
						});
					}
				}
				
			}

			return organizationTitles;
		}

		@Override
		public String toString() {
			return (name==null)?"NABU":name;
		}

		public Enumeration children() {
			return Collections.enumeration(getOrganizationTitles());
		}

		public boolean getAllowsChildren() {
			return true;
		}

		public TreeNode getChildAt(int childIndex) {
			return getOrganizationTitles().get(childIndex);
		}

		public int getChildCount() {
			return getOrganizationTitles().size();
		}

		public int getIndex(TreeNode node) {
			return getOrganizationTitles().indexOf(node);
		}

		public TreeNode getParent() {
			return null;
		}

		public boolean isLeaf() {
			return false;
		}

		public void insert(MutableTreeNode child, int index) {
			// TODO Auto-generated method stub
			
		}

		public void remove(int index) {
			// TODO Auto-generated method stub
			
		}

		public void remove(MutableTreeNode node) {
			// TODO Auto-generated method stub
			
		}

		public void removeFromParent() {
			// TODO Auto-generated method stub
			
		}

		public void setParent(MutableTreeNode newParent) {
			// TODO Auto-generated method stub
			
		}

		public void setUserObject(Object object) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static void main(String[] args) {
		new Download(null);
	}


}