package com.demand.well_family.well_family.main.join.view;

import android.text.InputFilter;
import android.view.View;

import java.util.Calendar;

/**
 * Created by Dev-0 on 2017-04-13.
 */

public interface JoinView {

    void init();

    void setToolbar(View view);

    void setNameFilterCallback(InputFilter[] inputFilter);

    View getDecorView();

    void showDatePicker(Calendar calendar);


    void showBirth(String birth);

    void showEmailCheckNotification();

    void goneEmailCheckNotification();

    void showPasswordCheckNotification();

    void gonePasswordCheckNotification();

    void showPasswordConfirmCheckNotification();

    void gonePasswordConfirmCheckNotification();

    void showNameCheckNotification();

    void goneNameCheckNotification();

    void showBirthCheckNotification();

    void goneBirthCheckNotification();

    void showPhoneCheckNotification();

    void gonePhoneCheckNotification();






    void showToolbarTitle(String message);


    void showMessage(String message);

    void navigateToBack();

    void navigateToLoginActivity();


}
