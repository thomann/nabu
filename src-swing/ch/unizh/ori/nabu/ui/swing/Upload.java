package ch.unizh.ori.nabu.ui.swing;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import java.awt.BorderLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.*;

import ch.unizh.ori.nabu.ui.swing.webservice.NabuWS;
import ch.unizh.ori.nabu.ui.swing.webservice.NabuWSService;
import ch.unizh.ori.nabu.voc.Vocabulary;

public class Upload extends AppFrame {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(Upload.class);

	private List<String> subs = Collections.emptyList();
	
	private Vocabulary voc;
	
	private JList list;

	private JTextField usernameTF;

	private JTextField passwordTF;

	private static final String PREF_LAST_USER_NAME = "last.user.name";
	private static final String PREF_LAST_PASSWORD = "last.password";

	public Upload(Vocabulary voc) {
		super("Upload " + voc.getName(), Upload.class.getName(), new Dimension(
				500, 500));
		
		this.voc = voc;

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

	private void initComponents() {
		list = new JList(new Vector<String>(subs));
		getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
		
		JPanel p = new JPanel();
		p.add(new JLabel("UniAccess Username:"));
		usernameTF = new JTextField(10);
		Preferences prefs = Preferences.userNodeForPackage(Upload.class);
		if(prefs.get(PREF_LAST_USER_NAME, null)!=null){
			usernameTF.setText(prefs.get(PREF_LAST_USER_NAME, null));
		}
		p.add(usernameTF);
		p.add(new JLabel("Password:"));
		passwordTF = new JPasswordField(10);
		if(prefs.get(PREF_LAST_PASSWORD, null)!=null){
			passwordTF.setText(prefs.get(PREF_LAST_PASSWORD, null));
		}
		p.add(passwordTF);
		getContentPane().add(p, BorderLayout.NORTH);
		
		p = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		AbstractAction cancelAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
			
		};
		cancelAction.putValue(Action.NAME, "cancel");
		cancelButton.setAction(cancelAction);
		p.add(cancelButton);
		
		JButton uploadButton = new JButton("Upload");
		AbstractAction okAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				if(doUpload())
					return;
				setVisible(false);
				dispose();
			}
			
		};
		okAction.putValue(Action.NAME, "Upload");
		uploadButton.setAction(okAction);
		p.add(uploadButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		KeyStroke escKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		
		getRootPane().registerKeyboardAction(cancelAction, escKeyStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		getRootPane().registerKeyboardAction(okAction, enterKeyStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		
	}

	protected boolean doUpload() {
		PostMethod filePost = null;
		try {
			String subPath = (String) list.getSelectedValue();
			if (subPath == null)
				subPath = "";
			else
				subPath += "/";
			String host = "localhost";
			int port = 8443;
			String targetURL = Download.getNabuServerUrlSecure() + "/admin/"
					+ subPath + "makeVoc.jsp";

			File base = new File(voc.getBase().getFile()).getParentFile();
			File dataFile = new File(base, voc.getSrc().getSrc());

			Protocol easyhttps = new Protocol("https",
					(ProtocolSocketFactory) new EasySSLProtocolSocketFactory(),
					443);
			Protocol.registerProtocol("https", easyhttps);

			filePost = new PostMethod(targetURL);
			filePost.getParams().setBooleanParameter(
					HttpMethodParams.USE_EXPECT_CONTINUE, true);

			log.debug("Uploading " + dataFile.getName() + " to " + targetURL);

			Properties vars2 = new Properties();
			File propertiesFile = new File(base, voc.getPropertiesFilename());
			vars2.load(new BufferedInputStream(new FileInputStream(
					propertiesFile)));
			Map<String, String> vars = new HashMap<String, String>((Map) vars2);

			Part[] parts = new Part[1 + vars.keySet().size()];
			parts[0] = new FilePart(dataFile.getName(), dataFile);
			int i = 1;
			for (String key : vars.keySet()) {
				parts[i++] = new StringPart((String) key, vars.get(key));
			}

			filePost.setRequestEntity(new MultipartRequestEntity(parts,
					filePost.getParams()));

			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					5000);

			client.getParams().setAuthenticationPreemptive(true);
			String username = usernameTF.getText();
			String password = passwordTF.getText();
			Credentials defaultcreds = new UsernamePasswordCredentials(
					username, password);
			client.getState().setCredentials(
					new AuthScope(host, port, AuthScope.ANY_REALM),
					defaultcreds);

			int status = client.executeMethod(filePost);

			if (status == HttpStatus.SC_OK) {
				log.info("Upload complete, response="
						+ filePost.getResponseBodyAsString());
				Preferences prefs = Preferences
						.userNodeForPackage(Upload.class);
				boolean newUsername = !username.equals(prefs.get(PREF_LAST_USER_NAME, null));
				if (newUsername) {
					prefs.put(PREF_LAST_USER_NAME, username);
				}
				if (!password.equals(prefs.get(PREF_LAST_PASSWORD, null))) {
					if (JOptionPane
							.showConfirmDialog(this,
									"Do you want to save this password? It will be stored in cleartext.", "Save Password", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						prefs.put(PREF_LAST_PASSWORD, password);
					}else if(newUsername){
						prefs.put(PREF_LAST_PASSWORD, null);
					}
				}
			} else {
				String msg = "Upload failed: "
						+ HttpStatus.getStatusText(status);
				log.warn(msg);
				JOptionPane.showMessageDialog(this, msg);
			}
		} catch (Exception ex) {
			log.debug(ex.getMessage(), ex);
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Problem in uploading: "
					+ ex.getMessage());
		} finally {
			if(filePost != null)
				filePost.releaseConnection();
		}

		return false;
	}
	
}

