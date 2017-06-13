package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-06-08.
 */

public class EnvironmentPhoto {
    private int id;
    private int fall_diagnosis_story_id;
    private int type;
    private String name;
    private String ext;
    private String created_at;
    private String updated_at;

    public EnvironmentPhoto() {
        super();
    }

    public EnvironmentPhoto(int id, int fall_diagnosis_story_id, int type, String name, String ext, String created_at,
                            String updated_at) {
        super();
        this.id = id;
        this.fall_diagnosis_story_id = fall_diagnosis_story_id;
        this.type = type;
        this.name = name;
        this.ext = ext;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFall_diagnosis_story_id() {
        return fall_diagnosis_story_id;
    }

    public void setFall_diagnosis_story_id(int fall_diagnosis_story_id) {
        this.fall_diagnosis_story_id = fall_diagnosis_story_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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