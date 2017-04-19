package com.demand.well_family.well_family.setting.deactivationReason.presenter;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public interface DeactivationReasonPresenter {
    void onCreate();
    void onClickLogout();
    void setVisibleGuidance(int position);
    String getDeactivationPeriodMessage(int position);
}
