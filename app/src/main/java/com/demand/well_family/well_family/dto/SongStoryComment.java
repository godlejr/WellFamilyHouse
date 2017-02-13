package com.demand.well_family.well_family.dto;

/**
 * Created by Dev-0 on 2017-02-13.
 */

public class SongStoryComment {
    private int id;
    private int user_id;
    private int song_story_id;
    private String content;
    private int deleted;
    private String created_at;
    private String updated_at;

    public SongStoryComment(int id, int user_id, int song_story_id, String content, int deleted, String created_at,
                            String updated_at) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.song_story_id = song_story_id;
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
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public int getSong_story_id() {
        return song_story_id;
    }
    public void setSong_story_id(int song_story_id) {
        this.song_story_id = song_story_id;
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