/**
 * <p>
 * EasySSLProtocolSocketFactory can be used to creats SSL {@link Socket}s 
 * that accept self-signed certificates. 
 * </p>
 * <p>
 * This socket factory SHOULD NOT be used for productive systems 
 * due to security reasons, unless it is a concious decision and 
 * you are perfectly aware of security implications of accepting 
 * self-signed certificates
 * </p>
 *
 * <p>
 * Example of using custom protocol socket factory for a specific host:
 *     <pre>
 *     Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
 *
 *     URI uri = new URI("https://localhost/", true);
 *     // use relative url only
 *     GetMethod httpget = new GetMethod(uri.getPathQuery());
 *     HostConfiguration hc = new HostConfiguration();
 *     hc.setHost(uri.getHost(), uri.getPort(), easyhttps);
 *     HttpClient client = new HttpClient();
 *     client.executeMethod(hc, httpget);
 *     </pre>
 * </p>
 * <p>
 * Example of using custom protocol socket factory per default instead of the standard one:
 *     <pre>
 *     Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
 *     Protocol.registerProtocol("https", easyhttps);
 *
 *     HttpClient client = new HttpClient();
 *     GetMethod httpget = new GetMethod("https://localhost/");
 *     client.executeMethod(httpget);
 *     </pre>
 * </p>
 * 
 * @author <a href="mailto:oleg -at- ural.ru">Oleg Kalnichevski</a>
 * 
 * <p>
 * DISCLAIMER: HttpClient developers DO NOT actively support this component.
 * The component is provided as a reference material, which may be inappropriate
 * for use without additional customization.
 * </p>
 */

class EasySSLProtocolSocketFactory implements SecureProtocolSocketFactory {

    /** Log object for this class. */
    private static final Log LOG = LogFactory.getLog(EasySSLProtocolSocketFactory.class);

    private SSLContext sslcontext = null;

    /**
     * Constructor for EasySSLProtocolSocketFactory.
     */
    public EasySSLProtocolSocketFactory() {
        super();
    }

