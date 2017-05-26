package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public class PhysicalEvaluationCategory {
    private int id;
    private int fall_diagnosis_content_category_id;
    private String name;
    private String content;
    private String avatar;
    private String created_at;
    private String updated_at;

    public PhysicalEvaluationCategory(int id, int fall_diagnosis_content_category_id, String name, String content,
                                      String avatar, String created_at, String updated_at) {
        super();
        this.id = id;
        this.fall_diagnosis_content_category_id = fall_diagnosis_content_category_id;
        this.name = name;
        this.content = content;
        this.avatar = avatar;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public PhysicalEvaluationCategory() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFall_diagnosis_content_category_id() {
        return fall_diagnosis_content_category_id;
    }

    public void setFall_diagnosis_content_category_id(int fall_diagnosis_content_category_id) {
        this.fall_diagnosis_content_category_id = fall_diagnosis_content_category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
