/*
 * Created on 25.11.2004
 *
 */
package ch.unizh.ori.nabu.voc;

import java.beans.Expression;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author pht
 *  
 */
public class ConvertSupport implements Serializable {

	private String className;

	private Class clazz;

	private String methodName;
	
	private transient Method method;

	public Object map(Object obj) throws InvocationTargetException, Exception {
		Expression ex = new Expression(clazz, methodName, new Object[] { obj });
		Object value = ex.getValue();
		return value;
	}

	/**
	 * @return
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param string
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void setClassName(String string) throws SecurityException, NoSuchMethodException {
		className = string;
		clazz = null;
		if (className != null) {
			try {
				clazz = Class.forName(className);
				updateMethod();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param string
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void setMethodName(String string) throws SecurityException, NoSuchMethodException {
		methodName = string;
		updateMethod();
	}
	
	private void updateMethod() throws SecurityException, NoSuchMethodException{
		if(false && clazz != null && methodName != null){
			method = clazz.getMethod(methodName, new Class[]{ Object.class });
		}
	}

}