    private static SSLContext createEasySSLContext() {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(
              null, 
              new TrustManager[] {new EasyX509TrustManager(null)}, 
              null);
            return context;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new HttpClientError(e.toString());
        }
    }

    private SSLContext getSSLContext() {
        if (this.sslcontext == null) {
            this.sslcontext = createEasySSLContext();
        }
        return this.sslcontext;
    }

    /**
     * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int,java.net.InetAddress,int)
     */
    public Socket createSocket(
        String host,
        int port,
        InetAddress clientHost,
        int clientPort)
        throws IOException, UnknownHostException {

        return getSSLContext().getSocketFactory().createSocket(
            host,
            port,
            clientHost,
            clientPort
        );
    }

    /**
     * Attempts to get a new socket connection to the given host within the given time limit.
     * <p>
     * To circumvent the limitations of older JREs that do not support connect timeout a 
     * controller thread is executed. The controller thread attempts to create a new socket 
     * within the given limit of time. If socket constructor does not return until the 
     * timeout expires, the controller terminates and throws an {@link ConnectTimeoutException}
     * </p>
     *  
     * @param host the host name/IP
     * @param port the port on the host
     * @param clientHost the local host name/IP to bind the socket to
     * @param clientPort the port on the local machine
     * @param params {@link HttpConnectionParams Http connection parameters}
     * 
     * @return Socket a new socket
     * 
     * @throws IOException if an I/O error occurs while creating the socket
     * @throws UnknownHostException if the IP address of the host cannot be
     * determined
     */
    public Socket createSocket(
        final String host,
        final int port,
        final InetAddress localAddress,
        final int localPort,
        final HttpConnectionParams params
    ) throws IOException, UnknownHostException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        SocketFactory socketfactory = getSSLContext().getSocketFactory();
        if (timeout == 0) {
            return socketfactory.createSocket(host, port, localAddress, localPort);
        } else {
            Socket socket = socketfactory.createSocket();
            SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
            SocketAddress remoteaddr = new InetSocketAddress(host, port);
            socket.bind(localaddr);
            socket.connect(remoteaddr, timeout);
            return socket;
        }
    }

    /**
     * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int)
     */
    public Socket createSocket(String host, int port)
        throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(
            host,
            port
        );
    }

    /**
     * @see SecureProtocolSocketFactory#createSocket(java.net.Socket,java.lang.String,int,boolean)
     */
    public Socket createSocket(
        Socket socket,
        String host,
        int port,
        boolean autoClose)
        throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(
            socket,
            host,
            port,
            autoClose
        );
    }

    public boolean equals(Object obj) {
        return ((obj != null) && obj.getClass().equals(EasySSLProtocolSocketFactory.class));
    }

    public int hashCode() {
        return EasySSLProtocolSocketFactory.class.hashCode();
    }

}

class EasyX509TrustManager implements X509TrustManager
{
    private X509TrustManager standardTrustManager = null;

    /** Log object for this class. */
    private static final Log LOG = LogFactory.getLog(EasyX509TrustManager.class);

    /**
     * Constructor for EasyX509TrustManager.
     */
    public EasyX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
        super();
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keystore);
        TrustManager[] trustmanagers = factory.getTrustManagers();
        if (trustmanagers.length == 0) {
            throw new NoSuchAlgorithmException("no trust manager found");
        }
        this.standardTrustManager = (X509TrustManager)trustmanagers[0];
    }

    /**
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[],String authType)
     */
    public void checkClientTrusted(X509Certificate[] certificates,String authType) throws CertificateException {
        standardTrustManager.checkClientTrusted(certificates,authType);
    }

    /**
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[],String authType)
     */
    public void checkServerTrusted(X509Certificate[] certificates,String authType) throws CertificateException {
        if ((certificates != null) && LOG.isDebugEnabled()) {
            LOG.debug("Server certificate chain:");
            for (int i = 0; i < certificates.length; i++) {
                LOG.debug("X509Certificate[" + i + "]=" + certificates[i]);
            }
        }
        if ((certificates != null) && (certificates.length == 1)) {
//            certificates[0].checkValidity();
        } else {
            standardTrustManager.checkServerTrusted(certificates,authType);
        }
    }

    /**
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    public X509Certificate[] getAcceptedIssuers() {
        return this.standardTrustManager.getAcceptedIssuers();
    }
}