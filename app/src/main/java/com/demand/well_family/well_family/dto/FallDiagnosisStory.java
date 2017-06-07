package com.demand.well_family.well_family.dto;

/**
 * Created by Dev-0 on 2017-05-26.
 */
public class FallDiagnosisStory {
    private int id;
    private int user_id;
    private int fall_diagnosis_category_id;
    private int fall_diagnosis_risk_category_id;
    private int hits;
    private String created_at;
    private String updated_at;

    private Boolean first_checked;

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

    public Boolean getFirst_checked() {
        return first_checked;
    }

    public void setFirst_checked(Boolean first_checked) {
        this.first_checked = first_checked;
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

}