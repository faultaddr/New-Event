package com.example.panda.newevent.database;

import cn.bmob.v3.BmobUser;

/**
 * Created by panyunyi on 2017/2/15.
 */

public class MyUser extends BmobUser {
    private String username;
    private String password;
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }




}
