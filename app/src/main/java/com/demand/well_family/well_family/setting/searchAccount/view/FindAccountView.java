package com.demand.well_family.well_family.setting.searchAccount.view;

import android.view.View;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface FindAccountView {
    void init();

    View getDecorView();
    void setToolbar(View decorView);
    void showToolbarTitle(String message);

    void navigateToSendPasswordActivity(User user);
    void navigateToBack();
    void showMessage(String message);

    void gonePasswordResetButton();
    void showPasswordResetButton();

}
