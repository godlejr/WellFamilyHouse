package com.demand.well_family.well_family.setting.sendPassword.view;

import android.view.View;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public interface SendPasswordView {
    void init();

    View getDecorView();
    void setToolbar(View decorView);

    void showToolbarTitle(String message);

    void setUserInfo(User user);
    void showMessage(String message);

    void navigateToBack();
}
