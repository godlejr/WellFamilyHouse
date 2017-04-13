package com.demand.well_family.well_family.main.base.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.App;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.BadgeFlag;
import com.demand.well_family.well_family.main.base.interator.MainInterator;
import com.demand.well_family.well_family.main.base.interator.impl.MainInteratorImpl;
import com.demand.well_family.well_family.main.base.presenter.MainPresenter;
import com.demand.well_family.well_family.main.base.view.MainView;
import com.demand.well_family.well_family.util.APIError;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;
    private MainInterator mainInterator;
    private PreferenceUtil preferenceUtil;

    public MainPresenterImpl(Context context) {
        this.mainView = (MainView) context;
        this.mainInterator = new MainInteratorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }


    @Override
    public void onCreate() {
        mainView.init();
        setUserInfo();
        getFamilyData();
        getAppData();
    }

    @Override
    public void setUserInfo() {
        User user = preferenceUtil.getUserInfo();
        mainInterator.setUserInfo(user);
    }

    @Override
    public void setToolbarAndMenu(User user) {
        preferenceUtil.setUserInfoForMain(user);

        View decorView = mainView.getDecorView();

        mainView.setToolbar(decorView);
        mainView.setMenu(decorView);

        mainView.showToolbarTitle("홈");

        int badgeCount = preferenceUtil.getBadgeCount();

        if(badgeCount == BadgeFlag.BADGE_INITIALIZAION){
            mainView.showBadgeCount("");
        }else{
            mainView.showBadgeCount(String.valueOf(badgeCount));
        }

        mainView.showMenuUserInfo(user);
    }

    @Override
    public void setUserBirth(String birth) {
        String date = mainInterator.getUserBirth(birth);
        mainView.showMenuUserBirth(date);
    }

    @Override
    public void getFamilyData() {
        User user = preferenceUtil.getUserInfo();
        mainInterator.getFamilyData(user);
    }

    @Override
    public void onFamilyDataParsing(ArrayList<Family> familyList) {
        if (familyList.size() == 0) {
            mainView.setFamilyAddView();
        } else {
            mainView.setFamilyItem(familyList);
        }
    }

    @Override
    public void getAppData() {
        ArrayList<App> appList= mainInterator.getAppData();
        mainView.setAppItem(appList);
    }

    @Override
    public void onClickLogout() {
        preferenceUtil.removeUserInfo();
        mainView.navigateToLoginActivity();
    }

    @Override
    public void onCilckUser() {
        User user = preferenceUtil.getUserInfo();
        mainView.navigateToUserActivity(user);
    }

    @Override
    public void onClickNotification() {
        preferenceUtil.setBadgeCount(BadgeFlag.BADGE_INITIALIZAION);
        mainView.updateBadgeCount();
        mainView.showBadgeCount("");
        mainView.navigateToNotificationActivity();
    }

    @Override
    public void onNetworkError(APIError apiError) {
        if(apiError == null){
            mainView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        }else{
            mainView.showMessage(apiError.message());
        }
    }

    @Override
    public void onBackPressed() {
        if (mainView != null) {
            mainView.navigateToBackground();
        }
    }
}
