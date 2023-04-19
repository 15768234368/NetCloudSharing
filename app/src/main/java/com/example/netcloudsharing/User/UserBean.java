package com.example.netcloudsharing.User;

public class UserBean {
    private String user_name;
    private String user_id;

    public UserBean(String user_name, String user_id) {
        this.user_name = user_name;
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_id() {
        return user_id;
    }
}
