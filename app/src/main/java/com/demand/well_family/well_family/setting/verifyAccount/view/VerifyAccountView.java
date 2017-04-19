package com.demand.well_family.well_family.setting.verifyAccount.view;

import android.view.View;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface VerifyAccountView {
    void init();

    View getDecorView();

    void setToolbar(View decorView);

    void showToolbarTitle(String message);

    void showMessage(String message);

    void navigateToBack();

    void navigateToFindAccountActivity();

    void navigateToResetPasswordActivity();

    void setButtonColorBrown();

    void setButtonColorGray();
}
