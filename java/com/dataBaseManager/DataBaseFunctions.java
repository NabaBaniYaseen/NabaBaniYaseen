package com.dataBaseManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataBaseFunctions {
	/*
	 * This class contain attribute for configuration database and to link data base
	 * with java code
	 */

	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost/robots_db";

	// Database Credentials
	static final String USER = "root";
	static final String PASS = "123456";

	private ObjectMapper objectMapper = getDefaultObjectMapper();

	/* this method used to get connection to data base */
	public static Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
		return connection;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public ObjectMapper getDefaultObjectMapper() {
		ObjectMapper defaultObjectMapper = new ObjectMapper();
		return defaultObjectMapper;
	}

	public boolean testConnection() throws SQLException {
		if (getConnection() != null)
			return true;
		return false;
	}

	public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
		return getConnection().prepareStatement(sql);
	}
}
