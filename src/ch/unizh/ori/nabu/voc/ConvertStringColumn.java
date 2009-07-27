/*
 * Created on 05.05.2004
 */
package ch.unizh.ori.nabu.voc;

import java.lang.reflect.InvocationTargetException;

/**
 * @author pht
 */
public class ConvertStringColumn extends StringColumn {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ConvertStringColumn.class);
	
	private ConvertSupport convertSupport = new ConvertSupport();
	
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

//	public Object map(String[] arr) {
//		Object ret = super.map(arr);
//		Expression ex = new Expression(clazz, methodName, new Object[]{ ret });
//		try {
//			Object value = ex.getValue();
//			return value;
//		} catch (InvocationTargetException e) {
//			System.err.println("ConvertStringColumn.map(): InvocationTargetException. Cause");
//			e.getCause().printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ret;
//	}
//
//	private String className;
//	private Class clazz;
//	private String methodName;
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
