package ch.unizh.ori.cruciverbalismus;

public class Crossword {
	private char [][] lines;
	private char [][] columns;

	public static final char EMPTY_CHAR = ' ';
	public static final char BLOCK_CHAR = '*';
	int size;

	public Crossword(int s_in){
		size = s_in;
		lines = new char [size][size];
		columns = new char [size][size];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				lines[i][j]=EMPTY_CHAR;
				columns[i][j]=EMPTY_CHAR;
			}
		}

	}
	public String toString(){
		StringBuffer strBuff = new StringBuffer();
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				strBuff.append(lines[i][j]);
			}
			strBuff.append('\n');
		}
		return strBuff.toString();
	}
	public char[] getLine(int n){
		return lines[n];
	}
	public char[] getColumn(int n){
		return columns[n];
	}
	public char[][]getLines(){
		return lines;
	}
	public char[][]getColumns(){
		return columns;
	}
	public boolean insertWordInLine(String word, int line, int offset){
		int wordSize = word.length();
		if((wordSize+offset)>size){
			return false;
		}
		for(int i=0;i<wordSize;i++){
			lines[line][offset+i]=word.charAt(i);
			columns[offset+i][line]=word.charAt(i);
		}
		return true;
	}
	public boolean insertWordInColumn(String word, int column, int offset){
		int wordSize = word.length();
		if((wordSize+offset)>size){
			return false;
		}
		for(int i=0;i<wordSize;i++){
			columns[column][offset+i]=word.charAt(i);
			lines[offset+i][column]=word.charAt(i);
		}
		return true;
	}
	public boolean insertBlock(int line, int column){
		if(line>=size || column>=size){
			return false;
		}
		lines[line][column]=BLOCK_CHAR;
		columns[column][line]=BLOCK_CHAR;
		return true;
	}
}
