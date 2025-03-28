package org.example.RPS.db;

import java.sql.*;
import java.util.List;

public class JdbcUtils {
    private static final String USER_NAME = "root"; // Set the SQL Username
    private static final String PASSWORD = "sirlolipop13"; // Set the SQL Password
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/userdbs";
    // jdbc:mysql://localhost:3308/newsSystem?allowPublicKeyRetrieval=true&useSSL=false&characterEncoding=utf-8
    private Connection connection;
    private PreparedStatement ps;
    private ResultSet resultSet;
    private int highScore;

    public JdbcUtils() {
        try {
            Class.forName(DRIVER);
//            System.out.println("DB connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get Connection to SQL
    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Count the amount of data in SQL according to the where statements
    public Integer count(String countSql, List<String> params) throws SQLException {
        ps = connection.prepareStatement(countSql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
        }
        resultSet = ps.executeQuery();
        int total = 0;
        if (resultSet.next()) {
            total = resultSet.getInt(1);
        }
        return total;
    }

    // Get sql command and do (Insert or Update) to SQL
    public int insert(String sql, List<String> params) throws SQLException {
        ps = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
        }
        return ps.executeUpdate();
    }

    // get High Score from SQL
    public int getHighScorefromSQL(String sql, List<String> params) throws SQLException {
        int highScore = 10;
        ps = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
        }
        resultSet = ps.executeQuery();
        while (resultSet.next()) {
            highScore = resultSet.getInt("highscore");
        }
        return highScore;
    }
}
