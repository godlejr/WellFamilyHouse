package com.demand.well_family.well_family.util;


import android.content.Context;
import android.content.SharedPreferences;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class PreferenceUtil {
    private Context context;

    private SharedPreferences sharedUserPreferences;
    private SharedPreferences sharedBadgePreferences;


    public PreferenceUtil(Context context) {
        this.context = context;
    }

    public SharedPreferences getUserSharedPreferences() {
        if (sharedUserPreferences == null) {
            sharedUserPreferences = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        }
        return sharedUserPreferences;
    }

    public SharedPreferences getBadgeSharePreference() {
        if (sharedBadgePreferences == null) {
            sharedBadgePreferences = context.getSharedPreferences("badge", Context.MODE_PRIVATE);
        }

        return sharedBadgePreferences;
    }

    public User getUserInfo() {
        User user = new User();
        SharedPreferences userInfo = getUserSharedPreferences();
        user.setId(userInfo.getInt("user_id", 0));

        user.setLevel(userInfo.getInt("user_level", 0));
        user.setName(userInfo.getString("user_name", null));
        user.setEmail(userInfo.getString("user_email", null));
        user.setBirth(userInfo.getString("user_birth", null));
        user.setAvatar(userInfo.getString("user_avatar", null));
        user.setPhone(userInfo.getString("user_phone", null));
        user.setAccess_token(userInfo.getString("access_token", null));
        user.setDevice_id(userInfo.getString("device_id", null));
        user.setLogin_category_id(userInfo.getInt("login_category_id", 1));

        return user;
    }

    public int getBadgeCount() {
        SharedPreferences badgeInfo = getBadgeSharePreference();
        int badgeCount = badgeInfo.getInt("badge_count", 0);

        return badgeCount;
    }

    public void setBadgeCount(int count) {
        SharedPreferences badgeInfo = getBadgeSharePreference();
        SharedPreferences.Editor editor = badgeInfo.edit();

        editor.putInt("badge_count", count);
        editor.apply();
    }

    public boolean getNotificationCheck() {
        SharedPreferences userInfo = getUserSharedPreferences();
        boolean isCheck = userInfo.getBoolean("notification", true);

        return isCheck;
    }

    public void setNotificationCheck(boolean isCheck) {
        SharedPreferences userInfo = getUserSharedPreferences();
        SharedPreferences.Editor editor = userInfo.edit();

        editor.putBoolean("notification", isCheck);
        editor.apply();
    }

    public void setUserInfo(User user, String deviceId, String token, String access_token) {
        SharedPreferences userInfo = getUserSharedPreferences();
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putInt("user_id", user.getId());
        editor.putString("user_name", user.getName());
        editor.putString("user_email", user.getEmail());
        editor.putString("user_birth", user.getBirth());
        editor.putString("user_avatar", user.getAvatar());
        editor.putString("user_phone", user.getPhone());
        editor.putString("device_id", deviceId);
        editor.putString("token", token);
        editor.putInt("user_level", user.getLevel());
        editor.putString("access_token", access_token);
        editor.putInt("login_category_id", user.getLogin_category_id());

        editor.commit();
    }



    public void setUserInfoForMain(User user){
        SharedPreferences userInfo = getUserSharedPreferences();
        SharedPreferences.Editor editor = userInfo.edit();

        editor.putString("user_name", user.getName());
        editor.putString("user_email", user.getEmail());
        editor.putString("user_birth", user.getBirth());
        editor.putString("user_avatar", user.getAvatar());
        editor.putString("user_phone", user.getPhone());

        editor.apply();
    }

    public void removeUserInfo(){
        SharedPreferences userInfo = getUserSharedPreferences();
        SharedPreferences.Editor editor = userInfo.edit();
        editor.remove("user_id");
        editor.remove("user_name");
        editor.remove("user_email");
        editor.remove("user_birth");
        editor.remove("user_avatar");
        editor.remove("user_phone");
        editor.remove("user_level");
        editor.remove("access_token");
        editor.commit();
    }

}
