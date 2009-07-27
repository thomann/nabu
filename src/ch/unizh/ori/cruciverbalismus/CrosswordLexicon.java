package ch.unizh.ori.cruciverbalismus;

import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

public class CrosswordLexicon {
	private Vector [] lexicon;
	private Vector deletePatterns;
	private Vector replacePatterns;
	private int maxSize;
	public static final int DEFAULT_MAX_SIZE=100000;
	public static final char EMPTY_CHAR = ' ';

		public CrosswordLexicon(){
			maxSize = DEFAULT_MAX_SIZE;
			lexicon = new Vector[maxSize];
			for(int i=0; i<maxSize; i++){
				lexicon[i]=new Vector();
			}
		}
		public CrosswordLexicon(int max_s){
			maxSize = max_s;
			lexicon = new Vector[maxSize];
			for(int i=0; i<maxSize; i++){
				lexicon[i]=new Vector();
			}
		}
		public int buildLexicon(String lexiconString){
			StringTokenizer linePrsr = new StringTokenizer(lexiconString, "\n\r");
			int cnt = 0;
			int actualMaxSize = 0;
			while(linePrsr.hasMoreTokens()){
				String lexiconLineStr = linePrsr.nextToken();
				CrosswordLexiconEntry newEntry = new CrosswordLexiconEntry(lexiconLineStr);
				int sz = newEntry.getSize();
				if(sz>0 && sz <=maxSize){
					lexicon[sz-1].add(newEntry);
					cnt++;
					if(sz>actualMaxSize){
						actualMaxSize=sz;
					}
				}
			}
			maxSize = actualMaxSize;
			return cnt;
		}
		public int buildLexicon(String lexiconString, boolean xcols){
			StringTokenizer linePrsr = new StringTokenizer(lexiconString, "\n\r");
			int cnt = 0;
			int actualMaxSize = 0;
			while(linePrsr.hasMoreTokens()){
				String lexiconLineStr = linePrsr.nextToken();
				CrosswordLexiconEntry newEntry = new CrosswordLexiconEntry(lexiconLineStr, xcols);
				int sz = newEntry.getSize();
				if(sz>0 && sz <=maxSize){
					lexicon[sz-1].add(newEntry);
					cnt++;
					if(sz>actualMaxSize){
						actualMaxSize=sz;
					}
				}
			}
			maxSize = actualMaxSize;
			return cnt;
		}
		public void buildDeletePatterns(String deleteString){
			deletePatterns = new Vector();
			StringTokenizer linePrsr = new StringTokenizer(deleteString, "\n\r");
			while(linePrsr.hasMoreTokens()){
				String  str = linePrsr.nextToken();
				if(str!=null && str.length()>0){
					deletePatterns.add(str);		
				}
			}
		}
		public void buildReplacePatterns(String replaceString){			
			replacePatterns = new Vector();
			StringTokenizer linePrsr = new StringTokenizer(replaceString, "\n\r");
			while(linePrsr.hasMoreTokens()){
				String  str = linePrsr.nextToken();
				String [] items = str.split("\t");
				if(items!=null && items.length>1 && items[0]!=null && items[0].length()>0 && items[1]!=null && items[1].length()>0){
					replacePatterns.add(items);		
				}
			}		
		}
		public String prepareLemma(String lemma){
			int replaceSz = replacePatterns.size();
			String newLemma = lemma;
			for(int i=0; i<replaceSz; i++){
				String [] replaceStr = (String []) replacePatterns.get(i);
				newLemma = newLemma.replaceAll(replaceStr[0], replaceStr[1]);
			}
			int deleteSz = deletePatterns.size();
			for(int i=0; i<deleteSz; i++){
				String deleteStr = (String) deletePatterns.get(i);
				newLemma = newLemma.replaceAll(deleteStr, "");
			}
			return newLemma;
		}
		public int buildLexicon(String lexiconString, String replaceString, String deleteString){
			buildDeletePatterns(deleteString);
			buildReplacePatterns(replaceString);
			
			StringTokenizer linePrsr = new StringTokenizer(lexiconString, "\n\r");
			int cnt = 0;
			int actualMaxSize = 0;
			while(linePrsr.hasMoreTokens()){
				String lexiconLineStr = linePrsr.nextToken();
				CrosswordLexiconEntry newEntry = new CrosswordLexiconEntry(lexiconLineStr, this);
				int sz = newEntry.getSize();
				if(sz>0 && sz <=maxSize){
					lexicon[sz-1].add(newEntry);
					cnt++;
					if(sz>actualMaxSize){
						actualMaxSize=sz;
					}
				}
			}
			maxSize = actualMaxSize;
			return cnt;
		}
		public int buildLexicon(String lexiconString, String replaceString, String deleteString, boolean xcols){
			buildDeletePatterns(deleteString);
			buildReplacePatterns(replaceString);
			
			StringTokenizer linePrsr = new StringTokenizer(lexiconString, "\n\r");
			int cnt = 0;
			int actualMaxSize = 0;
			while(linePrsr.hasMoreTokens()){
				String lexiconLineStr = linePrsr.nextToken();
				CrosswordLexiconEntry newEntry = new CrosswordLexiconEntry(lexiconLineStr, this, xcols);
				int sz = newEntry.getSize();
				if(sz>0 && sz <=maxSize){
					lexicon[sz-1].add(newEntry);
					cnt++;
					if(sz>actualMaxSize){
						actualMaxSize=sz;
					}
				}
			}
			maxSize = actualMaxSize;
			return cnt;
		}
		public int getMaxSize(){
			return maxSize;
		}
		public Vector getEntries(int sz){
			if(sz>0 && sz<=maxSize){
				return lexicon[sz];
			}
			return null;
		}
		
