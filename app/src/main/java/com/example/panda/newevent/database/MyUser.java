package com.example.panda.newevent.database;

import cn.bmob.v3.BmobUser;

/**
 * Created by panyunyi on 2017/2/15.
 */

public class MyUser extends BmobUser {
    private String userName;
    private String userPassWord;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassWord() {
        return userPassWord;
    }

    public void setUserPassWord(String userPassWord) {
        this.userPassWord = userPassWord;
    }
}
