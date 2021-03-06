package com.demand.well_family.well_family.dto;

/**
 * Created by Dev-0 on 2017-02-24.
 */

public class FavoriteCategory {
    private int id;
    private String name;
    private String created_at;
    private String updated_at;
    private boolean checked;

    public FavoriteCategory(int id, String name, String created_at, String updated_at) {
        super();
        this.id = id;
        this.name = name;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.checked = false;
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
