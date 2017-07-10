package com.demand.well_family.well_family.falldiagnosis.environment.base.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.Evaluation;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.environment.base.interactor.EnvironmentEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.environment.base.interactor.impl.EnvironmentEvaluationInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.environment.base.presenter.EnvironmentEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.environment.base.view.EnvironmentEvaluationView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24
 */

public class EnvironmentEvaluationPresenterImpl implements EnvironmentEvaluationPresenter {
    private EnvironmentEvaluationView dangerEvaluationView;
    private EnvironmentEvaluationInteractor dangerEvaluationInteractor;
    private PreferenceUtil preferenceUtil;

    public EnvironmentEvaluationPresenterImpl(Context context) {
        this.dangerEvaluationView = (EnvironmentEvaluationView) context;
        this.dangerEvaluationInteractor = new EnvironmentEvaluationInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(FallDiagnosisCategory fallDiagnosisCategory) {
        User user = preferenceUtil.getUserInfo();
        dangerEvaluationInteractor.setUser(user);
        dangerEvaluationInteractor.setFallDiagnosisCategory(fallDiagnosisCategory);
        dangerEvaluationView.init();

        View decorView = dangerEvaluationView.getDecorView();
        dangerEvaluationView.setToolbar(decorView);
        dangerEvaluationView.showToolbarTitle("위험환경 평가");
    }

    @Override
    public void onLoadData() {
        dangerEvaluationInteractor.getDangerEvaluationList();
    }

    @Override
    public void onSuccessGetDiagnosisCategories(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList) {
        dangerEvaluationView.setDangerEvaluationAdapterInit(fallDiagnosisContentCategoryList);
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            dangerEvaluationView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            dangerEvaluationView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onClickEnvironmentEvaluationItem(FallDiagnosisContentCategory fallDiagnosisContentCategory) {
        FallDiagnosisCategory fallDiagnosisCategory = dangerEvaluationInteractor.getFallDiagnosisCategory();
        dangerEvaluationView.navigateToCreateEnvironmentEvaluationActivity(fallDiagnosisCategory, fallDiagnosisContentCategory);
    }

}
