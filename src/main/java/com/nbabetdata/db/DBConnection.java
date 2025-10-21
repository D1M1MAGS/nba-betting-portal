package com.nbabetdata.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Path to your SQLite database file
    private static final String DB_URL = "jdbc:sqlite:db/nba_portal.db";

    /**
     * Get a connection to the SQLite database
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
