package com.demand.well_family.well_family.dto;

/**
 * Created by Dev-0 on 2017-05-26.
 */

public class FallDiagnosisStory {
    private int id;
    private int user_id;
    private int fall_diagnosis_category_id;
    private String created_at;
    private String updated_at;


    public FallDiagnosisStory(int id, int user_id, int fall_diagnosis_category_id, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.fall_diagnosis_category_id = fall_diagnosis_category_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public FallDiagnosisStory() {
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

    public int getFall_diagnosis_category_id() {
        return fall_diagnosis_category_id;
    }

    public void setFall_diagnosis_category_id(int fall_diagnosis_category_id) {
        this.fall_diagnosis_category_id = fall_diagnosis_category_id;
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
