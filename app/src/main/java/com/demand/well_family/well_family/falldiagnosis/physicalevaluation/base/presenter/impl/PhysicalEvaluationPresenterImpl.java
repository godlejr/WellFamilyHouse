package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.interactor.PhysicalEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.interactor.impl.PhysicalEvaluationInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.presenter.PhysicalEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.view.PhysicalEvaluationView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public class PhysicalEvaluationPresenterImpl implements PhysicalEvaluationPresenter {
    private PhysicalEvaluationInteractor physicalEvaluationInteractor;
    private PhysicalEvaluationView physicalEvaluationView;
    private PreferenceUtil preferenceUtil;

    public PhysicalEvaluationPresenterImpl(Context context) {
        this.physicalEvaluationInteractor = new PhysicalEvaluationInteractorImpl(this);
        this.physicalEvaluationView = (PhysicalEvaluationView)context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(FallDiagnosisCategory fallDiagnosisCategory) {
        User user = preferenceUtil.getUserInfo();
        physicalEvaluationInteractor.setUser(user);
        physicalEvaluationInteractor.setFallDiagnosisCategory(fallDiagnosisCategory);
        physicalEvaluationView.init();

        View decorView = physicalEvaluationView.getDecorView();
        physicalEvaluationView.setToolbar(decorView);
        physicalEvaluationView.showToolbarTitle("신체능력 평가");
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            physicalEvaluationView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            physicalEvaluationView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onLoadData() {
        physicalEvaluationInteractor.getPhysicalEvaluationCategories();
    }

    @Override
    public void onSuccessGetPhysicalEvaluationCategories(ArrayList<FallDiagnosisContentCategory> physicalEvaluationList) {
        physicalEvaluationView.setPhysicalEvaluationAdapterInit(physicalEvaluationList);
    }

    @Override
    public void onClickStart() {
        physicalEvaluationView.navigateToCreatePhysicalEvaluationActivity(physicalEvaluationInteractor.getFallDiagnosisCategory());
    }

}

