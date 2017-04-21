package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-04-05.
 */

public class FamilyInfoForFamilyJoin {
    private int id;
    private String name;
    private String content;
    private String avatar;
    private int user_id;
    private int join_flag;
    private String created_at; // family join created_at

    private int position;

    public FamilyInfoForFamilyJoin(int id, String name, String content, String avatar, int user_id, int join_flag, String created_at) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.avatar = avatar;
        this.user_id = user_id;
        this.join_flag = join_flag;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getJoin_flag() {
        return join_flag;
    }

    public void setJoin_flag(int join_flag) {
        this.join_flag = join_flag;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
