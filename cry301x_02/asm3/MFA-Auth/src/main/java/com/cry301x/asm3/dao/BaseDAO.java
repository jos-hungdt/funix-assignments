package com.cry301x.asm3.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDAO {
    public final static String DB_NAME = "mfa_sys";
    public final static String ACCOUNT = "root";
    public final static String PASSWORD = "#Admin123";
    private final static String URL_PREFIX = "jdbc:mysql://localhost:3306/";
    private static Connection conn = null;

    protected static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        StringBuilder url = new StringBuilder(URL_PREFIX);
        url.append(DB_NAME);

        if (conn == null) {
            conn = DriverManager.getConnection(url.toString(), ACCOUNT, PASSWORD);
        }

        return conn;
    }
}
