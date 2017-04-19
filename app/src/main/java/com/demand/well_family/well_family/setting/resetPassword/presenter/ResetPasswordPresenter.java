package com.demand.well_family.well_family.setting.resetPassword.presenter;


import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface ResetPasswordPresenter {
    void onCreate();

    void onTextChanged(CharSequence password);

    void setPassword(String password1, String password2);

    void onSuccessSetPassword();

    void onNetworkError(APIErrorUtil apiErrorUtil);
}
