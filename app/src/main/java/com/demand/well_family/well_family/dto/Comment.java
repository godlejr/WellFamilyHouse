package com.demand.well_family.well_family.dto;

/**
 * Created by Dev-0 on 2017-02-13.
 */

public class Comment {

    private int id;
    private int user_id;
    private int story_id;
    private String content;
    private int deleted;
    private String created_at;
    private String updated_at;

    private int position;   // list position

    public Comment() {
    }

    public Comment(int id, int user_id, int story_id, String content, int deleted, String created_at,
                   String updated_at) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.story_id = story_id;
        this.content = content;
        this.deleted = deleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStory_id() {
        return story_id;
    }

    public void setStory_id(int story_id) {
        this.story_id = story_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        updated_at = updated_at;
    }

}
