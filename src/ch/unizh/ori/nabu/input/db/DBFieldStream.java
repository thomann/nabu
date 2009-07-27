/*
 * Created on 25.04.2004
 */
package ch.unizh.ori.nabu.input.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.sql.DataSource;

import ch.unizh.ori.nabu.voc.AbstractFieldStream;

/**
 * @author pht
 */
public class DBFieldStream extends AbstractFieldStream {
	
	private String sql;
	private DataSource source;
	private int count;

	public Object start() throws Exception {
		Connection conn = source.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}

	public String[] next(Object param) throws Exception {
		ResultSet rs = (ResultSet) param;
		if(!rs.next()){
			return null;
		}
		ResultSetMetaData meta = rs.getMetaData();
		String[] ret = new String[meta.getColumnCount()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = rs.getString(i+1);
		}
		return ret;
	}

	public void stop(Object param) throws Exception {
		ResultSet rs = (ResultSet) param;
		Connection conn = null;
		Statement st = null;
		try{
			st = rs.getStatement();
			if(st != null){
				conn = st.getConnection();
			}
		}catch (Exception e) {
			System.err.println("DBFieldStream.stop(): "+e.getMessage());
		}
		rs.close();
		if(st != null){
			st.close();
		}
		if(conn != null){
			conn.close();
		}
	}

	/**
	 * @return
	 */
	public DataSource getSource() {
		return source;
	}

	/**
	 * @return
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param source
	 */
	public void setSource(DataSource source) {
		this.source = source;
	}

	/**
	 * @param string
	 */
	public void setSql(String string) {
		sql = string;
	}

	/**
	 * @return
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * @param i
	 */
	public void setCount(int i) {
		count = i;
	}

}
