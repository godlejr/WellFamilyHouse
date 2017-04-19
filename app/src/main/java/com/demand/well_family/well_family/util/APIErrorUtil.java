package com.demand.well_family.well_family.util;

/**
 * Created by ㅇㅇ on 2017-03-15.
 */

public class APIErrorUtil {
    private int statusCode;
    private String message;

    public APIErrorUtil() {
    }

    public APIErrorUtil(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}
