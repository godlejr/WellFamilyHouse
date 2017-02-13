package com.demand.well_family.well_family.dto;

/**
 * Created by Dev-0 on 2017-02-13.
 */

public class Story {
    private int id;
    private int user_id;
    private int family_id;
    private String content;
    private String created_at;

    public Story(int id, int user_id, int family_id, String content,String created_at) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.family_id = family_id;
        this.content = content;
        this.created_at =created_at;
    }


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFamily_id() {
        return family_id;
    }

    public void setFamily_id(int family_id) {
        this.family_id = family_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
