package ch.unizh.ori.cruciverbalismus;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class ConfigBean {
	public static final int DEFAULT_SIZE = 6;
	public static final double DEFAULT_CROSSINGS = 0.3;
	public static final double DEFAULT_BLOCKS = 0.3;
	public static final double DEFAULT_UNEQUITY = 0.3;
	public static final int DEFAULT_ITERATIONS = 10000;
	public static final String DEFAULT_STYLE_HTML = "<style type=\"text/css\" media=\"screen\">\n"+
	"#crossword{ font-size:24px; text-align:center}\n"+
	"input{border-width: 0px; font-size:36px; text-align:center; margin:10%}\n"+
	"sup{ font-size:24px; }"+
	"#clues{font-size: 24px; border-width:thick; border-color: black}\n"+
	"#solution{font-size: 24px; text-align:center}\n"+
	"</style>";
	public static final String DEFAULT_REPLACE_PATTERNS = "";
	public static final String DEFAULT_DELETE_PATTERNS = " .?!,()";
	private int size;
	private int oldSize;
	private double crossings;
	private double blocks;
	private double unequity;
	private int iterations;
	private boolean rtol;
	private boolean xcols;
	private String lexicon;
	private String title;
	private String crosswordHTML;
	private String solutionHTML;
	private String deletePatterns;
	private String replacePatterns;
	public ConfigBean(){
		this.size = DEFAULT_SIZE;
		this.oldSize=-1;
		this.crossings = DEFAULT_CROSSINGS;
		this.blocks = DEFAULT_BLOCKS;
		this.unequity = DEFAULT_UNEQUITY;
		this.iterations = DEFAULT_ITERATIONS;
		this.rtol = false;
		this.lexicon = "";
		this.crosswordHTML = "";
		this.solutionHTML = "";
	}
	public String getSize() {
		return Integer.toString(size);
	}
	public void setSize(String s) {
		int sInt = Integer.parseInt(s);
		if(sInt>0){
			this.size = sInt;
		}else{
			this.size = DEFAULT_SIZE;
		}
	}
	public String getCrossings() {
		return Double.toString(crossings);
	}
	public void setCrossings(String c) {
		Double sDbl = Double.parseDouble(c);
		if(sDbl>0){
			this.crossings = sDbl;
		}else{
			this.crossings = DEFAULT_CROSSINGS;
		}
	}
	public String getBlocks() {
		return Double.toString(blocks);
	}
	public void setBlocks(String b) {
		Double bDbl = Double.parseDouble(b);
		if(bDbl>0){
			this.blocks = bDbl;
		}else{
			this.blocks = DEFAULT_BLOCKS;
		}
	}
	public String getUnequity() {
		return Double.toString(unequity);
	}
	public void setUnequity(String u) {
		this.unequity = Double.parseDouble(u);
	}
	public String getIterations() {
		return Integer.toString(iterations);
	}
	public void setIterations(String i) {
		this.iterations = Integer.parseInt(i);
	}
//	public String isRtol() {
//			return Boolean.toString(rtol);
//	}
//	public void setRtol(String r) {
//			this.rtol = Boolean.parseBoolean(r);
//	}
	
	public boolean isXcols() {
		return xcols;
	}
	public void setXcols(boolean xcols) {
		this.xcols = xcols;
	}
	public String getLexicon() {
		return lexicon;
	}
	public void setLexicon(String l) {
		lexicon = l;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCrosswordHTML() {
		//buildCrosswordHTML();
		return crosswordHTML;
	}
	public String getNewCrosswordHTML() {
		buildCrosswordHTML();
		return crosswordHTML;
	}
	public void setCrosswordHTML(String crosswordHTML) {
		this.crosswordHTML = crosswordHTML;
	}
	public String getSolutionHTML() {
		return solutionHTML;
	}
	public void setSolutionHTML(String solutionHTML) {
		this.solutionHTML = solutionHTML;
	}

	public boolean buildCrosswordHTML(){
		int lexLn = this.lexicon.length();
		if(lexLn==0){
			return false;
		}
		
		int actualSize = this.size;
		int actualSizeSqr = actualSize*actualSize;
		double actualCrossing = this.crossings;
		double actualBlocks = this.blocks;
		double actualUnequity = this.unequity;
		int actualIterations = this.iterations;
		StringBuffer strBuff = new StringBuffer();
		String lexString = this.lexicon;
		CrosswordLexicon lexicon = new CrosswordLexicon(actualSize);
		if(this.replacePatterns==null){
			replacePatterns = DEFAULT_REPLACE_PATTERNS;
		}
		String replaceStr = this.replacePatterns;
		if(this.deletePatterns==null){
			deletePatterns = DEFAULT_DELETE_PATTERNS;
		}
		String deleteStr = this.deletePatterns;
		lexicon.buildLexicon(lexString, replaceStr, deleteStr, xcols);
		CrosswordGenerator generator = new CrosswordGenerator(lexicon);
		boolean qualityTest = false;
		int crosses =0;
		double crossRate=0.0;
		int blockNum=0;
		int lnNum=0;
		int colNum=0;
		for(int i=0; i<actualIterations && !qualityTest; i++){
			boolean test = generator.createCrossword(actualSize);
			blockNum = generator.getNumberOfBlocks();
			lnNum = generator.getNumberOfLineWords();
			colNum = generator.getNumberOfColumnWords();			
			double lnColDiffRate = Math.abs(lnNum-colNum)/(1.0*actualSize);
			qualityTest = (blockNum/actualSize < actualBlocks);
			qualityTest = qualityTest && (lnColDiffRate < actualUnequity);
			if(qualityTest){
				crosses = generator.countNumberOfCrosses();
				crossRate = crosses/(0.0+actualSizeSqr-blockNum);
				if(crossRate<actualCrossing){
					qualityTest=false;
				}
			}
		}
		if(!qualityTest){
			boolean test = generator.createCrossword(actualSize);
			blockNum = generator.getNumberOfBlocks();
			lnNum = generator.getNumberOfLineWords();
			colNum = generator.getNumberOfColumnWords();			
			double lnColDiffRate = Math.abs(lnNum-colNum)/(1.0*actualSize);
			qualityTest = (blockNum/actualSize < actualBlocks);
			qualityTest = qualityTest && (lnColDiffRate < actualUnequity);
			if(qualityTest){
				crosses = generator.countNumberOfCrosses();
				crossRate = crosses/(0.0+actualSizeSqr-blockNum);
				if(crossRate<actualCrossing){
					qualityTest=false;
				}
			}

		}
		generator.generateGridLabels();
		int [][] labelMap = generator.getGridLabels();
		
		Vector colPosV = generator.getColumnEntryPos();
		int colPosSz = colPosV.size();
		Vector lnPosV = generator.getLineEntryPos();
		int lnPosSz = lnPosV.size();
		Crossword grid = generator.getGrid();
		StringBuffer strBuff2 = new StringBuffer();
		strBuff2.append("Number of crosses: "+Integer.toString(crosses)+"("+Double.toString( crossRate)+")\n");
		Vector lineWords = generator.getLineEntries();
		int [] lnMap = generator.getLineLabelMap();
		int [] colMap = generator.getColumnLabelMap();
		int lineSize = lineWords.size();
		Vector columnWords = generator.getColumnEntries();
		int colSize = columnWords.size();
		int maxLbNum = lineSize+colSize;
		strBuff2.append("Horizontal:\n");
		for(int i=1; i<=maxLbNum; i++){
			for(int ii=0; ii<lineSize; ii++){
				if(lnMap[ii]==i){
					CrosswordLexiconEntry e =(CrosswordLexiconEntry) lineWords.get(ii);
					int labelNum = lnMap[ii];
					strBuff2.append(Integer.toString(labelNum)+"\t"+e.getClue()+"\n");
				}
			}
		}		
		strBuff2.append("\nVertical:\n");
		for(int i=1; i<=maxLbNum; i++){
			for(int ii=0; ii<colSize; ii++){
				if(colMap[ii]==i){
					CrosswordLexiconEntry e =(CrosswordLexiconEntry) columnWords.get(ii);
					int labelNum = colMap[ii];
					strBuff2.append(Integer.toString(labelNum)+"\t"+e.getClue()+"\n");
				}
			}
		}
		/*
		strBuff.append("<style type=\"text/css\" media=\"screen\">\n");
		strBuff.append("#crossword{ font-size:24px; text-align:center}\n");
		strBuff.append("input{border-width: 0px; font-size:36px; text-align:center; margin:10%}\n");
		strBuff.append("sup{ font-size:24px; }");
		strBuff.append("#clues{font-size: 24px; border-width:thick; border-color: black}\n");
		strBuff.append("#solution{font-size: 24px; text-align:center}\n");
		strBuff.append("</style>");
		*/	
		strBuff.append("<table><tr valign=\"top\"><td>");		
		strBuff.append(generator.generateGridHTML(this.rtol));		
		strBuff.append("</td><td>"); 		
		strBuff.append(generator.generateCluesHTML(this.rtol));		
		strBuff.append("</td></tr></table>");		
		this.crosswordHTML = strBuff.toString();		
		this.solutionHTML = generator.generateSolutionHTML(this.rtol);		
		//strBuff.append("</body></html>");		
		return true;
	}
	public static String getDEFAULT_STYLE_HTML() {
		return DEFAULT_STYLE_HTML;
	}
	public String getReplacePatterns() {
		return replacePatterns;
	}
	public void setReplacePatterns(String replacePatterns) {
		this.replacePatterns = replacePatterns;
	}
	public String getDeletePatterns() {
		return deletePatterns;
	}
	public void setDeletePatterns(String deletePatterns) {
		this.deletePatterns = deletePatterns;
	}
	public boolean isRtol() {
		return rtol;
	}
	public void setRtol(boolean rtol) {
		this.rtol = rtol;
	}
}

