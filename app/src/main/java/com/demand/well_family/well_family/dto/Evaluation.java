package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class Evaluation {
    String title;
    int image;

    public Evaluation(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
