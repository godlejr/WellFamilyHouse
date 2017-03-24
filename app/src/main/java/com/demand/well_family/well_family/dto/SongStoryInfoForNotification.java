package com.demand.well_family.well_family.dto;

/**
 * Created by Dev-0 on 2017-03-22.
 */

public class SongStoryInfoForNotification {
    private int id;
    private int range_id;
    private int song_id;
    private String song_title;
    private String song_singer;
    private String record_file;
    private String content;
    private String location;
    private String created_at;
    private int hits;

    private int user_id;
    private int user_level;
    private String user_name;
    private String user_email;
    private String user_avatar;
    private String user_phone;
    private String user_birth;

    public SongStoryInfoForNotification(int id, int range_id, int song_id, String song_title, String song_singer, String record_file, String content, String location, String created_at, int hits, int user_id, int user_level, String user_name, String user_email, String user_avatar, String user_phone, String user_birth) {
        this.id = id;
        this.range_id = range_id;
        this.song_id = song_id;
        this.song_title = song_title;
        this.song_singer = song_singer;
        this.record_file = record_file;
        this.content = content;
        this.location = location;
        this.created_at = created_at;
        this.hits = hits;
        this.user_id = user_id;
        this.user_level = user_level;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_avatar = user_avatar;
        this.user_phone = user_phone;
        this.user_birth = user_birth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_birth() {
        return user_birth;
    }

    public void setUser_birth(String user_birth) {
        this.user_birth = user_birth;
    }
}
