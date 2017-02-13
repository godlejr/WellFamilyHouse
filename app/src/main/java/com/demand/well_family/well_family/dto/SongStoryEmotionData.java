package com.demand.well_family.well_family.dto;

/**
 * Created by Dev-0 on 2017-02-14.
 */

public class SongStoryEmotionData {
    private int id;
    private String name;
    private int emotion_category_id;
    private String avatar;
    private String created_at;
    private String updated_at;

    public SongStoryEmotionData(int id, String name, int emotion_category_id, String avatar, String created_at,
                                String updated_at) {
        super();
        this.id = id;
        this.name = name;
        this.emotion_category_id = emotion_category_id;
        this.avatar = avatar;
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
    public int getEmotion_category_id() {
        return emotion_category_id;
    }
    public void setEmotion_category_id(int emotion_category_id) {
        this.emotion_category_id = emotion_category_id;
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
