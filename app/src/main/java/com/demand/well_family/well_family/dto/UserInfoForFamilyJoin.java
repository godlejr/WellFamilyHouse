package com.demand.well_family.well_family.dto;

/**
 * Created by ㅇㅇ on 2017-03-30.
 */

public class UserInfoForFamilyJoin {
    private int id;
    private String email;
    private String name;
    private String birth;
    private String phone;
    private String avatar;
    private int level;
    private int join_flag;

    public UserInfoForFamilyJoin() {
        super();
    }

    public UserInfoForFamilyJoin(int id, String email, String name, String birth, String phone, String avatar, int level, int join_flag) {
        super();
        this.id = id;
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.phone = phone;
        this.avatar = avatar;
        this.level = level;
        this.join_flag = join_flag;
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

    public int getJoin_flag() {
        return join_flag;
    }

    public void setJoin_flag(int join_flag) {
        this.join_flag = join_flag;
    }


}
