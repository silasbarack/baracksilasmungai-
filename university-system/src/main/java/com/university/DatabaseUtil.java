package com.university;
import java.sql.*;
public final class DatabaseUtil {
    private DatabaseUtil(){}
    public static Connection getConnection() throws SQLException {
        String url=System.getenv().getOrDefault("UNIVERSITY_DB_URL","jdbc:mysql://localhost:3306/university_db");
        String user=System.getenv().getOrDefault("UNIVERSITY_DB_USER","root");
        String password=System.getenv("UNIVERSITY_DB_PASSWORD");
        if(password==null) throw new SQLException("UNIVERSITY_DB_PASSWORD environment variable is required");
        return DriverManager.getConnection(url,user,password);
    }
}