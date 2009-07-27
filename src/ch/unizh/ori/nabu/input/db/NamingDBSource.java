/*
 * Created on 25.04.2004
 */
package ch.unizh.ori.nabu.input.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author pht
 */
public class NamingDBSource extends DBSource {
	
	private DataSource dataSource;
	private String res = "jdbc/NabuDB";

	protected DataSource getDataSource() throws Exception {
		if(dataSource == null){
			try{
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				dataSource = (DataSource) envCtx.lookup(getRes());
			}catch (Exception e) {
			}
		}
		return dataSource;
	}

	/**
	 * @return
	 */
	public String getRes() {
		return res;
	}

	/**
	 * @param source
	 */
	public void setDataSource(DataSource source) {
		dataSource = source;
	}

	/**
	 * @param string
	 */
	public void setRes(String string) {
		res = string;
	}

}
