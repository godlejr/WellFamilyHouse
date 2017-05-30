package com.demand.well_family.well_family.dto;

/**
 * Created by Dev-0 on 2017-05-30.
 */

public class PhysicalEvaluationScore {
    private int id;
    private int fall_diagnosis_story_id;
    private int balance_score;
    private int movement_score;
    private int leg_strength_score;
    private String created_at;
    private String updated_at;

    public PhysicalEvaluationScore() {
    }

    public PhysicalEvaluationScore(int id, int fall_diagnosis_story_id, int balance_score, int movement_score,
                                   int leg_strength_score, String created_at, String updated_at) {
        super();
        this.id = id;
        this.fall_diagnosis_story_id = fall_diagnosis_story_id;
        this.balance_score = balance_score;
        this.movement_score = movement_score;
        this.leg_strength_score = leg_strength_score;
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

    public int getBalance_score() {
        return balance_score;
    }

    public void setBalance_score(int balance_score) {
        this.balance_score = balance_score;
    }

    public int getMovement_score() {
        return movement_score;
    }

    public void setMovement_score(int movement_score) {
        this.movement_score = movement_score;
    }

    public int getLeg_strength_score() {
        return leg_strength_score;
    }

    public void setLeg_strength_score(int leg_strength_score) {
        this.leg_strength_score = leg_strength_score;
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
