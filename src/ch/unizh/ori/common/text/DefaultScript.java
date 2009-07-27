/*
 * Created on 05.03.2006
 *
 */
package ch.unizh.ori.common.text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ch.unizh.ori.nabu.core.DefaultDescriptable;

public class DefaultScript extends DefaultDescriptable implements Script {
	
	public static final String STANDARD_TRANSLITERATION = "standard";
	
	private Map presentations = new HashMap();
	private Map transliterations = new HashMap();
	
	private Locale locale;
	
	private String title;

	private String example;
	private String exampleTranslit;
	
	private String defaultEditablePresentation;
	private String defaultViewOnlyPresentation;
	
	private String defaultFont;
	
	private boolean complex = false;

	public Object convert(Object in, String inTranslit, String outTranslit) {
		if(outTranslit != null && outTranslit.equals(inTranslit)){
			return in;
		}
		Transliteration inConv = getTransliteration(inTranslit);
		Transliteration outConv = getTransliteration(outTranslit);
		Object standard = inConv.toStandard(in);
		Object out = outConv.toForeign(standard);
		return out;
	}
	
	public Presentation getPresentation(String id){
		if(id == null){
			StringPresentation stringPresentation = new StringPresentation(STANDARD_TRANSLITERATION, "Standard Transliteration");
			stringPresentation.setScript(this);
			if(defaultFont != null)
				stringPresentation.setFont(defaultFont);
			stringPresentation.setOutTransliteration(STANDARD_TRANSLITERATION);
			return stringPresentation;
		}else if (getPresentations().get(id) == null) {
			StringPresentation stringPresentation = new StringPresentation(id, id+" Transliteration");
			stringPresentation.setScript(this);
			if(defaultFont != null)
				stringPresentation.setFont(defaultFont);
			stringPresentation.setOutTransliteration(id);
			addPresentation(stringPresentation);
		}
		return (Presentation) getPresentations().get(id);
	}
	
	public Transliteration getTransliteration(String id){
		if(id == null || id.equals("") || id.equals(STANDARD_TRANSLITERATION)){
			return identityTransliteration;
		}
		return (Transliteration) getTransliterations().get(id);
	}
	
	public void addPresentation(Presentation p){
		getPresentations().put(p.getId(), p);
	}
	
	public void addTransliteration(Transliteration c){
		getTransliterations().put(c.getId(), c);
	}
	
	public void setLocaleName(String localeName){
		Locale locale = convertToLocale(localeName);
		setLocale(locale);
	}

	public static Locale convertToLocale(String localeName) {
		String language = "";
		String country = "";
		String variant = "";
		int i = localeName.indexOf('_');
		if(i<0){
			language = localeName;
		}else{
			language = localeName.substring(0,i);
			int j = localeName.indexOf('_', i+1);
			if(j<0){
				country = localeName.substring(i+1);
			}else{
				country = localeName.substring(i+1,j);
				variant = localeName.substring(j+1);
			}
		}
		Locale locale = new Locale(language, country, variant);
		return locale;
	}
	
	public void setId(String string) {
		super.setId(string);
		if(locale == null)
			setLocaleName(string);
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Map getTransliterations() {
		return transliterations;
	}

	public void setTransliterations(Map transliterations) {
		this.transliterations = transliterations;
	}

	public Map getPresentations() {
		return presentations;
	}

	public void setPresentations(Map presentations) {
		this.presentations = presentations;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getExampleTranslit() {
		return exampleTranslit;
	}

	public void setExampleTranslit(String exampleTranslit) {
		this.exampleTranslit = exampleTranslit;
	}

	public String getDefaultEditablePresentation() {
		return defaultEditablePresentation;
	}

	public void setDefaultEditablePresentation(String defaultEditablePresentation) {
		this.defaultEditablePresentation = defaultEditablePresentation;
	}

	public String getDefaultViewOnlyPresentation() {
		return defaultViewOnlyPresentation;
	}

	public void setDefaultViewOnlyPresentation(String defaultViewOnlyPresentation) {
		this.defaultViewOnlyPresentation = defaultViewOnlyPresentation;
	}
	
	public Transliteration identityTransliteration = new IdentityTransliteration(this, STANDARD_TRANSLITERATION);
	
	public static class IdentityTransliteration extends DefaultDescriptable implements Transliteration{
		
		private Script script;
		
		public IdentityTransliteration(){
			super();
		}
		
		public IdentityTransliteration(Script script) {
			super();
			this.script = script;
		}

		public IdentityTransliteration(Script script, String id, String name) {
			super(id, name);
			this.script = script;
		}

		public IdentityTransliteration(Script script, String id) {
			this(script, id, id+" Transliteration");
		}

		public Object toStandard(Object foreign) {
			return foreign;
		}

		public Object toForeign(Object standard) {
			return standard;
		}

		public Script getScript() {
			return script;
		}

		public void setScript(Script script) {
			this.script = script;
		}
		
	}

	public String getDefaultFont() {
		return defaultFont;
	}

	public void setDefaultFont(String defaultFont) {
		this.defaultFont = defaultFont;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isComplex() {
		return complex;
	}

	public void setComplex(boolean complex) {
		this.complex = complex;
	}

}
