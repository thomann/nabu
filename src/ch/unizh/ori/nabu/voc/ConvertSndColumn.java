/*
 * Created on 05.05.2004
 */
package ch.unizh.ori.nabu.voc;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pht
 */
public class ConvertSndColumn extends SndColumn {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ConvertSndColumn.class);
	
	ConvertSupport convertSupport = new ConvertSupport();
	
	public Object map(String[] arr) {
		Object ret = super.map(arr);
		try {
			Object value = convertSupport.map(ret);
			return value;
		} catch (InvocationTargetException e) {
			log.error("ConvertStringColumn.map(): InvocationTargetException. Cause", e.getCause());
//			e.getCause().printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public String getClassName() {
		return convertSupport.getClassName();
	}
	public String getMethodName() {
		return convertSupport.getMethodName();
	}
	public void setClassName(String string) throws SecurityException,
			NoSuchMethodException {
		convertSupport.setClassName(string);
	}
	public void setMethodName(String string) throws SecurityException,
			NoSuchMethodException {
		convertSupport.setMethodName(string);
	}
	

//	private String className;
//	private Class clazz;
//	private String methodName;
//	
//
//	/**
//	 * @return
//	 */
//	public String getClassName() {
//		return className;
//	}
//
//	/**
//	 * @return
//	 */
//	public String getMethodName() {
//		return methodName;
//	}
//
//	/**
//	 * @param string
//	 */
//	public void setClassName(String string) {
//		className = string;
//		clazz = null;
//		if(className != null){
//			try {
//				clazz = Class.forName(className);
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * @param string
//	 */
//	public void setMethodName(String string) {
//		methodName = string;
//	}

}
