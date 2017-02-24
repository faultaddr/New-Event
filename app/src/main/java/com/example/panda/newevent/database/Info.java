package com.example.panda.newevent.database;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by panda on 2016/11/29.
 */

public class Info extends BmobObject{
    private String Title;
    private String Content;
    private String User;
    private String Emergency;

    public BmobDate getDate() {
        return Date;
    }

    public void setDate(BmobDate date) {
        Date = date;
    }

    private BmobDate Date;
    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    private String Time;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }


    public String getEmergency() {
        return Emergency;
    }

    public void setEmergency(String emergency) {
        Emergency = emergency;
    }
}
