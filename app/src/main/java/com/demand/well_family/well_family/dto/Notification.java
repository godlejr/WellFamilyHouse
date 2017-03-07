package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-03-07.
 */

public class Notification {
    private int id;
    private int user_id;
    private int receive_category_id;
    private int receiver_id;
    private String content_name;
    private int intent_flag;
    private int intent_id;
    private int behavior_id;
    private int checked;
    private String created_at;
    private String updated_at;


    public Notification(int id, int user_id, int receive_category_id, int receiver_id, String content_name, int intent_flag, int intent_id, int behavior_id, int checked, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.receive_category_id = receive_category_id;
        this.receiver_id = receiver_id;
        this.content_name = content_name;
        this.intent_flag = intent_flag;
        this.intent_id = intent_id;
        this.behavior_id = behavior_id;
        this.checked = checked;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public int getReceive_category_id() {
        return receive_category_id;
    }

    public void setReceive_category_id(int receive_category_id) {
        this.receive_category_id = receive_category_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getContent_name() {
        return content_name;
    }

    public void setContent_name(String content_name) {
        this.content_name = content_name;
    }

    public int getIntent_flag() {
        return intent_flag;
    }

    public void setIntent_flag(int intent_flag) {
        this.intent_flag = intent_flag;
    }

    public int getIntent_id() {
        return intent_id;
    }

    public void setIntent_id(int intent_id) {
        this.intent_id = intent_id;
    }

    public int getBehavior_id() {
        return behavior_id;
    }

    public void setBehavior_id(int behavior_id) {
        this.behavior_id = behavior_id;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
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
        this.updated_at = updated_at;
    }
}
