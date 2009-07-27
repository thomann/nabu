/*
 * Created on 09.03.2006
 *
 */
package ch.unizh.ori.nabu.ui;

import java.awt.Dimension;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.fop.messaging.MessageHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ch.unizh.ori.common.text.PlotterPresentation;
import ch.unizh.ori.common.text.Presentation;
import ch.unizh.ori.common.text.StringPresentation;
import ch.unizh.ori.nabu.core.ListQuestionProducer;
import ch.unizh.ori.nabu.voc.FieldStream;
import ch.unizh.ori.nabu.voc.Filter;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.StringColumn;
import ch.unizh.ori.nabu.voc.Vocabulary;
import ch.unizh.ori.tuppu.Plottable;

public class VocabularyXMLExporter {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(VocabularyXMLExporter.class);

	private Vocabulary voc;

	private int width = 4, height = 4;

	private String paper = "a4";

	private Filter filter;

	private Mode mode;

	private List modeFields;
	
	private String padding = "0.25cm";

	private List lections = new ArrayList();
	
	private Map idsToLessons = new HashMap();

	public Document getXrtDoc() throws ParserConfigurationException, IOException, FontFormatException {
		try{
		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().newDocument();
		Element root = doc.createElement("vocabulary");
		doc.appendChild(root);

		ListQuestionProducer qp = getProducer();
		
		idsToLessons.clear();
		for (Iterator iter = getLections().iterator(); iter.hasNext();) {
			FieldStream fs = (FieldStream) iter.next();
			idsToLessons.put(fs.getId(), fs);
		}

		modeFields = mode.createModeFields();

		if (!qp.isList())
			throw new IllegalArgumentException("No list: " + qp);

		qp.initSession();
		int count = qp.countQuestions();

		int itemsPerPage = width * height;
		int pages = (int) Math.ceil(count / (float) itemsPerPage);

		root.setAttribute("pages", String.valueOf(pages));
		root.setAttribute("itemsPerPage", String.valueOf(itemsPerPage));
		root.setAttribute("count", String.valueOf(count));
		root.setAttribute("width", String.valueOf(width));
		root.setAttribute("height", String.valueOf(height));
		
		root.setAttribute("td-height", (21/(float)height)+"cm");
		root.setAttribute("padding", padding);
		
		if (paper != null)
			root.setAttribute("paper", paper);

		for (int p = 0; p < pages; p++) {
			Object[] page = new Object[itemsPerPage];
			int i;
			for (i = 0; i < page.length; i++) {
				page[i] = qp.produceNext();
				if (page[i] == null)
					break;
			}
//			Element pageNode = doc.createElement("page");
//			root.appendChild(pageNode);
//			pageNode.setAttribute("page", String.valueOf(p));
//			pageNode.setAttribute("count", String.valueOf(i));

			Element recto = doc.createElement("page");
			root.appendChild(recto);
			recto.setAttribute("recto", "true");
			Element verso = doc.createElement("page");
			root.appendChild(verso);
			verso.setAttribute("verso", "true");
			for (int row = 0; row < height; row++) {
				Element rectoRow = doc.createElement("tr");
				recto.appendChild(rectoRow);
				Element versoRow = doc.createElement("tr");
				verso.appendChild(versoRow);
				for (int col = 0; col < width; col++) {
					int j;
					j = width * row + col;
					rectoRow.appendChild(createQuestionNode(doc, page[j], false));
					j = width * row + width - col - 1;
					versoRow.appendChild(createQuestionNode(doc, page[j], true));
				}
			}
		}

		return doc;
		}catch(Throwable t){
			t.printStackTrace();
			throw new IOException(t.getMessage());
		}
	}

	private ListQuestionProducer getProducer() {
		//		QuestionProducer qp = getVoc().createProducer(lections, filter, mode,
		//				false);
		
				List theVoc = voc.createVocList(lections);
				if(filter != null){
					for (Iterator iter = theVoc.iterator(); iter.hasNext();) {
						Map v = (Map) iter.next();
						if(!filter.accept(v)){
							iter.remove();
						}
					}
				}
				ListQuestionProducer qp = new ListQuestionProducer(theVoc);
		return qp;
	}

	private Node createQuestionNode(Document doc, Object question, boolean verso) throws IOException, ParserConfigurationException, FontFormatException {
		if (question == null) {
			return doc.createElement("empty");
		}
		Map q = (Map) question;
		Element ret = doc.createElement("td");
		ret.setAttribute("voc", voc.getName());
		if (voc.isDecorate()) {
			FieldStream fs = (FieldStream) idsToLessons.get(q.get("lesson"));
			if(fs != null)
				ret.setAttribute("lesson", fs.getName());
			else
				log.warn("Lesson not found: "+q);
		}
		for (Iterator iter = modeFields.iterator(); iter.hasNext();) {
			ModeField mf = (ModeField) iter.next();
			if (verso != mf.isAsking())
				continue;
			Object data = q.get(mf.getKey());
			Presentation presentation = mf.getPresentation();
			boolean complex = presentation != null && presentation.getScript() != null && presentation.getScript().isComplex();
			if (mf.isImage() || complex) {
				if(complex){
					presentation = presentation.getScript().getPresentation("pretty");
					if(presentation == null)
						presentation = mf.getPresentation();
				}
				Element image = doc.createElement("image");
				ret.appendChild(image);
				image.setAttribute("text", data.toString());
				if (presentation instanceof PlotterPresentation) {
					PlotterPresentation pp = (PlotterPresentation) presentation;
					String transliteration = ((StringColumn)mf.getColumn()).getTransliteration();
					Object converted = pp.getScript().convert(data.toString(), transliteration, pp.getOutTransliteration());
					Node n = createSVG(doc, pp, converted.toString());
					image.appendChild(n);
				}
			} else {
				Element text = doc.createElement("text");
				ret.appendChild(text);
				if (presentation instanceof StringPresentation) {
					StringPresentation sp = (StringPresentation) presentation;
					if(sp.getFont()!=null)
						text.setAttribute("font", sp.getFont());
					String transliteration = ((StringColumn)mf.getColumn()).getTransliteration();
					String outText = sp.getOutText(data, transliteration);
					text.setAttribute("text", outText);
				}else{
					text.setAttribute("text", data.toString());
				}
			}
		}
		return ret;
	}
	
