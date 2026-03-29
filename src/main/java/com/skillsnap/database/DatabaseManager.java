package com.skillsnap.database;

import java.sql.*;

public class DatabaseManager {

    // ── Connection details ────────────────────────────────────
    private static final String URL  = "jdbc:mysql://localhost:3306/skillsnap";
    private static final String USER = "root";
    private static final String PASS = "root";

    // ── Singleton instance ────────────────────────────────────
    private static DatabaseManager instance;
    private Connection connection;

    // ── Private constructor ───────────────────────────────────
    private DatabaseManager() {
        connect();
    }

    // ── Get the one and only instance ────────────────────────
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // ── Connect to database ───────────────────────────────────
    private void connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    // ── Get connection (used by all DAOs) ─────────────────────
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.out.println("Connection check failed: " + e.getMessage());
        }
        return connection;
    }

    // ── Close connection (call on app exit) ───────────────────
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database disconnected.");
            }
        } catch (SQLException e) {
            System.out.println("Disconnect failed: " + e.getMessage());
        }
    }
}
