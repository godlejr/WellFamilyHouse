package com.demand.well_family.well_family.dto;

import java.io.Serializable;

/**
 * Created by Dev-0 on 2017-05-29.
 */

public class PhysicalEvaluation  implements Serializable {

    private int id;
    private int user_id;
    private int fall_diagnosis_story_id;
    private int physical_evaluation_category_id;
    private int minute;
    private int second;
    private int millisecond;
    private String created_at;
    private String updated_at;

    public PhysicalEvaluation() {
        super();
    }

    public PhysicalEvaluation(int id, int user_id, int fall_diagnosis_story_id, int physical_evaluation_category_id,
                              int minute, int second, int millisecond, String created_at, String updated_at) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.fall_diagnosis_story_id = fall_diagnosis_story_id;
        this.physical_evaluation_category_id = physical_evaluation_category_id;
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
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

    public int getFall_diagnosis_story_id() {
        return fall_diagnosis_story_id;
    }

    public void setFall_diagnosis_story_id(int fall_diagnosis_story_id) {
        this.fall_diagnosis_story_id = fall_diagnosis_story_id;
    }

    public int getPhysical_evaluation_category_id() {
        return physical_evaluation_category_id;
    }

    public void setPhysical_evaluation_category_id(int physical_evaluation_category_id) {
        this.physical_evaluation_category_id = physical_evaluation_category_id;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMillisecond() {
        return millisecond;
    }

    public void setMillisecond(int millisecond) {
        this.millisecond = millisecond;
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
