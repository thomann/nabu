/*
 * Created on 07.05.2004
 */
package ch.unizh.ori.nabu.input.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * @author pht
 */
public class DummyDataSource implements DataSource {
	
	static{
		init();
	}
	
	public static void init(){
		try {
			// Load the JDBC driver
			String driverName = "org.gjt.mm.mysql.Driver";
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			// Could not find the driver
		}
	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public void setLoginTimeout(int seconds) throws SQLException {
	}

	public PrintWriter getLogWriter() throws SQLException {
		return new PrintWriter(System.err);
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	public Connection getConnection() throws SQLException {
		return getConnection("nabu_admin", "mypassword");
	}

	public Connection getConnection(String username, String password) throws SQLException {
		Connection connection = null;
		try {
			// Create a connection to the database
//			String serverName = "localhost";
//			String mydatabase = "mydatabase";
//			String url = "jdbc:mysql://" + serverName + "/" + mydatabase; // a JDBC url
			String url = "jdbc:mysql://localhost:3306/nabu?autoReconnect=true";
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

}
