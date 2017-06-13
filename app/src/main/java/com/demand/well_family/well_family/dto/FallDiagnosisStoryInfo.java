package com.demand.well_family.well_family.dto;

import java.io.Serializable;

/**
 * Created by ㅇㅇ on 2017-06-02.
 */

public class FallDiagnosisStoryInfo implements Serializable {
    private int story_id;
    private int user_id;
    private String title;
    private String avatar;
    private int score;
    private int total_count;
    private String risk_comment;


    public FallDiagnosisStoryInfo(int story_id, int user_id, String title, String avatar, int score, int total_count,
                                  String risk_comment) {
        super();
        this.story_id = story_id;
        this.user_id = user_id;
        this.title = title;
        this.avatar = avatar;
        this.score = score;
        this.total_count = total_count;
        this.risk_comment = risk_comment;
    }

    public FallDiagnosisStoryInfo() {
        super();
    }

    public int getStory_id() {
        return story_id;
    }

    public void setStory_id(int story_id) {
        this.story_id = story_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public String getRisk_comment() {
        return risk_comment;
    }

    public void setRisk_comment(String risk_comment) {
        this.risk_comment = risk_comment;
    }


}