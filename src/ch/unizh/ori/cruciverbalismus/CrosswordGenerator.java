package ch.unizh.ori.cruciverbalismus;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class CrosswordGenerator {
	CrosswordLexicon lexicon;
	Set usedWords;
	int size;
	Crossword grid;
	int [] finishedLineField;
	int [] finishedColumnField;
	int [] [] gridLabels;
	int [] lineLabelMap;
	int [] columnLabelMap;
	Ran1 rand;
	int seed;
	Vector lineEntries;
	Vector lineEntryPos;
	Vector columnEntries;
	Vector columnEntryPos;
	int numberOfBlocks=0;
	int numberOfLineWords=0;
	int numberOfColumnWords=0;
	int numberOfCrosses=0;
	
	public static final int DEFAULT_SIZE = 6;
	public static final int DEFAULT_HTML_GRID_SIZE = 600;
	public static final int DEFAULT_HTML_SOLUTION_GRID_SIZE = 300;
	public static final String DEFAULT_HTML_CROSSWORD_STYLE = "crossword";
	public static final String DEFAULT_HTML_SOLUTION_STYLE = "solution";
	public static final String DEFAULT_HTML_CLUES_STYLE = "clues";
	
	
	public Vector getLineEntries(){
		return lineEntries;
	}
	public Vector getLineEntryPos(){
		return lineEntryPos;
	}
	public Vector getColumnEntries(){
		return columnEntries;
	}
	public Vector getColumnEntryPos(){
		return columnEntryPos;
	}
	public int getNumberOfBlocks(){
		return numberOfBlocks;
	}
	public int getNumberOfLineWords(){
		return numberOfLineWords;
	}
	public int getNumberOfColumnWords(){
		return numberOfColumnWords;
	}
	public int getRandomPos(){
		double r = rand.rand();
		int rl = (int) Math.floor(r*(size-1));
		return rl;
	}
	public int getRandomInt(int max){
		double r = rand.rand();
		int rl = (int) Math.floor(r*(max));
		return rl;
	}
	public void setSeed(int s){
		seed = s;
		rand.seed(seed);
	}
	public int getSeed(){
		return seed;
	}
	public CrosswordGenerator(CrosswordLexicon lex){
		lexicon=lex;
		rand = new Ran1();
		seed = (int) System.currentTimeMillis();
		if(seed<0){
			seed=-seed;
		}
		rand.seed(seed);
		size = DEFAULT_SIZE;
	}
	public boolean createCrossword(int s){
		lineLabelMap = null;
		columnLabelMap = null;
		gridLabels = null; 
		size = s;
		lineEntries = new Vector();
		lineEntryPos = new Vector();
		columnEntries = new Vector();
		columnEntryPos = new Vector();
		finishedLineField = new int [size];
		finishedColumnField = new int [size];
		grid=new Crossword(s);
		usedWords = new HashSet();
		int startLine = getRandomPos();
		int actualLine = startLine;
		int startColumn = getRandomPos();
		int actualColumn = startColumn;
		int steps = (int)Math.floor(size/2);
		/*
		for(int i=0; i<steps; i++){
			findInitialLineWord(actualLine);
			findInitialColumnWord(actualColumn);
			actualLine+=2;
			if(actualLine>=size){
				actualLine-=size;
			}
			actualColumn+=2;
			if(actualColumn>=size){
				actualColumn-=size;
			}
		}
		*/
		
		boolean found = false;
		found = findInitialLineWord(actualLine);
		if(found){
			found = false;
			for(int i=0; i<steps && !found; i++){
				found = findInitialColumnWord(actualColumn);
				actualColumn++;
				if(actualColumn>=size){
					actualColumn-=size;
				}
			}
		}
		
		startLine = getRandomPos();
		actualLine = startLine;
		startColumn = getRandomPos();
		actualColumn = startColumn;
		
		for(int i=0; i<size; i++){
			findColumnWord(actualColumn);
			findLineWord(actualLine);
			actualLine++;
			if(actualLine>=size){
				actualLine-=size;
			}
			actualColumn++;
			if(actualColumn>=size){
				actualColumn-=size;
			}
		}
		numberOfBlocks=0;
		char [][] lines = grid.getLines();
		for(int i=0; i<size; i++){	
			for(int j=0; j<size; j++){
				if(lines[i][j]==Crossword.BLOCK_CHAR)
					numberOfBlocks++;
			}
		}
		numberOfLineWords = lineEntries.size();
		numberOfColumnWords = columnEntries.size();
		return true;
	}
	public boolean findInitialLineWord(int line){
		char [] crossLine = grid.getLine(line);
		int lexMaxSize = lexicon.getMaxSize();
		if(size<lexMaxSize){
			Vector lVect = lexicon.getEntries(size, crossLine, usedWords);
			if(lVect==null){
				return false;
			}
			int vSz = lVect.size();
			if(vSz==0){
				return false;
			}
			int choice = getRandomInt(vSz);
			CrosswordLexiconEntry entry = (CrosswordLexiconEntry) lVect.elementAt(choice);
			usedWords.add(entry);
			grid.insertWordInLine(entry.getLemma(), line, 0);
			lineEntries.add(entry);
			finishedLineField[line]=size-1;
			int [] pos = new int[2];
			pos[0]=line;
			pos[1]=0;
			lineEntryPos.add(pos);
			return true;
		}else{	
			Vector lVect = lexicon.getEntries(lexMaxSize, crossLine, usedWords);
			if(lVect==null){
				return false;
			}
			int vSz = lVect.size();
			if(vSz==0){
				return false;
			}
			int choice = getRandomInt(vSz);
			CrosswordLexiconEntry entry = (CrosswordLexiconEntry) lVect.elementAt(choice);
			usedWords.add(entry);
			grid.insertWordInLine(entry.getLemma(), line, 0);
			lineEntries.add(entry);
			finishedLineField[line]=lexMaxSize;
			grid.insertBlock(line,lexMaxSize);
			int [] pos = new int[2];
			pos[0]=line;
			pos[1]=0;
			lineEntryPos.add(pos);
			return true;
		}
	}
	public boolean findInitialColumnWord(int column){
		char [] crossColumn = grid.getColumn(column);
		Vector cVect = lexicon.getEntries(size, crossColumn, usedWords);
		if(cVect==null){
			return false; 
		}
		int cSz = cVect.size();
		if(cSz==0){
			return false;
		}
		int choice = getRandomInt(cSz);
		CrosswordLexiconEntry entry = (CrosswordLexiconEntry) cVect.elementAt(choice);
		usedWords.add(entry);
		grid.insertWordInColumn(entry.getLemma(), column, 0);
		columnEntries.add(cVect.elementAt(choice));
		finishedColumnField[column]=size-1;
		int [] pos = new int[2];
		pos[0]=column;
		pos[1]=0;
		columnEntryPos.add(pos);
		return true;
	}
	public void findLineWord(int line){
		int startPos = finishedLineField[line];
		int maxLength = size-startPos;
		if(maxLength<2){
			return;
		}
		char [] crossLine = grid.getLine(line);
		for(int i=0;i<maxLength-2;i++){
			int offset = startPos+i;
			if(offset==0 || crossLine[offset-1]==Crossword.EMPTY_CHAR || crossLine[offset-1]==Crossword.BLOCK_CHAR){
				int actualMaxLength = maxLength-i;
				for(int j=0; j<actualMaxLength; j++){
					int length = actualMaxLength-j;
					boolean isRegular = testRegularity(crossLine, offset, length);
					if(isRegular){
						Vector lVect = lexicon.getEntries(length, crossLine, offset, usedWords);
						if(lVect!=null){
							int vSz = lVect.size();
							if(vSz>0){
								int choice = getRandomInt(vSz);
								CrosswordLexiconEntry entry = (CrosswordLexiconEntry) lVect.elementAt(choice);
								usedWords.add(entry);
								grid.insertWordInLine(entry.getLemma(), line, offset);
								
								finishedLineField[line]+=offset+length;
								grid.insertBlock(line,offset+length);
								if(offset>0){
									grid.insertBlock(line, offset-1);
								}
								int [] pos = new int[2];
								pos[0]=line;
								pos[1]=offset;
								/*
								int [] ePos = (int []) lineEntryPos.get(0);
								int entryPos = 1; 
								while((entryPos < vSz) && ePos!=null && (ePos[0]<line || (ePos[0]==line &&  ePos[1]<offset))){
									ePos = (int []) lineEntryPos.get(entryPos);
									entryPos++;
								}
								
								lineEntryPos.add(entryPos, pos);
								lineEntries.add(entryPos, entry);
								 
								 */
								lineEntryPos.add(pos);
								lineEntries.add(entry);
								return;
							}
						}
					}
				}
			}
		}
	}
	public void findColumnWord(int column){
		int startPos = finishedColumnField[column];
		int maxLength = size-startPos;
		if(maxLength<2){
			return;
		}
		char [] crossColumn = grid.getColumn(column);
		for(int i=0;i<maxLength-2;i++){
			int offset = startPos+i;
			if(offset==0 || crossColumn[offset-1]==Crossword.EMPTY_CHAR || crossColumn[offset-1]==Crossword.BLOCK_CHAR){
				int actualMaxLength = maxLength-i;
				for(int j=0; j<actualMaxLength; j++){
					int length = actualMaxLength-j;
					boolean isRegular = testRegularity(crossColumn, offset, length);
					if(isRegular){
						Vector lVect = lexicon.getEntries(length, crossColumn, offset, usedWords);
						if(lVect!=null){
							int vSz = lVect.size();
							if(vSz>0){
								int choice = getRandomInt(vSz);
								CrosswordLexiconEntry entry = (CrosswordLexiconEntry) lVect.elementAt(choice);
								usedWords.add(entry);
								grid.insertWordInColumn(entry.getLemma(), column, offset);
								columnEntries.add(entry);
								finishedColumnField[column]+=offset+length;
								grid.insertBlock(offset+length, column);
								if(offset>0){
									grid.insertBlock(offset-1, column);
								}
								int [] pos = new int[2];
								pos[0]=column;
								pos[1]=offset;
								columnEntryPos.add(pos);
								return;
							}
						}
					}
				}
			}
		}
	}
	public boolean testRegularity(char [] crossColumn, int offset, int length){
		boolean leftRegular = false;
		boolean rightRegular = false;
		boolean hasLetter = false;
		int wordEndPos=offset+length-1;
		int patternEndPos = crossColumn.length-1;
		if(offset>0){
			if(crossColumn[offset-1]==Crossword.EMPTY_CHAR){
				leftRegular=true;
			}else if(crossColumn[offset-1]==Crossword.BLOCK_CHAR){
				leftRegular=true;
			}
		}else{
			leftRegular=true;
		}
		if(!leftRegular){
			return false;
		}
		if(wordEndPos<patternEndPos){
			if(crossColumn[wordEndPos+1]==Crossword.EMPTY_CHAR){
				rightRegular=true;
			}else if(crossColumn[wordEndPos+1]==Crossword.BLOCK_CHAR){
				rightRegular=true;
			}			
		}else{
			rightRegular=true;
		}
		if(!rightRegular){
			return false;
		}
		for(int i=offset; i<=wordEndPos; i++){
			if(crossColumn[i]==Crossword.BLOCK_CHAR){
				return false;
			}
			if(crossColumn[i]!=Crossword.EMPTY_CHAR){
				hasLetter=true;
			}
		}
		return hasLetter;
	}
	public int countNumberOfCrosses(){
		boolean [][] lineGrid = new boolean [size][size];
		for(int i=0; i<numberOfLineWords; i++){
			CrosswordLexiconEntry e =  (CrosswordLexiconEntry) lineEntries.get(i);
			int length = e.getSize();
			int [] pos = (int []) lineEntryPos.get(i);
			for(int j=0; j<length; j++){
				lineGrid[pos[0]][pos[1]+j]=true;
			}
		}
		int crossCount = 0; 
		for(int i=0; i<numberOfColumnWords; i++){
			CrosswordLexiconEntry e =  (CrosswordLexiconEntry) columnEntries.get(i);
			int length = e.getSize();
			int [] pos = (int []) columnEntryPos.get(i);
			for(int j=0; j<length; j++){
				if(lineGrid[pos[1]+j][pos[0]]){
					crossCount++;
				}
			}
		}
		return crossCount;
	}
	
	public void generateGridLabels(){
		int labelCount = 1;
		int lnWds = lineEntryPos.size();
		lineLabelMap = new int[lnWds];
		int colWds = columnEntryPos.size();
		columnLabelMap = new int[colWds];	
		gridLabels = new int[size][];
		for(int i=0; i<size; i++){
			gridLabels[i]=new int[size];
			for(int j=0; j<size; j++){
				int lnWd = -1;
				for(int ii=0; ii< lnWds && lnWd<0; ii++){
					int [] pos= (int [])lineEntryPos.elementAt(ii);
					if(pos[0]==i && pos[1]==j){
						lnWd=ii;
						lineLabelMap[ii]=labelCount;
					}	
				}
				int colWd = -1;
				for(int ii=0; ii< colWds && colWd<0; ii++){
					int [] pos= (int [])columnEntryPos.elementAt(ii);
					if(pos[1]==i && pos[0]==j){
						colWd=ii;
						columnLabelMap[ii]=labelCount;
					}	
				}
				if(lnWd>=0 || colWd>=0){
					gridLabels[i][j]=labelCount;
					labelCount++;
				}
			}
		}
		for(int i=0; i< lnWds; i++){
			int [] pos= (int [])lineEntryPos.elementAt(i);
			
		}
	}
	public  int [][] getGridLabels(){
		return gridLabels;
	}
	public  int [] getLineLabelMap(){
		return lineLabelMap;
	}
	public  int [] getColumnLabelMap(){
		return columnLabelMap;
	}
	public Crossword getGrid(){
		return grid;
	}
	public String generateGridHTML(){
		return generateGridHTML(false);
	}
	public String generateGridHTML(boolean rightToLeft){
		int gridSize = DEFAULT_HTML_GRID_SIZE;
		int fieldSize = (int) Math.round(gridSize/(0.0+size)-0.5);
		gridSize = size*fieldSize;
		
		if(gridLabels==null){
			generateGridLabels();
		}
		char [][] gridLineChars = grid.getLines();
		StringBuffer strBuff = new StringBuffer();
		strBuff.append("<table  rules=\"all\" width=\""+Integer.toString(gridSize)+"\" height=\""+Integer.toString(gridSize)+"\"");
		strBuff.append(" id=\""+DEFAULT_HTML_CROSSWORD_STYLE+"\">\n");
		strBuff.append("<colgroup col=\""+Integer.toString(size)+"\"></colgroup>\n");
		for(int i=0; i<size; i++){
			strBuff.append("<tr valign=\"center\">\n");
			if(rightToLeft){
				for(int j=size-1; j>=0; j--){
					strBuff.append("<td");
					if(i==0){
						strBuff.append(" width=\""+Integer.toString(fieldSize)+"\"");
					}
					if(j==0){
						strBuff.append(" height=\""+Integer.toString(fieldSize)+"\"");
					}
					if(gridLineChars[i][j]==Crossword.BLOCK_CHAR || gridLineChars[i][j]==Crossword.EMPTY_CHAR){
						strBuff.append(" bgcolor=\"black\">"); 
					}else{
						strBuff.append(">");
						if(gridLabels[i][j]>0){
							strBuff.append("<table><tr valign=\"top\"><td>");
							strBuff.append("<sup>"+Integer.toString(gridLabels[i][j])+"</sup></td>");
							strBuff.append("<td><input type=\"text\" maxlength=\"1\"  size=\"2\"/></td>");
							strBuff.append("<td>&nbsp;</td></tr></table>");
						}else{
							strBuff.append("<input type=\"text\" maxlength=\"1\" size=\"2\"/>");
						}
					}
					strBuff.append("</td>\n");
				}
			}else{
				for(int j=0; j<size; j++){
				strBuff.append("<td");
				if(i==0){
					strBuff.append(" width=\""+Integer.toString(fieldSize)+"\"");
				}
				if(j==0){
					strBuff.append(" height=\""+Integer.toString(fieldSize)+"\"");
				}
				if(gridLineChars[i][j]==Crossword.BLOCK_CHAR || gridLineChars[i][j]==Crossword.EMPTY_CHAR){
					strBuff.append(" bgcolor=\"black\">"); 
				}else{
					strBuff.append(">");
					if(gridLabels[i][j]>0){
						strBuff.append("<table><tr valign=\"top\"><td>");
						strBuff.append("<sup>"+Integer.toString(gridLabels[i][j])+"</sup></td>");
						strBuff.append("<td><input type=\"text\" maxlength=\"1\"  size=\"2\"/></td>");
						strBuff.append("<td>&nbsp;</td></tr></table>");
					}else{
						strBuff.append("<input type=\"text\" maxlength=\"1\" size=\"2\"/>");
					}
				}
				strBuff.append("</td>\n");
				}
			}
			strBuff.append("</tr>\n");
		}
		strBuff.append("</table>\n");
		return strBuff.toString();
	}
	public String generateSolutionHTML(){
		return generateGridHTML(false);
	}
	public String generateSolutionHTML(boolean rightToLeft){
		int gridSize = DEFAULT_HTML_SOLUTION_GRID_SIZE;
		int fieldSize = (int) Math.round(gridSize/(0.0+size)-0.5);
		gridSize = size*fieldSize;
		
		if(gridLabels==null){
			generateGridLabels();
		}
		char [][] gridLineChars = grid.getLines();
		StringBuffer strBuff = new StringBuffer();
		strBuff.append("<table  rules=\"all\" width=\""+Integer.toString(gridSize)+"\" height=\""+Integer.toString(gridSize)+"\"");
		strBuff.append(" id=\""+DEFAULT_HTML_SOLUTION_STYLE+"\">\n");
		strBuff.append("<colgroup col=\""+Integer.toString(size)+"\"></colgroup>\n");
		if(rightToLeft){
			for(int i=0; i<size; i++){
				strBuff.append("<tr>\n");
				for(int j=size-1; j>=0; j--){
					strBuff.append("<td");
					if(i==0){
						strBuff.append(" width=\""+Integer.toString(fieldSize)+"\"");
					}
					if(j==0){
						strBuff.append(" height=\""+Integer.toString(fieldSize)+"\"");
					}
					if(gridLineChars[i][j]==Crossword.BLOCK_CHAR || gridLineChars[i][j]==Crossword.EMPTY_CHAR){
						strBuff.append(" bgcolor=\"black\">"); 
					}else{
						strBuff.append(">"+gridLineChars[i][j]); 
					}
					strBuff.append("</td>\n");
				}
				strBuff.append("</tr>\n");
			}			
		}else{
			for(int i=0; i<size; i++){
				strBuff.append("<tr>\n");
				for(int j=0; j<size; j++){
					strBuff.append("<td");
					if(i==0){
						strBuff.append(" width=\""+Integer.toString(fieldSize)+"\"");
					}
					if(j==0){
						strBuff.append(" height=\""+Integer.toString(fieldSize)+"\"");
					}
					if(gridLineChars[i][j]==Crossword.BLOCK_CHAR || gridLineChars[i][j]==Crossword.EMPTY_CHAR){
						strBuff.append(" bgcolor=\"black\">"); 
					}else{
						strBuff.append(">"+gridLineChars[i][j]); 
					}
					strBuff.append("</td>\n");
				}
				strBuff.append("</tr>\n");
			}
		}
		strBuff.append("</table>\n");
		return strBuff.toString();
	}
	public String generateCluesHTML(){
		return generateCluesHTML(false);
	}
	public String generateCluesHTML(boolean rightToRight){
		StringBuffer strBuff = new StringBuffer();
		Vector lineWords = lineEntries;
		int [] lnMap = getLineLabelMap();
		int [] colMap = getColumnLabelMap();
		int lineSize = lineWords.size();
		Vector columnWords = columnEntries;
		int colSize = columnWords.size();
		int maxLbNum = lineSize+colSize;
		strBuff.append("<table><tr valign=\"top\"><td>");
		strBuff.append("<table  id=\"clues\"><tr><td>");
		if(rightToRight){
			strBuff.append("&#8592;");
		}else{
			strBuff.append("&#8594;");
		}
		strBuff.append("&nbsp;</td><td></td></tr>\n");
		for(int i=1; i<=maxLbNum; i++){
			for(int ii=0; ii<lineSize; ii++){
				if(lnMap[ii]==i){
					CrosswordLexiconEntry e =(CrosswordLexiconEntry) lineWords.get(ii);
					int labelNum = lnMap[ii];
					strBuff.append("<tr valign=\"top\"><td>"+Integer.toString(labelNum)+"</td><td>"+e.getClue()+"</td></tr>\n");
				}
			}
		}
		strBuff.append("</td></tr></table>");
		strBuff.append("</td><td>");
		strBuff.append("<table  id=\"clues\"><tr><td>&#8595;&nbsp;</td><td></td></tr>\n");
		for(int i=1; i<=maxLbNum; i++){
			for(int ii=0; ii<colSize; ii++){
				if(colMap[ii]==i){
					CrosswordLexiconEntry e =(CrosswordLexiconEntry) columnWords.get(ii);
					int labelNum = colMap[ii];
					strBuff.append("<tr valign=\"top\"><td>"+Integer.toString(labelNum)+"</td><td>"+e.getClue()+"</td></tr>\n");
				}
			}
		}
		strBuff.append("</td></tr></table>");
		strBuff.append("</td></tr></table>");
		return strBuff.toString();
	}
}
