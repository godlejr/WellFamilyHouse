package com.demand.well_family.well_family.dto;

/**
 * Created by Dev-0 on 2017-01-19.
 */

public class User {
    private int id;
    private String email;
    private String name;
    private String birth;
    private String phone;
    private String avatar;
    private int level;
    private int login_category_id;
    private String device_id;
    private String access_token;

    public User() {
    }

    public User(int id, String email, String name, String birth, String phone, String avatar, int level) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.phone = phone;
        this.avatar = avatar;
        this.level = level;
    }

    public User(int id, String email, String name, String birth, String phone, String avatar, int level, int login_category_id, String access_token) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.phone = phone;
        this.avatar = avatar;
        this.level = level;
        this.login_category_id = login_category_id;
        this.access_token = access_token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getLogin_category_id() {
        return login_category_id;
    }

    public void setLogin_category_id(int login_category_id) {
        this.login_category_id = login_category_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
