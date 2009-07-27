/*
 * Created on 25.11.2004
 *
 */
package ch.unizh.ori.common.text.helper;

/**
 * @author J. Thomann
 *
 * created first in project JApd
 * 
 */
public class ArabicHelper {
	public static final String [][] latinizeTable = {
		//{"\u0640\u062a\u064e ","<sup>ta</sup> "},	// tatweel-ta-fatHa
		//{"\u0640\u062a\u064f ","<sup>tu</sup> "},	// tatweel-ta-Damma
		//{"\u0640\u062a\u0650 ","<sup>ti</sup> "},	// tatweel-ta-kasra
		//{"\u064e\u0640\u062a\u0652 ","<sup>at</sup> "},	// fatHa-tatweel-ta-sukun
		
		{"\u0640",""},				// fatHa-tatweel
		{"\u0621", "\u02be"},		// hamza
		{"\u0622", "\u02be\0101"},	// alif madda
		{"\u0623", "\u02be"},		// alif hamza
		{"\u0624", "\u02be"},		// waw hamza
		{"\u0625", "\u02be"},		// alif hamza be
		{"\u0626", "\u02be"},		// final ya hamzata


		{"\u064f\u0648\u0652","uw"},	// Damma-waw-sukun
		{"\u0650\u064a\u0652","iy"},	// kasra-ya'-sukun
		{"\u064f\u0648\u064e","uwa"},	// Damma-waw-fatHa
		{"\u0650\u064a\u064e","iya"},	// kasra-ya'-fatHa
		{"\u064f\u0648\u0651","uww"},	// Damma-waw-fatHa
		{"\u0650\u064a\u0651","iyy"},	// kasra-ya'-fatHa


		{"\u064e\u0627","\u0101"},	// fatHa-alif 
		{"\u064f\u0648","\u016B"},	// Damma-waw	
		{"\u0650\u064a","\u012b"},	// kasra-ya'
		{"\u064e\u0649","\u00e0"},	// fatHa-alif maqsura
		{"\u064f\u0648\u0627 ","\u00fb "},	// Damma-waw-alif-space
		
		
		{" \u0627\u0644\u0652"," \u0103l-"},	// start-alif-lam-sukun
		{"\u0627\u0644\u062a\u0651","\u0103t-t"},	// alif-lam-ta-shadda
		{"\u0627\u0644\u062b\u0651","\u0103\u1e6f-\u1e6f"},	// alif-lam-tha-shadda
		{"\u0627\u0644\u062f\u0651","\u0103d-d"},	// alif-lam-dal-shadda
		{"\u0627\u0644\u0630\u0651","\u0103\u1e0f-\u1e0f"},	// alif-lam-dhal-shadda
		{"\u0627\u0644\u0631\u0651","\u0103r-r"},	// alif-lam-ra-shadda		
		{"\u0627\u0644\u0632\u0651","\u0103z-z"},	// alif-lam-zay-shadda			
		{"\u0627\u0644\u0633\u0651","\u0103s-s"},	// alif-lam-sin-shadda
		{"\u0627\u0634\u0651","\u0103\u0161-\u0161"},	// alif-lam-shin-shadda
		{"\u0627\u0644\u0635\u0651","\u0103\u1e63-\u1e63"},	// alif-lam-Sad-shadda
		{"\u0627\u0644\u0636\u0651","\u0103\u1e0d-\u1e0d"},	// alif-lam-Dad-shadda
		{"\u0627\u0644\u0637\u0651","\u0103\u1e6d-\u1e6d"},	// alif-lam-Ta-shadda
		{"\u0627\u0644\u0638\u0651","\u0103\u1e93-\u1e93"},	// alif-lam-Za-shadda
		{"\u0627\u0644\u0644\u0651","\u0103l-l"},	// alif-lam-lam-shadda
			//Alphabet
		{"\u0628","b"},			// ba
		{"\u0629","t\u0308"},	// ta marbuta 
		{"\u062a","t"},			// ta 
		{"\u062b","\u1e6f"},	// tha 
		{"\u062c","\u01e7"},	// jim
		{"\u062d","\u1e25"},	// Ha 
		{"\u062e","\u1e2b"},	// xa
		{"\u062f","d"},			// dal
		{"\u0630","\u1e0f"},	// dhal
		{"\u0631","r"},			// ra
		{"\u0632","z"},			// zay
		{"\u0633","s"},			// sin
		{"\u0634","\u0161"},	// shin 
		{"\u0635","\u1e63"},	// Sad
		{"\u0636","\u1e0d"},	// Dad
		{"\u0637","\u1e6d"},	// Ta
		{"\u0638","\u1e93"},	// Za
		{"\u0639","\u02bf"},	// Eayn
		{"\u063a","\u0121"},	// ghayn
		{"\u0640",""},	// kashida
		{"\u0641","f"},	// fa
		{"\u0642","q"},	// qaf
		{"\u0643","k"},	// kaf
		{"\u0644","l"},	// lam
		{"\u0645","m"},	// mim
		{"\u0646","n"},	// nun
		{"\u0647","h"},	// ha
		{"\u0648","w"},	// waw
		{"\u064a","y"},	// ya
		
		{"\u064b\u0627",":an"},	// fatHatan-alif
		{"\u064b",":an"},	// fatHatan
		{"\u064c",":un"},	// Dammatan
		{"\u064d",":in"},	// kasratan
		{"\u064e","a"},	// fatHa
		{"\u064f","u"},	// Damma
		{"\u0650","i"},	// kasra
		{"\u0651","+"},	// shadda
		{"\u0652",""},	// sukun
		{"\u0670","\u0101"},	// superscript alif 
		{"\u0671","\u012D"},	//  alif al-wasl 
		{"\u0627\u0653","\u02be\u0101"}	// alif-madda
		};
		
