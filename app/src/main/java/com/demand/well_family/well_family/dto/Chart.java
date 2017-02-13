package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-01-31.
 */

public class Chart {
    String url, title, singer;

    public Chart(String url, String title, String singer) {
        this.url = url;
        this.title = title;
        this.singer = singer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