		public Vector getEntries(int sz, char [] pattern){
			if(sz>0 && sz<=maxSize){
				int lexSize = lexicon[sz-1].size();
				Vector found = new Vector();
				for(int i=0; i<lexSize; i++){
					CrosswordLexiconEntry testEntry = (CrosswordLexiconEntry) lexicon[sz-1].get(i);
					if(testEntry.fit(pattern))
						found.add(testEntry);
				}
				return found;
			}
			return null;
		}
		public Vector getEntries(int sz, char [] pattern, Set usedWords){
			if(sz>0 && sz<=maxSize){
				int lexSize = lexicon[sz-1].size();
				Vector found = new Vector();
				for(int i=0; i<lexSize; i++){
					CrosswordLexiconEntry testEntry = (CrosswordLexiconEntry) lexicon[sz-1].get(i);
					if(testEntry.fit(pattern) && !usedWords.contains(testEntry))
						found.add(testEntry);
				}
				return found;
			}
			return null;
		}
		public Vector getEntries(int sz, char [] pattern, int offset){
			if(sz>0 && sz<=maxSize){
				int lexSize = lexicon[sz-1].size();
				Vector found = new Vector();
				for(int i=0; i<lexSize; i++){
					CrosswordLexiconEntry testEntry = (CrosswordLexiconEntry) lexicon[sz-1].get(i);
					if(testEntry.fit(pattern, offset))
						found.add(testEntry);
				}
				return found;
			}
			return null;
			
		}
		public Vector getEntries(int sz, char [] pattern, int offset, Set usedWords){
			if(sz>0 && sz<=maxSize){
				int lexSize = lexicon[sz-1].size();
				Vector found = new Vector();
				for(int i=0; i<lexSize; i++){
					CrosswordLexiconEntry testEntry = (CrosswordLexiconEntry) lexicon[sz-1].get(i);
					if(testEntry.fit(pattern, offset) && !usedWords.contains(testEntry))
						found.add(testEntry);
				}
				return found;
			}
			return null;
			
		}
}
