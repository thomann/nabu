/*
 * ImageServlet.java
 *
 * Created on 21. September 2002, 11:14
 */

package ch.unizh.ori.nabu.ui.http.tuppu;

import javax.servlet.*;
import javax.servlet.http.*;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.IOException;
import javax.imageio.*;

/**
 *
 * @author  pht
 * @version
 */
public abstract class ImageServlet extends HttpServlet {

	private BufferedImage defaultImg;

	private Graphics2D defaultGraphics;

	/** Holds value of property insets. */
	//private Insets insets = new Insets(0,0,0,0);
	private Insets insets, defaultInsets = new Insets(2, 2, 2, 2);
	private Dimension size, defaultSize;
	private Color background, defaultBackground;
	private Color foreground, defaultForeground;
	
	private Font font;
	
	private boolean drawFrame, defaultDrawFrame;

	/** Initializes the servlet.
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		defaultImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		defaultGraphics = defaultImg.createGraphics();
		
		defaultBackground = Color.decode(config.getInitParameter("background"));
		defaultForeground = Color.decode(config.getInitParameter("foreground"));
		defaultDrawFrame = new Boolean(config.getInitParameter("drawFrame")).booleanValue();
	}

	protected void sendPlot(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean png = request.getHeader("Accept").indexOf("image/png") >= 0;
		response.setContentType((png)?"image/png":"image/jpeg");
		
		Rectangle2D bounds = getBounds(request, response);
		int width = (int) Math.ceil(bounds.getWidth()) + getInsets().left
				+ getInsets().right;
		int height = (int) Math.ceil(bounds.getHeight()) + getInsets().top
				+ getInsets().bottom;

		BufferedImage img = new BufferedImage(Math.max(width, 1), Math.max(
				height, 1), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();

		g2.setColor(getBackground(request, response));
		g2.fillRect(0, 0, width, height);

		g2.setColor(Color.lightGray);
		g2.drawRect(0, 0, width - 1, height - 1);
		g2.translate(getInsets().left, getInsets().top);

		this.plot(g2, request, response);

		g2.dispose();

		ImageIO.write(img, (png)?"png":"jpeg", response.getOutputStream());
	}

	public abstract void plot(Graphics2D g, HttpServletRequest request,
			HttpServletResponse response);

	//public abstract int getHeight(HttpServletRequest request, HttpServletResponse response);
	//public abstract int getWidth(HttpServletRequest request, HttpServletResponse response);
	public abstract Rectangle2D getBounds(HttpServletRequest request,
			HttpServletResponse response);

	public Color getBackground(HttpServletRequest request,
			HttpServletResponse response) {
		return Color.white;
	}

	public Font getFont(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	/** Destroys the servlet.
	 */
	public void destroy() {
		defaultGraphics.dispose();
		defaultGraphics = null;
		defaultImg = null;
	}

	/** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			java.io.IOException {
		sendPlot(request, response);
	}

	/** Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			java.io.IOException {
		processRequest(request, response);
	}

	/** Handles the HTTP <code>POST</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			java.io.IOException {
		processRequest(request, response);
	}

	/** Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "A plotting servlet";
	}

	protected Graphics2D getDefaultGraphics() {
		return defaultGraphics;
	}

	/** Getter for property insets.
	 * @return Value of property insets.
	 */
	public Insets getInsets() {
		return this.insets;
	}

	/** Setter for property insets.
	 * @param insets New value of property insets.
	 */
	public void setInsets(Insets insets) {
		this.insets = insets;
		if (insets == null) {
			this.insets = new Insets(0, 0, 0, 0);
		}
	}

	public Color getBackground() {
		return background;
	}
	public void setBackground(Color background) {
		this.background = background;
	}
	public boolean isDrawFrame() {
		return drawFrame;
	}
	public void setDrawFrame(boolean drawFrame) {
		this.drawFrame = drawFrame;
	}
	public Color getForeground() {
		return foreground;
	}
	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}
	public Dimension getSize() {
		return size;
	}
	public void setSize(Dimension size) {
		this.size = size;
	}
}