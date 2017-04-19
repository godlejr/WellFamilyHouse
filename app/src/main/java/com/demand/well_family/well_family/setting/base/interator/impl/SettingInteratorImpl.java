package com.demand.well_family.well_family.setting.base.interator.impl;

import android.app.Activity;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.setting.base.interator.SettingInterator;
import com.demand.well_family.well_family.setting.base.presenter.SettingPresenter;

/**
 * Created by Dev-0 on 2017-04-17.
 */

public class SettingInteratorImpl implements SettingInterator {
    private SettingPresenter settingPresenter;

    public SettingInteratorImpl(SettingPresenter settingPresenter){
        this.settingPresenter = settingPresenter;
    }


}
