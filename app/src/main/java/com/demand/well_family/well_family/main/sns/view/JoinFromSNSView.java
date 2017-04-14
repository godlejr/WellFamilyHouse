package com.demand.well_family.well_family.main.sns.view;

import android.view.View;

import java.util.Calendar;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public interface JoinFromSNSView {
    void init();

    void setToolbar(View view);

    View getDecorView();

    void showToolbarTitle(String message);

    void showDatePicker(Calendar calendar);

    void showBrith(String birth);

    void showBirthCheckNotification();

    void goneBirthCheckNotification();

    void showPhoneCheckNotification();

    void gonePhoneCheckNotification();


    void showMessage(String message);

    String getDeviceId();

    String getFireBaseToken();

    void navigateToBack();

    void navigateToMainActivity();


}
