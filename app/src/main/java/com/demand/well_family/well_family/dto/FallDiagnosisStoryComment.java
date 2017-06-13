package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-06-09.
 */

public class FallDiagnosisStoryComment {
    private int id;
    private int fall_diagnosis_story_id;
    private int user_id;
    private String content;
    private int deleted;
    private String created_at;
    private String updated_at;

    public FallDiagnosisStoryComment() {
        super();
    }

    public FallDiagnosisStoryComment(int id, int fall_diagnosis_story_id, int user_id, String content, int deleted,
                                     String created_at, String updated_at) {
        super();
        this.id = id;
        this.fall_diagnosis_story_id = fall_diagnosis_story_id;
        this.user_id = user_id;
        this.content = content;
        this.deleted = deleted;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
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
