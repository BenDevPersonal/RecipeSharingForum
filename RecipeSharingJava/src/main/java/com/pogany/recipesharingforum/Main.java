package com.pogany.recipesharingforum;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties prop = new Properties();
        try {
            prop.load(new FileReader("src/db_conn.property"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final String CONN_URL = prop.get("url").toString();
        final String DB_USER = prop.get("user").toString();
        final String DB_PASS = prop.get("pass").toString();

        try (Connection conn = DriverManager.getConnection(CONN_URL, DB_USER, DB_PASS)) {
            conn.setAutoCommit(false);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}