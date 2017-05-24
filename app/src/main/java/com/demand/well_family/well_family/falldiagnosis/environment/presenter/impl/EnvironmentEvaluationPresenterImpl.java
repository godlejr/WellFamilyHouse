package com.demand.well_family.well_family.falldiagnosis.environment.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.Evaluation;
import com.demand.well_family.well_family.falldiagnosis.environment.interactor.EnvironmentEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.environment.interactor.impl.EnvironmentEvaluationInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.environment.presenter.EnvironmentEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.environment.view.EnvironmentEvaluationView;
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
    public void onCreate() {
        dangerEvaluationView.init();

        View decorView = dangerEvaluationView.getDecorView();
        dangerEvaluationView.setToolbar(decorView);
        dangerEvaluationView.showToolbarTitle("위험 환경 평가");
    }

    @Override
    public void setDangerEvaluationList() {
        ArrayList<Evaluation> dangerEvaluationList = dangerEvaluationInteractor.getDangerEvaluationList();
        dangerEvaluationView.setDangerEvaluationList(dangerEvaluationList);
    }

}
