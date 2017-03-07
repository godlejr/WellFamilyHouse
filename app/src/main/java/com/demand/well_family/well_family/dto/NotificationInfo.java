package com.demand.well_family.well_family.dto;

/**
 * Created by Dev-0 on 2017-03-07.
 */

public class NotificationInfo {
    private int id;
    private String avatar;
    private String name;
    private String content;
    private String title;
    private String photo;

    public NotificationInfo(int id, String avatar, String name, String content, String title, String photo) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.content = content;
        this.title = title;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
