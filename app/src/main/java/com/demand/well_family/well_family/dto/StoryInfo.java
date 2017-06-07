package com.demand.well_family.well_family.dto;

import java.io.Serializable;

/**
 * Created by Dev-0 on 2017-01-20.
 */

public class StoryInfo implements Serializable {
    private int user_id;
    private String name;
    private String avatar;
    private int story_id;
    private String created_at;
    private String content;
    private Boolean first_checked; //  select first check
    private Boolean isChecked; // like cheched
    private int position;

    public StoryInfo() {
    }

    public StoryInfo(int user_id, String name, String avatar, int story_id, String created_at, String content) {
        super();
        this.user_id = user_id;
        this.name = name;
        this.avatar = avatar;
        this.story_id = story_id;
        this.created_at = created_at;
        this.content = content;
        this.first_checked = false;
        this.isChecked = false;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public int getStory_id() {
        return story_id;
    }

    public void setStory_id(int story_id) {
        this.story_id = story_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getFirst_checked() {
        return first_checked;
    }

    public void setFirst_checked(Boolean first_checked) {
        this.first_checked = first_checked;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
