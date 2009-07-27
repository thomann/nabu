/*
 * Created on 03.03.2006
 *
 */
package ch.unizh.ori.common.text;

import java.lang.reflect.Method;

public class ConvertStringPresentation extends StringPresentation {
	
	private Class clazz;
	private String methodName;
	private transient Method method;

	public String getOutText(Object value, String enc) {
		try {
			return (String)getMethod().invoke(null, new String[]{super.getOutText(value, enc)});
		} catch (Exception ex) {
			throw new RuntimeException("Problem invoking: value="+value, ex);
		}
	}
	
	public void setClassName(String classname){
		try{
			clazz = Class.forName(classname);
		}catch (ClassNotFoundException ex){
			throw new RuntimeException("Problem with class: "+classname, ex);
		}
	}
	
	public void setMethodName(String methodname){
		this.methodName = methodname;
	}

	private Method getMethod() {
		if (method == null) {
			try{
				method = clazz.getMethod(methodName, new Class[]{String.class});
			}catch(Exception ex){
				throw new RuntimeException("Problem with method: "+methodName+" in class "+clazz, ex);
			}
		}

		return method;
	}

}
