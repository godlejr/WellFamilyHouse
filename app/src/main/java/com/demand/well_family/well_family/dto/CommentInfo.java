package com.demand.well_family.well_family.dto;

/**
 * Created by Dev-0 on 2017-01-24.
 */

public class CommentInfo {

    private int comment_id;
    private int user_id;
    private String user_name;
    private String avatar;
    private String content;
    private String created_at;

    public CommentInfo(int comment_id, int user_id, String user_name, String avatar, String content, String created_at) {
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.avatar = avatar;
        this.content = content;
        this.created_at = created_at;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
