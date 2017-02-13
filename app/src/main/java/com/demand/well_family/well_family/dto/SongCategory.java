package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-01-31.
 */

public class SongCategory {
    private int id;
    private String name;
    private String avatar;
    private String created_at;

    public SongCategory(int id, String name, String avatar, String created_at) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
