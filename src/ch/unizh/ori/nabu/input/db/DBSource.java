/*
 * Created on 25.04.2004
 */
package ch.unizh.ori.nabu.input.db;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import javax.sql.DataSource;

import ch.unizh.ori.nabu.voc.Source;

/**
 * @author pht
 */
public abstract class DBSource extends Source {

	private String tableName;
	private String lessonFieldName;
	private String sql;
	
	private MessageFormat loadSql;

	public List readLections(URL base) throws Exception {
		List ret = new ArrayList();
		
		DataSource ds = getDataSource();
		Connection conn = ds.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(getSql());
		int i=0;
		while(rs.next()){
			DBFieldStream fs = new DBFieldStream();
			String lesson = rs.getString(1);
			int count = rs.getInt(2);
			fs.setId(lesson);
			fs.setName(createLessonName(i++,lesson));
			fs.setSource(getDataSource());
			fs.setSql(formatLoadSQL(lesson));
			fs.setCount(count);
			ret.add(fs);
		}
		
		return ret;
	}
	
	protected abstract DataSource getDataSource() throws Exception;

	/**
	 * @return
	 */
	public String getSql() {
		if(sql == null){
			sql = "SELECT DISTINCT "+lessonFieldName+", COUNT(*) FROM "+tableName
			+" GROUP BY "+lessonFieldName+";";
		}
		return sql;
	}

	/**
	 * @param string
	 */
	public void setLesson(String string) {
		lessonFieldName = string;
	}

	/**
	 * @param string
	 */
	public void setSql(String string) {
		sql = string;
	}

	/**
	 * @param string
	 */
	public void setTable(String string) {
		tableName = string;
	}

	/**
	 * @return
	 */
	protected MessageFormat getLoadSql0() {
		if(loadSql == null){
			setLoadSql("SELECT * FROM {0} WHERE {1}=\'\'{2}\'\';");
		}
		return loadSql;
	}
	
	protected String formatLoadSQL(String lesson){
		MessageFormat mf = getLoadSql0();
		String[] params = new String[]{tableName,lessonFieldName,lesson};
		String ret = mf.format(params);
		return ret;
	}

	/**
	 * @param format
	 */
	public void setLoadSql(String format) {
		loadSql = new MessageFormat(format);
	}

}
