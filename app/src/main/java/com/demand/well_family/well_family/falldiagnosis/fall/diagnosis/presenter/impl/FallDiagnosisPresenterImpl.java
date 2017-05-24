package com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter.impl;

import android.content.Context;
import android.view.View;


import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.interactor.FallDiagnosisInteractor;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.interactor.impl.FallDiagnosisInteractorImpl;

import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter.FallDiagnosisPresenter;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.view.FallDiagnosisView;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class FallDiagnosisPresenterImpl implements FallDiagnosisPresenter {
    private FallDiagnosisInteractor fallDiagnosisInteractor;
    private FallDiagnosisView fallDiagnosisView;
    private PreferenceUtil preferenceUtil;

    public FallDiagnosisPresenterImpl(Context context) {
        this.fallDiagnosisInteractor = new FallDiagnosisInteractorImpl(this);
        this.fallDiagnosisView = (FallDiagnosisView)context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        fallDiagnosisView.init();

        View decorView = fallDiagnosisView.getDecorView();
        fallDiagnosisView.setToolbar(decorView);
        fallDiagnosisView.showToolbarTitle("낙상위험 자가진단");
    }

    @Override
    public void setNextView(int page) {
        fallDiagnosisView.setNextView(page + 1);
    }

    @Override
    public void setPreviousView(int page) {
        fallDiagnosisView.setPreviousView(page - 1);
    }

    @Override
    public void onClickNextView(int page) {
        int daignosisItemCount = fallDiagnosisView.getDiagnosisItemCount();

        if(page == daignosisItemCount){
            fallDiagnosisView.navigateToResultActivity();
        } else {
            fallDiagnosisView.setNextView(page + 1);
        }
    }

}
