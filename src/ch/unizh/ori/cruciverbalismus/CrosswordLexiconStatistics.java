package ch.unizh.ori.cruciverbalismus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class CrosswordLexiconStatistics {
	
	private CrosswordLexicon lexicon;
	private HashMap characterCount;
	private ArrayList wordLengthCount;
	private int minWordLength;
	private int maxWordLength;
	private int numOfCharacters;
	private double meanWordLength;
	private double varWordLength;
	
	
	public CrosswordLexiconStatistics(CrosswordLexicon lexicon_in){
		lexicon = lexicon_in;
		characterCount = new HashMap();
		wordLengthCount = new ArrayList();
		count();
	}
	public void incrementCharacterCount(Character ch){
		Integer cnt = (Integer) characterCount.get(ch);
		if(cnt!=null){
			cnt = new Integer(1+cnt.intValue());
		}else{
			characterCount.put(ch, new Integer(1));
		}
	}
	public void incrementWordLengthCount(int ln){
		Integer cnt = (Integer) wordLengthCount.get(ln);
		if(cnt!=null){
			cnt = new Integer(1+cnt.intValue());
		}else{
			wordLengthCount.add(ln, new Integer(1));
		}
	}
	public void count(){
		maxWordLength = lexicon.getMaxSize();
		
		minWordLength = 0;
		numOfCharacters = 0;
		for(int i=0; i<=maxWordLength; i++){
			Vector entries = lexicon.getEntries(i);
			if(entries!=null){
				int ln = entries.size();
				if(minWordLength == 0 && ln>0){
					minWordLength = i;
				}
				for(int j=0;j<ln; j++){
					CrosswordLexiconEntry entry = (CrosswordLexiconEntry) entries.get(j);
					String lemma = entry.getLemma();
					int sz = lemma.length();
					for(int k=0; k<sz; k++){
						Character ch = new Character(lemma.charAt(k));
						incrementCharacterCount(ch);
						numOfCharacters++;
					}
					incrementWordLengthCount(sz);
				}
			}
			
			
		}
		int sumOfSqrs = 0;
		int sum = 0;
		int n = 0;
		for(int i=minWordLength; i<=maxWordLength; i++){
			int tmp = ((Integer) wordLengthCount.get(i)).intValue();
			sum += tmp;
			sumOfSqrs += tmp*tmp;
			n++;
		}
		varWordLength = sumOfSqrs-sum*sum/(n-1.0);
	}
	
}
