package com.demand.well_family.well_family.main.join.presenter;

import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by Dev-0 on 2017-04-13.
 */

public interface JoinPresenter {

    void onCreate();

    void setCalendar();

    void setUserBirth(String birth);

    void setNameFilterCallback();

    void onClickJoin(String email, String password, String passwordConfirm, String name, String birth, String phone);

    void onClickEmailCheck(String email);

    void onSuccessJoin();

    void onNameFilter();

    void validateEmail(int check);

    void onNetworkError(APIErrorUtil apiErrorUtil);

}