	public static final String [][] latinizeMbrolaTable = {
		//{"\u0640\u062a\u064e ","<sup>ta</sup> "},	// tatweel-ta-fatHa
		//{"\u0640\u062a\u064f ","<sup>tu</sup> "},	// tatweel-ta-Damma
		//{"\u0640\u062a\u0650 ","<sup>ti</sup> "},	// tatweel-ta-kasra
		//{"\u064e\u0640\u062a\u0652 ","<sup>at</sup> "},	// fatHa-tatweel-ta-sukun
		
		{"\u0640","-"},				// fatHa-tatweel
		{"\u0621", "?"},		// hamza
		{"\u0622", "?aa"},	// alif madda
		{"\u0623", "?"},		// alif hamza
		{"\u0624", "?"},		// waw hamza
		{"\u0625", "?"},		// alif hamza be
		{"\u0626", "?"},		// final ya hamzata


		{"\u064f\u0648\u0652","uw"},	// Damma-waw-sukun
		{"\u0650\u064a\u0652","iy"},	// kasra-ya'-sukun
		{"\u064f\u0648\u064e","uwa"},	// Damma-waw-fatHa
		{"\u0650\u064a\u064e","iya"},	// kasra-ya'-fatHa
		{"\u064f\u0648\u0651","uww"},	// Damma-waw-fatHa
		{"\u0650\u064a\u0651","iyy"},	// kasra-ya'-fatHa


		{"\u064e\u0627","aa"},	// fatHa-alif 
		{"\u064f\u0648","uu"},	// Damma-waw	
		{"\u0650\u064a","ii"},	// kasra-ya'
		{"\u064e\u0649","aa"},	// fatHa-alif maqsura
		{"\u064f\u0648\u0627 ","uu "},	// Damma-waw-alif-space
		
		
		{" \u0627\u0644\u0652"," al "},	// start-alif-lam-sukun
		{"\u0627\u0644\u062a\u0651","at t"},	// alif-lam-ta-shadda
		{"\u0627\u0644\u062b\u0651","ath th"},	// alif-lam-tha-shadda
		{"\u0627\u0644\u062f\u0651","ad d"},	// alif-lam-dal-shadda
		{"\u0627\u0644\u0630\u0651","adh dh"},	// alif-lam-dhal-shadda
		{"\u0627\u0644\u0631\u0651","ar r"},	// alif-lam-ra-shadda		
		{"\u0627\u0644\u0632\u0651","az z"},	// alif-lam-zay-shadda			
		{"\u0627\u0644\u0633\u0651","as s"},	// alif-lam-sin-shadda
		{"\u0627\u0634\u0651","ash sh"},	// alif-lam-shin-shadda
		{"\u0627\u0644\u0635\u0651","aS S"},	// alif-lam-Sad-shadda
		{"\u0627\u0644\u0636\u0651","aD D"},	// alif-lam-Dad-shadda
		{"\u0627\u0644\u0637\u0651","aT T"},	// alif-lam-Ta-shadda
		{"\u0627\u0644\u0638\u0651","aZ Z"},	// alif-lam-Za-shadda
		{"\u0627\u0644\u0644\u0651","al l"},	// alif-lam-lam-shadda
			//Alphabet
		{"\u0628\u0651","bb"},			// ba
		{"\u062a\u0651","tt"},			// ta 
		{"\u062b\u0651","thth"},	// tha 
		{"\u062c\u0651","jj"},	// jim
		{"\u062d\u0651","HH"},	// Ha 
		{"\u062e\u0651","khkh"},	// xa
		{"\u062f\u0651","dd"},			// dal
		{"\u0630\u0651","dhdh"},	// dhal
		{"\u0631\u0651","rr"},			// ra
		{"\u0632\u0651","zz"},			// zay
		{"\u0633\u0651","ss"},			// sin
		{"\u0634\u0651","shsh"},	// shin 
		{"\u0635\u0651","SS"},	// Sad
		{"\u0636\u0651","DD"},	// Dad
		{"\u0637\u0651","TT"},	// Ta
		{"\u0638\u0651","ZZ"},	// Za
		{"\u0639\u0651","99"},	// Eayn
		{"\u063a\u0651","ghgh"},	// ghayn
		{"\u0641\u0651","ff"},	// fa
		{"\u0642\u0651","qq"},	// qaf
		{"\u0643\u0651","kk"},	// kaf
		{"\u0644\u0651","ll"},	// lam
		{"\u0645\u0651","mm"},	// mim
		{"\u0646\u0651","nn"},	// nun
		{"\u0647\u0651","hh"},	// ha
		{"\u0648\u0651","ww"},	// waw
		{"\u064a\u0651","yy"},	// ya
			//Alphabet
		{"\u0628","b"},			// ba
		{"\u0629","t"},	// ta marbuta 
		{"\u062a","t"},			// ta 
		{"\u062b","th"},	// tha 
		{"\u062c","j"},	// jim
		{"\u062d","H"},	// Ha 
		{"\u062e","kh"},	// xa
		{"\u062f","d"},			// dal
		{"\u0630","dh"},	// dhal
		{"\u0631","r"},			// ra
		{"\u0632","z"},			// zay
		{"\u0633","s"},			// sin
		{"\u0634","sh"},	// shin 
		{"\u0635","S"},	// Sad
		{"\u0636","D"},	// Dad
		{"\u0637","T"},	// Ta
		{"\u0638","Z"},	// Za
		{"\u0639","9"},	// Eayn
		{"\u063a","gh"},	// ghayn
		{"\u0640",""},	// kashida
		{"\u0641","f"},	// fa
		{"\u0642","q"},	// qaf
		{"\u0643","k"},	// kaf
		{"\u0644","l"},	// lam
		{"\u0645","m"},	// mim
		{"\u0646","n"},	// nun
		{"\u0647","h"},	// ha
		{"\u0648","w"},	// waw
		{"\u064a","y"},	// ya

		{"\u064b\u0627","an"},	// fatHatan-alif
		{"\u064b","an"},	// fatHatan
		{"\u064c","un"},	// Dammatan
		{"\u064d","in"},	// kasratan
		{"\u064e","a"},	// fatHa
		{"\u064f","u"},	// Damma
		{"\u0650","i"},	// kasra
		{"\u0651","+"},	// shadda
		{"\u0652",""},	// sukun
		{"\u0670","aa"},	// superscript alif 
		{"\u0671",""},	//  alif al-wasl 
		{"\u0627\u0653","?aa"},	// alif-madda
		{" "," "}	// space
		};
		
		
	public static final String [][] noharakatTable = {
		{"\u064b",""},	// fatHatan
		{"\u064c",""},	// Dammatan
		{"\u064d",""},	// kasratan
		{"\u064e",""},	// fatHa
		{"\u064f",""},	// Damma
		{"\u0650",""},	// kasra
		{"\u0651",""},	// shadda
		{"\u0652",""},	// sukun
		{"\u0653",""}	// madda
	};

