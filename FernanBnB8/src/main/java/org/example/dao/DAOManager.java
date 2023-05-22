package org.example.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOManager {

    Connection conn;
    final String URL;
    final String USER;
    final String PASS;
    static DAOManager singlenton;


    public DAOManager(Properties p) {
        this.conn = null;
        URL = "jdbc:mysql://"+ p.getProperty("database");
        USER = p.getProperty("dbuser");
        PASS = p.getProperty("dbpass");
    }

    public static DAOManager getSinglentonInstance(Properties p){
        if (singlenton == null) return new DAOManager(p);
        return null;
    }

    public Connection getConn() {
        return conn;
    }

    public void open() throws Exception{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL,USER,PASS);
        } catch (Exception e){
            throw e;
        }
    }

    public void close() throws SQLException{
        try {
            if (this.conn != null) this.conn.close();
        } catch (Exception e){
            throw e;
        }
    }

}
