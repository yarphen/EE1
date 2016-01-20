package com.sheremet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBUtils {
	public static final String DRIVERCLASS = "com.mysql.jdbc.Driver";
	public static final String PROTOCOL = "jdbc:mysql:";
	private Connection connection;
	public DBUtils(String host, String dbname, String user, String pass) {
		try{	
			Class.forName (DRIVERCLASS);
			connection = DriverManager.getConnection(PROTOCOL+"//"+host+"/"+dbname,user,pass);          
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public ResultSet queryNoParams(String query) throws SQLException {
		Statement st = connection.createStatement();
		st.executeQuery(query);
		return st.getResultSet();
	}
	public boolean execute(String query) throws SQLException {
		Statement st = connection.createStatement();
		return st.execute(query);
	}
}
