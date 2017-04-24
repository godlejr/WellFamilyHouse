package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-04-21.
 */

public class Report {
    private String author_name;
    private int writing_category_id;
    private int writing_id;
    private String writing_content;
    private int writing_position;

    public Report() {
    }

    public Report(String author_name, int writing_category_id, int writing_id, String writing_content, int writing_position) {
        this.author_name = author_name;
        this.writing_category_id = writing_category_id;
        this.writing_id = writing_id;
        this.writing_content = writing_content;
        this.writing_position = writing_position;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public int getWriting_category_id() {
        return writing_category_id;
    }

    public void setWriting_category_id(int writing_category_id) {
        this.writing_category_id = writing_category_id;
    }

    public int getWriting_id() {
        return writing_id;
    }

    public void setWriting_id(int writing_id) {
        this.writing_id = writing_id;
    }

    public String getWriting_content() {
        return writing_content;
    }

    public void setWriting_content(String writing_content) {
        this.writing_content = writing_content;
    }

    public int getWriting_position() {
        return writing_position;
    }

    public void setWriting_position(int writing_position) {
        this.writing_position = writing_position;
    }
}
