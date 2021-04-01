package com.cognixia.jump.jdbc.ems;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
	
	private static Connection connection = null;
	
	private static final String URL = "jdbc:mysql://localhost:3306/ems";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	
	private static void makeConnection() {
		
		// This is one way of making a connection by using a properties file
//		Properties props = new Properties();
//		
//		try {
//			props.load(new FileInputStream("./resources/config.properties"));
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		
//		String url = props.getProperty("url");
//		String username = props.getProperty("username");
//		String password = props.getProperty("password");
		
		try {
//			connection = DriverManager.getConnection(url, username, password);
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Connection getConnection() {
		if (connection == null) {
			makeConnection();
		}
		return connection;
	}
	
}
