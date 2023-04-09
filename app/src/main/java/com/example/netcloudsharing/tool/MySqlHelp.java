package com.example.netcloudsharing.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*
    直接连接数据库的辅助工具类
 */
public class MySqlHelp {
        public static int getUserSize(){
            final String CLS = "com.mysql.jdbc.Driver";
            final String URL = "jdbc:mysql://10.200.127.52:3306/netsharingdb";
            final String USER = "zzc";
            final String PWD = "112211";
            int count=0;
            try{
                Class.forName(CLS);
                Connection conn = DriverManager.getConnection(URL,USER,PWD);
                String sql ="select count(1) as sl from userinfo";
                Statement stmt = conn.createStatement();
                ResultSet rs =stmt.executeQuery(sql);
                while(rs.next()){
                    count = rs.getInt("sl");
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return count;
        }
}
