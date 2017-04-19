package com.demand.well_family.well_family.setting.resetPassword.view;

import android.view.View;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface ResetPasswordView {
    void init();

    View getDecorView();

    void setToolbar(View decorView);

    void showToolbarTitle(String message);

    void setButtonColorBrown();

    void setButtonColorGray();

    void showMessage(String message);

    void navigateToBack();
}
