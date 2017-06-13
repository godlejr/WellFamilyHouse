package com.demand.well_family.well_family.dto;

import java.io.Serializable;

/**
 * Created by Dev-0 on 2017-05-26.
 */
public class FallDiagnosisStory implements Serializable {
    private int id;
    private int user_id;
    private int fall_diagnosis_category_id;
    private int fall_diagnosis_risk_category_id;
    private int hits;
    private String created_at;
    private String updated_at;

    private Boolean isChecked;
    private Boolean firstChecked;

    private FallDiagnosisStoryInfo fallDiagnosisStoryInfo;
    private int position;


    public FallDiagnosisStory() {
        super();
    }


    public FallDiagnosisStory(int id, int user_id, int fall_diagnosis_category_id, int fall_diagnosis_risk_category_id,
                              int hits, String created_at, String updated_at) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.fall_diagnosis_category_id = fall_diagnosis_category_id;
        this.setFall_diagnosis_risk_category_id(fall_diagnosis_risk_category_id);
        this.hits = hits;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Boolean getFirstChecked() {
        return firstChecked;
    }

    public void setFirstChecked(Boolean firstChecked) {
        this.firstChecked = firstChecked;
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

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }


    public int getFall_diagnosis_risk_category_id() {
        return fall_diagnosis_risk_category_id;
    }


    public void setFall_diagnosis_risk_category_id(int fall_diagnosis_risk_category_id) {
        this.fall_diagnosis_risk_category_id = fall_diagnosis_risk_category_id;
    }

    public FallDiagnosisStoryInfo getFallDiagnosisStoryInfo() {
        return fallDiagnosisStoryInfo;
    }

    public void setFallDiagnosisStoryInfo(FallDiagnosisStoryInfo fallDiagnosisStoryInfo) {
        this.fallDiagnosisStoryInfo = fallDiagnosisStoryInfo;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}