	public static Node createSVG(Document document, PlotterPresentation pp, String text) throws IOException, ParserConfigurationException, FontFormatException {
        // Get a DOMImplementation
//        DOMImplementation domImpl =
//            GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document
		if (document == null) {
			document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
		}
//        Document document = domImpl.createDocument(null, "svg", null);

        // Create an instance of the SVG Generator
		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
		ctx.setEmbeddedFontsOn(true);
        SVGGraphics2D svgGenerator = new SVGGraphics2D(ctx, true);

        // Ask the test to render into the SVG Graphics2D implementation
        Plottable plottable = pp.getPlotter().createPlottable(text, new HashMap());
		plottable.plot(svgGenerator);

        // Finally, stream out SVG to the standard output using UTF-8
        // character to byte encoding
//        boolean useCSS = true; // we want to use CSS style attribute
//        Writer out = new OutputStreamWriter(System.out, "UTF-8");
//        svgGenerator.stream(out, useCSS);
        Element ret = svgGenerator.getRoot();
        Dimension size = plottable.getSize();
		ret.setAttribute("width", size.getWidth()+"");
		ret.setAttribute("height", size.getHeight()+"");
		return ret;
	}

	public static void loadHyphenation(String dir) {
		org.apache.fop.configuration.Configuration.put("hyphenation-dir", dir);
	}

	// This method writes a DOM document to a file
	public static void writeXmlFile(Document doc, String filename)
			throws TransformerException {
		// Prepare the DOM document for writing
		Source source = new DOMSource(doc);	

		// Prepare the output file
		File file = new File(filename);
		Result result = new StreamResult(file.toURI().getPath());

		// Write the DOM document to the file
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(source, result);
	}

	// This method applies the xslFilename to inFilename and writes
	// the output to outFilename.
	public static void xsl(Node in, String outFilename, String xslFilename)
			throws FileNotFoundException {
		xsl(in, xslFilename,
				new StreamResult(new FileOutputStream(outFilename)));
	}

	public static void xsl(Node in, String xslFilename, Result result) {
		try {
			// Create transformer factory
			TransformerFactory factory = TransformerFactory.newInstance();

			// Use the factory to create a template containing the xsl file
			Templates template = factory.newTemplates(new StreamSource(
					new File(xslFilename)));

			// Use the template to create a transformer
			Transformer xformer = template.newTransformer();

			// Prepare the input and output files
			Source source = new DOMSource(in);

			// Apply the xsl file to the source file and write the result to the
			// output file
			xformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			// An error occurred while applying the XSL file
			// Get location of error in input file
//			SourceLocator locator = e.getLocator();
//			int col = locator.getColumnNumber();
//			int line = locator.getLineNumber();
//			String publicId = locator.getPublicId();
//			String systemId = locator.getSystemId();
			e.printStackTrace();
		}
	}

	public static void convertXML2PDF(Node xml, String xsl, OutputStream pdf)
			throws IOException, FOPException, TransformerException {
		// Construct driver
		Driver driver = new Driver();

		// Setup logger
		Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
		driver.setLogger(logger);
		MessageHandler.setScreenLogger(logger);

		// Setup Renderer (output format)
		driver.setRenderer(Driver.RENDER_PDF);

		// Setup output
		OutputStream out = pdf;
		try {
			driver.setOutputStream(out);

			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(
					xsl));

			// Setup input for XSLT transformation
			Source src = new DOMSource(xml);

			// Resulting SAX events (the generated FO) must be piped through to
			// FOP
			Result res = new SAXResult(driver.getContentHandler());

			// Start XSLT transformation and FOP processing
			transformer.transform(src, res);
		} catch (TransformerException e) {
			// An error occurred while applying the XSL file
			// Get location of error in input file
			SourceLocator locator = e.getLocator();
			if (locator != null) {
				int col = locator.getColumnNumber();
				int line = locator.getLineNumber();
				String publicId = locator.getPublicId();
				String systemId = locator.getSystemId();
				log.error(col + "/" + line, e);
			}
		} finally {
			out.close();
		}
	}

	public Vocabulary getVoc() {
		return voc;
	}

	public void setVoc(Vocabulary voc) {
		this.voc = voc;
		setLections(new ArrayList(voc.getLections()));
		setMode((Mode) voc.getModes().values().iterator().next());
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public List getLections() {
		return lections;
	}

	public void setLections(List lections) {
		this.lections = lections;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		setFilter(mode.getFilter());
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getPadding() {
		return padding;
	}

	public void setPadding(String padding) {
		this.padding = padding;
	}

}
