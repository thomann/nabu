package ch.unizh.ori.cruciverbalismus;

import java.util.StringTokenizer;


public class CrosswordLexiconEntry {
	private String lemma;
	private String clue;
	private int size;
	private boolean used;
	public static final char EMPTY_CHAR = CrosswordLexicon.EMPTY_CHAR;

		public CrosswordLexiconEntry(String lemma_in, String clue_in){
			lemma = lemma_in;
			clue = clue_in;
			size = lemma.length();
			used = false;
		}
		
		public CrosswordLexiconEntry(String lexiconString, boolean xcols){
			StringTokenizer itemPrsr = new StringTokenizer(lexiconString, "\t");
			if(!xcols){
				if(itemPrsr.hasMoreTokens()){
					lemma = itemPrsr.nextToken();
				}else{
					lemma="";
				}
				if(itemPrsr.hasMoreTokens()){
					clue = itemPrsr.nextToken();
				}else{
					clue="";
				}
			}else{
				if(itemPrsr.hasMoreTokens()){
					clue = itemPrsr.nextToken();
				}else{
					clue="";
				}
				if(itemPrsr.hasMoreTokens()){
					lemma = itemPrsr.nextToken();
				}else{
					lemma="";
				}
				
			}
			size = lemma.length();
			used = false;
		}
		public CrosswordLexiconEntry(String lexiconString){
			StringTokenizer itemPrsr = new StringTokenizer(lexiconString, "\t");
			if(itemPrsr.hasMoreTokens()){
				lemma = itemPrsr.nextToken();
			}else{
				lemma="";
			}
			if(itemPrsr.hasMoreTokens()){
				clue = itemPrsr.nextToken();
			}else{
				clue="";
			}
			size = lemma.length();
			used = false;
		}
		public CrosswordLexiconEntry(String lexiconString, CrosswordLexicon lex){
			StringTokenizer itemPrsr = new StringTokenizer(lexiconString, "\t");
			if(itemPrsr.hasMoreTokens()){
				String tmpStr = itemPrsr.nextToken();
				lemma = lex.prepareLemma(tmpStr);
			}else{
				lemma="";
			}
			if(itemPrsr.hasMoreTokens()){
				clue = itemPrsr.nextToken();
			}else{
				clue="";
			}
			size = lemma.length();
			used = false;
		}
		public CrosswordLexiconEntry(String lexiconString, CrosswordLexicon lex, boolean xcols){
			StringTokenizer itemPrsr = new StringTokenizer(lexiconString, "\t");
			if(!xcols){
				if(itemPrsr.hasMoreTokens()){
					String tmpStr = itemPrsr.nextToken();
					lemma = lex.prepareLemma(tmpStr);
				}else{
					lemma="";
				}
				if(itemPrsr.hasMoreTokens()){
					clue = itemPrsr.nextToken();
				}else{
					clue="";
				}
			}else{
				if(itemPrsr.hasMoreTokens()){
					String tmpStr = itemPrsr.nextToken();
					clue = lex.prepareLemma(tmpStr);
				}else{
					clue="";
				}
				if(itemPrsr.hasMoreTokens()){
					lemma = itemPrsr.nextToken();
				}else{
					lemma="";
				}
				
			}
			size = lemma.length();
			used = false;
		}
		
		
		public String getLemma(){
			return lemma;
		}
		public String getClue(){
			return clue;
		}
		public int getSize(){
			return size;
		}
		public boolean getUsed(){
			return used;
		}
		public void setUsed(boolean u){
			used = u;
		}
		public boolean fit(char [] pattern){
			return fit(pattern, 0);
		}
		public boolean fit(char [] pattern, int offset){
			int ptrnSize = pattern.length;
			if((ptrnSize-offset)<size){
				return false;
			}
			for(int i=0; i<size;i++){
				char ch = pattern[offset+i];
				if(ch!= EMPTY_CHAR && ch!=lemma.charAt(i)){
					return false;
				}
			}
			return true;
		}
}
