package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-02-03.
 */

public class SongStoryInfo {
    private  int user_id;
    private String name;
    private String avatar;
    private int story_id;
    private int range_id;
    private int song_id;
    private String song_title;
    private String song_singer;
    private String record_file;
    private String created_at;
    private String content;
    private String location;
    private int hits;
    private Boolean first_checked; //  select first check
    private Boolean isChecked; // like cheched


    public SongStoryInfo(int user_id, String name, String avatar, int story_id, int range_id, int song_id, String song_title, String song_singer, String record_file, String created_at, String content, String location, int hits) {
        this.user_id = user_id;
        this.name = name;
        this.avatar = avatar;
        this.story_id = story_id;
        this.range_id = range_id;
        this.song_id = song_id;
        this.song_title = song_title;
        this.song_singer = song_singer;
        this.record_file = record_file;
        this.created_at = created_at;
        this.content = content;
        this.location = location;
        this.hits = hits;
        this.first_checked = false;
        this.isChecked = false;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStory_id() {
        return story_id;
    }

    public void setStory_id(int story_id) {
        this.story_id = story_id;
    }

    public int getRange_id() {
        return range_id;
    }

    public void setRange_id(int range_id) {
        this.range_id = range_id;
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public String getSong_title() {
        return song_title;
    }

    public void setSong_title(String song_title) {
        this.song_title = song_title;
    }

    public String getSong_singer() {
        return song_singer;
    }

    public void setSong_singer(String song_singer) {
        this.song_singer = song_singer;
    }

    public String getRecord_file() {
        return record_file;
    }

    public void setRecord_file(String record_file) {
        this.record_file = record_file;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public Boolean getFirst_checked() {
        return first_checked;
    }

    public void setFirst_checked(Boolean first_checked) {
        this.first_checked = first_checked;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
