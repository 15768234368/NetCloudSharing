package com.example.netcloudsharing.tool;

import java.util.ArrayList;
import java.util.List;

/*
    用户数据库操作类
    实现用户的CRUD操作
 */
public class UserDao extends DbOpenHelper {
    /**
     * 查询所有用户的信息 R
     *
     * @return List<Userinfo>表单
     */
    public List<Userinfo> getAllUserList() {
        List<Userinfo> list = new ArrayList<>();
        try {
            getConnection();    //取得连接信息
            String sql = "select * from userinfo";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                String uid = rs.getString(0);
                String pic = rs.getString(1);
                String name = rs.getString(2);
                String account = rs.getString(3);
                String password = rs.getString(4);
                String gender = rs.getString(5);
                String birthday = rs.getString(6);
                String constellation = rs.getString(7);
                String nowLive = rs.getString(8);
                String birthplace = rs.getString(9);
                String email = rs.getString(10);
                String info = rs.getString(11);

                list.add(new Userinfo(uid, pic, name, account, password, gender, birthday, constellation, nowLive, birthplace, email, info));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return list;
    }

    /**
     * 按用户名和密码查询用户信息 R
     *
     * @param account  用户名
     * @param password 密码
     * @return Userinfo实例
     */
    public Userinfo getUserByUnameAndUpass(String account, String password) {
        Userinfo item = null;
        try {
            getConnection();    //取得连接信息
            String sql = "select * from userinfo where account = ? and password = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, account);
            pStmt.setString(2, password);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                String uid = rs.getString(1);
                String pic = rs.getString(2);
                String name = rs.getString(3);
                String gender = rs.getString(6);
                String birthday = rs.getString(7);
                String constellation = rs.getString(8);
                String nowLive = rs.getString(9);
                String birthplace = rs.getString(10);
                String email = rs.getString(11);
                String info = rs.getString(12);
                item = new Userinfo(uid, pic, name, account, password, gender, birthday, constellation, nowLive, birthplace, email, info);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return item;
    }

    public Userinfo getUserByUid(String uid) {
        Userinfo item = null;
        try {
            getConnection();    //取得连接信息
            String sql = "select * from userinfo where uid = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, uid);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                String pic = rs.getString(2);
                String name = rs.getString(3);
                String account = rs.getString(4);
                String password = rs.getString(5);
                String gender = rs.getString(6);
                String birthday = rs.getString(7);
                String constellation = rs.getString(8);
                String nowLive = rs.getString(9);
                String birthplace = rs.getString(10);
                String email = rs.getString(11);
                String info = rs.getString(12);
                item = new Userinfo(uid, pic, name, account, password, gender, birthday, constellation, nowLive, birthplace, email, info);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return item;
    }

    public boolean queryIsRegister(String account) {
        try {
            getConnection();
            String sql = "select * from userinfo where account = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, account);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return false;
    }

    /**
     * 添加用户信息 C
     *
     * @param item 要添加的用户
     * @return int 影响的行数
     */
    public int addUser(Userinfo item) {
        int iRow = 0;
        try {
            getConnection();    //取得连接信息
            String sql = "insert into userinfo(uid,account,password) values(?,?,?)";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getUid());
            pStmt.setString(2, item.getAccount());
            pStmt.setString(3, item.getPassword());
            iRow = pStmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return iRow;
    }

    /**
     * 修改用户信息 U
     *
     * @param item 要修改的用户
     * @return int 影响的行数
     */
    public int editUser(Userinfo item) {
        int iRow = 0;
        try {
            getConnection();    //取得连接信息
            String sql = "update userinfo set pic = ?,name = ?, birthday = ?, constellation = ?, nowLive = ?, birthplace = ?, email = ?, info = ?, gender = ? where uid =?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getPic());
            pStmt.setString(2, item.getName());
            pStmt.setString(3, item.getBirthday());
            pStmt.setString(4, item.getConstellation());
            pStmt.setString(5, item.getNowLive());
            pStmt.setString(6, item.getBirthplace());
            pStmt.setString(7, item.getEmail());
            pStmt.setString(8, item.getInfo());
            pStmt.setString(9, item.getGender());
            pStmt.setString(10, item.getUid());
            iRow = pStmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return iRow;
    }

    /**
     * 根据id 删除用户信息 D
     *
     * @param id 要删除的用户id
     * @return int 影响的行数
     */
    public int delUser(int id) {
        int iRow = 0;
        try {
            getConnection();    //取得连接信息
            String sql = "delete from userinfo where id = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            iRow = pStmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeAll();
        }
        return iRow;
    }
}
