package com.demand.well_family.well_family.main.base.presenter;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIError;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public interface MainPresenter {
    void onCreate();


    void setUserInfo();

    void setToolbarAndMenu(User user);


    void getFamilyData();

    void getAppData();


    void onFamilyDataParsing(ArrayList<Family> familyList);

    void onClickLogout();

    void onCilckUser();

    void onClickNotification();

    void onBackPressed();

    void onNetworkError(APIError apiError);
}
