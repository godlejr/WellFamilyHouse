package com.demand.well_family.well_family.setting.deactivationReason.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.setting.deactivationReason.interactor.DeactivationReasonInteractor;
import com.demand.well_family.well_family.setting.deactivationReason.interactor.impl.DeactivationReasonInteractorImpl;
import com.demand.well_family.well_family.setting.deactivationReason.presenter.DeactivationReasonPresenter;
import com.demand.well_family.well_family.setting.deactivationReason.view.DeactivationReasonView;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public class DeactivationReasonPresenterImpl implements DeactivationReasonPresenter {
    private DeactivationReasonInteractor deactivationReasonInteractor;
    private DeactivationReasonView deactivationReasonView;
    private PreferenceUtil preferenceUtil;

    private String spinnerArray[] = {"7일", "14일", "30일"};

    public DeactivationReasonPresenterImpl(Context context) {
        this.deactivationReasonView = (DeactivationReasonView) context;
        this.deactivationReasonInteractor = new DeactivationReasonInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        deactivationReasonView.init();

        View decorView = deactivationReasonView.getDecorView();
        deactivationReasonView.setToolbar(decorView);
        deactivationReasonView.showToolbarTitle("계정 비활성화");
        deactivationReasonView.setDeactivationPeriodSpinner(spinnerArray);
    }

    @Override
    public void onClickLogout() {
        preferenceUtil.removeUserInfo();
        deactivationReasonView.navigateToLoginActivity();
    }

    @Override
    public void setVisibleGuidance(int position) {
        for (int i = 0; i < 8; i++) {
            deactivationReasonView.goneGuidance(i);
        }
        if (position < 8) {
            deactivationReasonView.showGuidance(position);
        }
    }

    @Override
    public String getDeactivationPeriodMessage(int position) {
        String message = spinnerArray[position] + " 후 계정이 재활성화됩니다.";
        return message;
    }
}
