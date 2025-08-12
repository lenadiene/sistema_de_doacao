package com.example.doacoes.model.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static ConnectionManager instance;
    private final String url;
    private final String user;
    private final String pass;

    private ConnectionManager() {
        
        this.url = "jdbc:mysql://localhost:3306/doacao_db?useSSL=false&serverTimezone=UTC";
        this.user = "root";
        this.pass = "root";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) instance = new ConnectionManager();
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}