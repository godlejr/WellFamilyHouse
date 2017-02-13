package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-02-14.
 */

public class Range {
    private int id;
    private String name;

    public Range() {
        super();
    }
    public Range(int id, String name) {
        super();
        this.id = id;
        this.name = name;
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

}
