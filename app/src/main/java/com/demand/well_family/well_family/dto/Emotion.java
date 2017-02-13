package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-02-12.
 */

public class Emotion {
    private String id;
    private String category_id;
    private String created_at;
    private String name;
    private String image;

    public Emotion(String id, String category_id, String created_at, String name, String image) {
        this.id = id;
        this.category_id = category_id;
        this.created_at = created_at;
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
