package com.epfcore.epfcore.grades.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionManager — Gestionnaire de connexion JDBC vers DBeaver/MySQL.
 *
 * ⚠️  CONFIGURATION :
 *  Modifiez les constantes DB_URL, DB_USER, DB_PASSWORD
 *  pour correspondre à votre configuration DBeaver.
 *
 *  Exemple DBeaver / MySQL local :
 *    DB_URL = "jdbc:mysql://localhost:3306/epfcore?useSSL=false&serverTimezone=Europe/Paris&characterEncoding=UTF-8"
 *
 *  Exemple DBeaver / PostgreSQL :
 *    DB_URL = "jdbc:postgresql://localhost:5432/epfcore"
 *
 *  En production Spring Boot, préférez DataSource / application.properties.
 *  Cette classe est utile pour les DAO JDBC purs (NoteDao, etc.).
 */
public class ConnectionManager {

    // ── À MODIFIER selon votre base ───────────────────────────────────────────
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/epfcore"
                                            + "?useSSL=false&serverTimezone=Europe/Paris"
                                            + "&characterEncoding=UTF-8";
    private static final String DB_USER     = "root";      // votre login DBeaver
    private static final String DB_PASSWORD = "password";  // votre mot de passe DBeaver
    // ─────────────────────────────────────────────────────────────────────────

    static {
        try {
            // MySQL : com.mysql.cj.jdbc.Driver
            // PostgreSQL : org.postgresql.Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC introuvable. "
                + "Ajoutez mysql-connector-j ou postgresql dans pom.xml", e);
        }
    }

    private ConnectionManager() {}

    /**
     * Retourne une nouvelle connexion JDBC.
     * À appeler dans un try-with-resources pour garantir la fermeture.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}