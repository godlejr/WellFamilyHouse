package com.demand.well_family.well_family.falldiagnosis.dangerevaluation.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.Evaluation;
import com.demand.well_family.well_family.falldiagnosis.dangerevaluation.interactor.DangerEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.dangerevaluation.interactor.impl.DangerEvaluationInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.dangerevaluation.presenter.DangerEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.dangerevaluation.view.DangerEvaluationView;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24
 */

public class DangerEvaluationPresenterImpl implements DangerEvaluationPresenter {
    private DangerEvaluationView dangerEvaluationView;
    private DangerEvaluationInteractor dangerEvaluationInteractor;
    private PreferenceUtil preferenceUtil;

    public DangerEvaluationPresenterImpl(Context context) {
        this.dangerEvaluationView = (DangerEvaluationView) context;
        this.dangerEvaluationInteractor = new DangerEvaluationInteractorImpl(this);
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
