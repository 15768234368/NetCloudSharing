package com.example.netcloudsharing.tool;
/*
    用户信息实体类
 */
import java.io.Serializable;
public class Userinfo implements Serializable{
    private String uid;
    private String pic;
    private String name;
    private String account;
    private String password;
    private String gender;
    private String birthday;
    private String constellation;
    private String nowLive;
    private String birthplace;
    private String email;
    private String info;
    public Userinfo() {
    }

    public Userinfo(String uid, String pic, String name, String account, String password, String gender, String birthday, String constellation, String nowLive, String birthplace, String email, String info) {
        this.uid = uid;
        this.pic = pic;
        this.name = name;
        this.account = account;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;
        this.constellation = constellation;
        this.nowLive = nowLive;
        this.birthplace = birthplace;
        this.email = email;
        this.info = info;
    }

    public Userinfo(String uid, String account, String password) {
        this.uid = uid;
        this.account = account;
        this.password = password;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public void setNowLive(String nowLive) {
        this.nowLive = nowLive;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUid() {
        return uid;
    }

    public String getPic() {
        return pic;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getConstellation() {
        return constellation;
    }

    public String getNowLive() {
        return nowLive;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public String getEmail() {
        return email;
    }

    public String getInfo() {
        return info;
    }
}

