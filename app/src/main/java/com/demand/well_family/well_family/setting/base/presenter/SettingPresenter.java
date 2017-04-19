package com.demand.well_family.well_family.setting.base.presenter;

/**
 * Created by Dev-0 on 2017-04-17.
 */

public interface SettingPresenter {
    void onCreate();

    void setNotificationCheck(boolean isCheck);
    void getNotificationCheck();
    void setToolbar();

    void validateLoginCategoryId();
}
