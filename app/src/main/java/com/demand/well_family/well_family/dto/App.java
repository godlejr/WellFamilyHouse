package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-01-23.
 */

public class App {
   String name, packageName;
    int image;


    public App(String name) {
        this.name = name;
    }

    public App(String name, int image) {
        this.image = image;
        this.name = name;
    }

    public App(String name, int image, String packageName) {
        this.name = name;
        this.image = image;
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
