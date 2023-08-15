package com.example.netcloudsharing.Bean;

public class CommentBean {
    private String userImage;
    private String userName;
    private String msg;
    private String time;

    public CommentBean(String userImage, String userName, String msg, String time) {
        this.userImage = userImage;
        this.userName = userName;
        this.msg = msg;
        this.time = time;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }
}
