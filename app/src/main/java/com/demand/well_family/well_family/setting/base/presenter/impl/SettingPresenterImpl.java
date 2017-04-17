package com.demand.well_family.well_family.setting.base.presenter.impl;

import android.content.Context;

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

    public SettingPresenterImpl(Context context){
        this.settingView = (SettingView) context;
        this.settingInterator = new SettingInteratorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {

    }
}
