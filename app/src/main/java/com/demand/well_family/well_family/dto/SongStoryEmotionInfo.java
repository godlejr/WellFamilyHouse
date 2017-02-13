package com.demand.well_family.well_family.dto;

import java.io.Serializable;

/**
 * Created by Dev-0 on 2017-02-14.
 */

public class SongStoryEmotionInfo implements Serializable {
    private int id;
    private String name;
    private int emotion_category_id;
    private String avatar;
    private boolean checked;

    public SongStoryEmotionInfo(int id, String name, int emotion_category_id, String avatar) {
        this.id = id;
        this.name = name;
        this.emotion_category_id = emotion_category_id;
        this.avatar = avatar;
        this.checked =false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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
}
