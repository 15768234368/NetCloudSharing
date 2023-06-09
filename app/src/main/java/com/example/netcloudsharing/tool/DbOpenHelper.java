package com.example.netcloudsharing.tool;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/*
    直接连接数据库的辅助工具类
 */
public class DbOpenHelper {
    final static private String CLS = "com.mysql.jdbc.Driver";
    final static private String URL = "jdbc:mysql://8.134.169.27:3306/netsharingdb?characterEncoding=utf-8";
    final static private String USER = "zzc";
    final static private String PWD = "112211";
    private static final String TAG = "DbOpenHelper";
    public static Connection conn;  //连接对象
    public static Statement stmt;   //命令集
    public static PreparedStatement pStmt;    //预编译命令集
    public static ResultSet rs; //结果集

    //取得连接的方法
    public static void getConnection() {
        try {
            Class.forName(CLS);
            conn = DriverManager.getConnection(URL, USER, PWD);
            Log.d(TAG, "getConnection: " + "success");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //关闭连接
    public static void closeAll() {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pStmt != null) {
                pStmt.close();
                pStmt = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
