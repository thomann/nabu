package ch.unizh.ori.common.text;

import java.lang.reflect.*;


public class ScriptUtilities {
	
	private static Method m;
	private static Field compose;
	
	private static boolean inited;

	public static String compose(String str){
		try {
			if(!inited){
				inited = true;
				Class norm = Class.forName("sun.text.Normalizer");
				Class mode = Class.forName("sun.text.Normalizer$Mode");
				Class[] params = {String.class, mode, Integer.TYPE};
				m = norm.getMethod("normalize", params);
				compose = norm.getField("COMPOSE");
			}
			if(m != null && compose != null){
				Object composeObject = compose.get(null);
				str = (String)m.invoke(null, new Object[]{ str, composeObject, new Integer(0) });
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return str;
	}

}
