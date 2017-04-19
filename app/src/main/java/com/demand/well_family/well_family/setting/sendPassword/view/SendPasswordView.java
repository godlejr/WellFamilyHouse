package com.demand.well_family.well_family.setting.sendPassword.view;

import android.view.View;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public interface SendPasswordView {
    void init();

    View getDecorView();
    void setToolbar(View decorView);
    void showToolbarTitle(String message);

    void setUserInfo();
    void showMessage(String message);

    void navigateToBack();
}
