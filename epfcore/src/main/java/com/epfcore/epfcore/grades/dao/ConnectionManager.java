package com.epfcore.epfcore.grades.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String DB_URL      = "jdbc:mysql://localhost:3306/epfcore"
                                            + "?useSSL=false&serverTimezone=Europe/Paris"
                                            + "&characterEncoding=UTF-8";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC introuvable. "
                + "Ajoutez mysql-connector-j ou postgresql dans pom.xml", e);
        }
    }

    private ConnectionManager() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}