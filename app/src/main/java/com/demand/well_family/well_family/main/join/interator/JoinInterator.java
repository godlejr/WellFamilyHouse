package com.demand.well_family.well_family.main.join.interator;

import android.text.InputFilter;

/**
 * Created by Dev-0 on 2017-04-13.
 */

public interface JoinInterator {

    String getUserBirth(String birth);

    boolean getEmailValidation(String email);

    boolean getPasswordValidation(String password);

    void setJoin(String email, String password, String name, String birth, String phone);

    void getEmailCheck(String email);

    InputFilter[] getInputFilterForName();

    boolean isEmailCheck();

    void setEmailCheck(boolean emailCheck);
}
