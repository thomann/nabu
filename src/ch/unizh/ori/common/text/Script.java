/*
 * Created on 12.01.2004
 */
package ch.unizh.ori.common.text;

import java.util.Locale;
import java.util.Map;

import ch.unizh.ori.nabu.core.Descriptable;

/**
 * @author pht
 */
public interface Script extends Descriptable {
	
	public static final String DEFAULT = "default";
	public static final String UNICODE = "unicode";
	public static final String ASCII = "ascii";
	public static final String TeX = "TeX";

	public static final String ANY_TRANSLITERATION = "any";

	public Object convert(Object in, String inTranslit, String outTranslit);

	public Map getPresentations();
	public Presentation getPresentation(String id);
	
	public Locale getLocale();
	
	public boolean isComplex();
	
}
