package com.demand.well_family.well_family.setting.base.view;

import android.view.View;

/**
 * Created by Dev-0 on 2017-04-17.
 */

public interface SettingView {
    void init();

    void setNotificationCheck(boolean isCheck);
    void getNotificationCheck();
    void setToolbar(View decorView);
    View getDecorView();
    void showToolbarTitle(String message);

    void navigateToDeactivationActivity();
    void navigateToResetPasswordActivity();
    void navigateToBack();

    void goneResetPassword();
    void showResetPassword();
}
