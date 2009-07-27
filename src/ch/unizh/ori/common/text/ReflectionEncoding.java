/*
 * Created on 08.03.2004
 */
package ch.unizh.ori.common.text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author pht
 */
public class ReflectionEncoding extends DefaultEncoding {
	
	private String convertName, createName, className;
	private Method convertMethod, createMethod;
	private Class clazz;
	
	private boolean inited = false;

	/* (non-Javadoc)
	 * @see ch.unizh.ori.common.text.Encoding#convert(ch.unizh.ori.common.text.Text)
	 */
	public Object convert(Text text) {
		if(!(text instanceof StringText)){
			return null;
		}
		init();
		return invoke(((StringText)text).getText(), convertMethod);
	}

	/* (non-Javadoc)
	 * @see ch.unizh.ori.common.text.Encoding#create(java.lang.Object)
	 */
	public Text create(Object param) {
		String s = null;
		if(param != null){
			s = param.toString();
		}
		return new StringText(getScript(), invoke(s, createMethod));
	}

	private String invoke(String text, Method method) {
		String[] args = { text };
		String ret = null;
		try {
			Object o = method.invoke(null, args);
			if(o != null){
				ret = o.toString();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private void init(){
		if(inited) return;
		try {
			clazz = Class.forName(getClassName());
			Class[] param = { String.class };
			createMethod = clazz.getMethod(createName, param);
			convertMethod = clazz.getMethod(convertName, param);
			inited = true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return getId()+"/"+getName()+": "+className+"/" + createName+"/"+convertName;
	}


	/**
	 * @return
	 */
	public String getConvertName() {
		return convertName;
	}

	/**
	 * @return
	 */
	public String getCreateName() {
		return createName;
	}

	/**
	 * @param string
	 */
	public void setConvertName(String string) {
		inited = false;
		convertName = string;
	}

	/**
	 * @param string
	 */
	public void setCreateName(String string) {
		inited = false;
		createName = string;
	}

	/**
	 * @return
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param string
	 */
	public void setClassName(String string) {
		inited = false;
		className = string;
	}

}
