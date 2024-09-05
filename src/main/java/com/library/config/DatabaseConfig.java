// DatabaseConfig.java
package com.library.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
	
    private static final String URL = "jdbc:postgresql://localhost:5432/lms";
    private static final String USER = "postgres"; // Replace with your DB username
    private static final String PASSWORD = "mike"; // Replace with your DB password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}