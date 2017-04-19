package com.demand.well_family.well_family.setting.base.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.setting.base.interator.SettingInterator;
import com.demand.well_family.well_family.setting.base.interator.impl.SettingInteratorImpl;
import com.demand.well_family.well_family.setting.base.presenter.SettingPresenter;
import com.demand.well_family.well_family.setting.base.view.SettingView;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by Dev-0 on 2017-04-17.
 */

public class SettingPresenterImpl implements SettingPresenter {
    private SettingView settingView;
    private SettingInterator settingInterator;
    private PreferenceUtil preferenceUtil;

    public SettingPresenterImpl(Context context) {
        this.settingView = (SettingView) context;
        this.settingInterator = new SettingInteratorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        settingView.init();
        setToolbar();

        getNotificationCheck();
        validateLoginCategoryId();
    }

    @Override
    public void getNotificationCheck() {
        boolean isCheck = preferenceUtil.getNotificationCheck();
        settingView.setNotificationCheck(isCheck);
    }

    @Override
    public void setToolbar() {
        View decorView = settingView.getDecorView();
        settingView.setToolbar(decorView);
        settingView.showToolbarTitle("설정");
    }

    @Override
    public void setNotificationCheck(boolean isCheck) {
        settingView.setNotificationCheck(isCheck);
        preferenceUtil.setNotificationCheck(isCheck);
    }

    @Override
    public void validateLoginCategoryId() {
        User user = preferenceUtil.getUserInfo();
        int loginCategoryId = user.getLogin_category_id();

        if (loginCategoryId == 1) {
            settingView.showResetPassword();
        } else {
            settingView.goneResetPassword();
        }
    }
}