	public static final String [][] dotlessTable = {
		{"\u064a ","\u0649 "},	// end ya'
		{"\u0628","\u066e"},	// ba
		{"\u0629","\u0647"},	// ta marbuta
		{"\u062a","\u066e"},	// ta 
		{"\u062b","\u066e"},	// tha 
		{"\u062c","\u062d"},	// jim
		{"\u062e","\u062d"},	// xa
		{"\u0630","\u062f"},	// dhal
		{"\u0632","\u0631"},	// zay
		{"\u0634","\u0633"},	// shin
		{"\u0636","\u0635"},	// Dad
		{"\u0638","\u0637"},	// Za
		{"\u063a","\u0639"},	// ghayn
		{"\u0641","\u066f"},	// fa
		{"\u0642","\u066f"},	// qaf
		{"\u0646","\u066e"},	// nun
		{"\u064a","\u066e"}		// ya
		};

	public static final String [][] surrogateTable = {
		{"\u066f","\u06a4"},	// dotless qaf
		{"\u066e","\u067e"}		// dotless be
	};
	public static final String [][] nosurrogateTable = {
		{"\u06a4", "\u066f"},	// dotless qaf
		{"\u067e", "\u066e"}	// dotless be
	};
	public static final String [][] notatweelTable = {
		{"\u0640", ""}	// tatweel
	};
	public static final String [][] nobracketTable = {
		{"\u0640[\u0640", ""},	// tatweel[tatweel
		{"\u0640]\u0640", ""},	// tatweel]tatweel
		{"[", ""},	// [
		{"]", ""},	// ]
		{"\u0640(\u0640", ""},	// tatweel(tatweel
		{"\u0640(\u0640", ""},	// tatweel)tatweel
		{"(", ""},	// (
		{")", ""},	// )
		{"\u0640{\u0640", ""},	// tatweel{tatweel
		{"\u0640}\u0640", ""},	// tatweel}tatweel
		{"{", ""},	// {
		{"}", ""}	// }
		
	};
	public static final String [][] hamzalessTable = {
		{"\u0621", ""},			// hamza
		{"\u0622", "\u0627"},	// alif madda
		{"\u0623", "\u0627"},	// alif hamza
		{"\u0624", "\u0648"},	// waw hamza
		{"\u0625", "\u0627"},	// alif hamza be
		{"\u0626 ", "\u0649 "},	// final ya hamyata
		{"\u0626", "\u066e"},	// ya hamyata
	};
	
	
	public static final String [][] nodiacriticsRegExTable = {
		{"\u0621", "['\u0621]"},	// hamza
		{"'", "['\u0621]"},		// '
		{"\u02bf", "[`\u02bf]"},		// Ayn
		{"`", "[`\u02bf"},		// `
		{"t\u0308", "[t(t\u0308)\u1e6f\u1e6d]"},	// ta marbuta
		{"\u1e6f", "[t(t\u0308)\u1e6f\u1e6d]"},	// tha
		{"\u1e6d", "[t(t\u0308)\u1e6f\u1e6d]"},	// Ta
		{"t", "[t(t\u0308)\u1e6f\u1e6d]"},	// ta
		{"\u1e0f", "[d\u1e0f\u1e0d]"},		// dhal
		{"\u1e0d", "[d\u1e0f\u1e0d]"},		// dad
		{"d", "[d\u1e0f\u1e0d]"},		// '
		{"\u01e7", "[g\u01e7\u0121]"},		// gim
		{"\u0121", "[g\u01e7\u0121]"},		// gayn
		{"g", "[g\u01e7\u0121]"},		// g
		{"\u1e25", "[h\u1e25\u1e2b]"},		// Ha
		{"\u1e2b", "[h\u1e25\u1e2b]"},		// xa
		{"h", "[h\u1e25\u1e2b]"},		// h
		{"\u0161", "[s\u0161\u1e63]"},		// shin
		{"\u1e63", "[s\u0161\u1e63]"},		// Sad
		{"s", "[s\u0161\u1e63]"},		// s
		{"\u1e93", "[z\u1e93]"},		// Za
		{"z", "[z\u1e93]"},		// zay
		{"\u0101", "[a\u0101\u00e0\u0103]"},		// aA
		{"\u00e0", "[a\u0101\u00e0\u0103]"},		// alif maqsura
		{"\u0103", "[a\u0101\u00e0\u0103]"},		// a breve
		{"a", "[a\u0101\u00e0\u0103]"},		// a
		{"\u016B", "[u\u016B\u00fb]"},		// uU	
		{"\u00fb", "[u\u016B\u00fb]"},		// uU-Alif
		{"u", "[u\u016B\u00fb]"},		// u
		{"\u012b", "[i\u012b\u012D]"},		// iY
		{"\u012D", "[i\u012b\u012D]"},		// i breve
		{"i", "[i\u012b\u012D]"},		// i
		
	};
	
