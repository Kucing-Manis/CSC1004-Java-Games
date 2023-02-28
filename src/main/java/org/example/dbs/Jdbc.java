package org.example.dbs;

import org.example.dbs.gui.Login;
import org.example.dbs.models.UserData;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jdbc {
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "sirlolipop13";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/newsSystem?allowPublicKeyRetrieval=true&useSSL=false&characterEncoding=utf-8";
    private Connection connection;
    private PreparedStatement pstmt;
    private ResultSet resultSet;

    public Jdbc() {
        try {
            Class.forName(DRIVER);
            System.out.println("Database connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Insert and Update
    public boolean updateByPreparedStatement(String sql, List<Object> params) throws SQLException {
        int resultLineNumber = -1; // 如果行号小于零说明更新失败 if line number <0 means failed
        pstmt = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
        }
        resultLineNumber = pstmt.executeUpdate();
        return resultLineNumber > 0;
    }

    // Get Data as Map of String,List
    public Map<String, Object> findSimpleResult(String sql, List<Object> params) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        pstmt = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i)); //这里要处理序号问题，所以第一个参数是i+1
            }
        }
        resultSet = pstmt.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData(); // 获取元数据,目的是获得行号
        int col = metaData.getColumnCount();
        while (resultSet.next()) {
            for (int i = 0; i < col; i++) {
                String colName = metaData.getColumnLabel(i + 1);
                Object colValue = resultSet.getObject(colName);
                if (colValue == null) {
                    colValue = "";
                }
                map.put(colName, colValue);
            }
        }
        return map;
    }

    //Get Data as Object
    public <T> T findSimpleRefResult(String sql, List<Object> params, Class<T> cls) throws Exception {
        T resultObject = null;
        pstmt = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
        }
        resultSet = pstmt.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int col = metaData.getColumnCount();
        while (resultSet.next()) {
            resultObject = cls.newInstance(); // 创建一个该类的一个新实例
            for (int i = 0; i < col; i++) {
                String colName = metaData.getColumnName(i + 1);
                Object colValue = resultSet.getObject(colName);
                if (colValue == null) {
                    colValue = "";
                }
                Field field = cls.getDeclaredField(colName);
                field.setAccessible(true);
                field.set(resultObject, colValue);
            }
        }
        return resultObject;
    }

    public void releaseConnection() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        Jdbc jdbc = new Jdbc();
        jdbc.getConnection();

        // 利用反射查询单条记录
        // use reflection to query single line
        String sql = "select * from `admin` where username = ?";
        List<Object> params = new ArrayList<>();
        params.add("石佳欢");
        try {
            UserData User = jdbc.findSimpleRefResult(sql, params, UserData.class);
            System.out.println(jdbc.findSimpleRefResult(sql, params, UserData.class));
            System.out.println(User.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
