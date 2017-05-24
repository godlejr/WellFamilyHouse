package com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter.impl;

import android.content.Context;
import android.view.View;


import com.demand.well_family.well_family.dto.SelfDiagnosisCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.interactor.FallDiagnosisInteractor;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.interactor.impl.FallDiagnosisInteractorImpl;

import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter.FallDiagnosisPresenter;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.view.FallDiagnosisView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

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
    public void onClickNextView(int page) {
        int diagnosisItemCount = fallDiagnosisView.getDiagnosisItemCount()-1;

        if(page == diagnosisItemCount){
            fallDiagnosisView.navigateToResultActivity();
        } else {
            fallDiagnosisView.setNextView(page + 1);
        }
    }


    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            fallDiagnosisView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            fallDiagnosisView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onSuccessGetDiagnosisCategories(ArrayList<SelfDiagnosisCategory> diagnosisCategoryList) {
        fallDiagnosisView.setDiagnosisCategoryAdapter(diagnosisCategoryList);
    }

    @Override
    public void onLoadData() {
        User user = preferenceUtil.getUserInfo();
        fallDiagnosisInteractor.getDiagnosisCategories(user);
    }
}
