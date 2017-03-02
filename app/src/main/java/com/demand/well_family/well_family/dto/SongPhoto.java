package com.demand.well_family.well_family.dto;

import java.io.Serializable;

/**
 * Created by ㅇㅇ on 2017-02-02.
 */

public class SongPhoto implements Serializable{
    private int id;
    private int song_story_id;
    private int type;
    private String name;
    private String ext;
    private String created_at;
    private String updated_at;

    public SongPhoto(int id, int song_story_id, int type, String name, String ext, String created_at, String updated_at) {
        this.id = id;
        this.song_story_id = song_story_id;
        this.type = type;
        this.name = name;
        this.ext = ext;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSong_story_id() {
        return song_story_id;
    }

    public void setSong_story_id(int song_story_id) {
        this.song_story_id = song_story_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
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
