package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-02-02.
 */

public class SongComment {
    private int id;
    private int user_id;
    private int song_id;
    private String content;
    private int deleted;
    private String created_at;
    private String updated_at;

    public SongComment() {
        super();
    }

    public SongComment(int id, int user_id, int song_id, String content, int deleted, String created_at,
                       String updated_at) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.song_id = song_id;
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

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
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
