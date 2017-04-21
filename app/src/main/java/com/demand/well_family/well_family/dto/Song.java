package com.demand.well_family.well_family.dto;

import java.io.Serializable;

/**
 * Created by Dev-0 on 2017-01-31.
 */

public class Song implements Serializable {
    private int id;
    private String name;
    private String ext;
    private String title;
    private String singer;
    private String avatar;
    private int category_id;
    private String created_at;

    public Song() {
    }

    public Song(int id, String name, String ext, String title, String singer, String avatar, int category_id, String created_at) {
        this.id = id;
        this.name = name;
        this.ext = ext;
        this.title = title;
        this.singer = singer;
        this.avatar = avatar;
        this.category_id = category_id;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
