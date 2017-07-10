package com.demand.well_family.well_family.dto;

import java.io.Serializable;

/**
 * Created by ㅇㅇ on 2017-06-23.
 */

public class ExerciseCategory implements Serializable {
    private int id;
    private String name;
    private String video;
    private String created_at;
    private String updated_at;

    public ExerciseCategory() {
    }

    public ExerciseCategory(int id, String name, String video, String created_at, String updated_at) {


        this.id = id;
        this.name = name;
        this.video = video;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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
