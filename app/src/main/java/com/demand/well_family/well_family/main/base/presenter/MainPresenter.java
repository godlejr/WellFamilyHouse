package com.demand.well_family.well_family.main.base.presenter;

import com.demand.well_family.well_family.dto.App;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public interface MainPresenter {
    void onCreate();


    void setUserInfo();

    void setToolbarAndMenu(User user);

    void setUserBirth(String birth);

    void getFamilyData();

    void getAppData();

    String getBannerName(int position);

    void onSuccessGetFamilyData(ArrayList<Family> familyList);

    void onClickApp(App app, int position);

    void onClickLogout();

    void onClickAppGames(String appPackageName);

    void onClickFamily(Family family);


    void onClickUser();

    void onClickNotification();

    void onClickSongMain();

    void onBackPressed();

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onClickExercise();

    void onClickFallDiagnosis();
}