	public static String dotless(String s){
		return transcode(s, dotlessTable);
	}
	public static String noharakat(String s){
		return transcode(s, noharakatTable);
	}
	public static String surrogate(String s){
		return transcode(s, surrogateTable);
	}
	public static String nosurrogate(String s){
		return transcode(s, nosurrogateTable);
	}
	public static String notatweel(String s){
		return transcode(s, notatweelTable);
	}
	public static String nobracket(String s){
		return transcode(s, nobracketTable);
	}
	public static String hamzaless(String s){
		return transcode(s, hamzalessTable);
	}
	public static String latinize(String s){
		return transcode(s, latinizeTable);
	}
	public static String latinizeMbrolaTable(String s){
		return transcode(s, latinizeMbrolaTable);
		//return reduplicate(tmpStr,'+';
	}
	public static String nodiacriticsRegEx(String s){
		return transcode(s, nodiacriticsRegExTable);
	}
	
	public static String reduplicate(String srcStr, char shadda){
		int strLength = srcStr.length();
		if(strLength<1)
			return "";
		StringBuffer strBuff = new StringBuffer(strLength);
		char oldCh = srcStr.charAt(0);
		strBuff.append(oldCh);
		for(int i=1;i<strLength;i++){
			char ch = srcStr.charAt(i);
			if(ch==shadda){
				strBuff.append(oldCh);
			}else{
				strBuff.append(ch);
				
			}
			oldCh = ch;
		}
		return strBuff.toString();
	}
	public static String transcode(String srcStr, String [][] codeTable){
		if(srcStr==null)
			return null;
		int strLength=srcStr.length();
		StringBuffer transStrBuff = new StringBuffer(strLength);
		int strPos = 0;
		boolean replaced = false;
		while(strPos<strLength){
			replaced = false;
			for(int i=0;i<codeTable.length;i++){
				if(srcStr.startsWith(codeTable[i][0], strPos)){
					transStrBuff.append(codeTable[i][1]);
					strPos+=codeTable[i][0].length();
					replaced = true;
					break;				
				};
			};
			if(!replaced){
				transStrBuff.append(srcStr.charAt(strPos));
				strPos++;
			};
			
		};
		return transStrBuff.toString();
	};
}
