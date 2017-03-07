package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-03-07.
 */

public class Notification {
    private String content;
    private String date;
    private String avatar;

    public Notification(String content, String date, String avatar) {
        this.content = content;
        this.date = date;
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
