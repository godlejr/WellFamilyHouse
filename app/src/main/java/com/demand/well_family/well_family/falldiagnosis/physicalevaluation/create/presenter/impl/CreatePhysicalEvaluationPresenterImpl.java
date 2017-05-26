package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.interactor.CreatePhysicalEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.interactor.impl.CreatePhysicalEvaluationInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.CreatePhysicalEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.view.CreatePhysicalEvaluationView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public class CreatePhysicalEvaluationPresenterImpl implements CreatePhysicalEvaluationPresenter {
    private CreatePhysicalEvaluationInteractor createPhysicalEvaluationInteractor;
    private CreatePhysicalEvaluationView createPhysicalEvaluationView;
    private PreferenceUtil preferenceUtil;

    public CreatePhysicalEvaluationPresenterImpl(Context context) {
        this.createPhysicalEvaluationInteractor = new CreatePhysicalEvaluationInteractorImpl(this);
        this.createPhysicalEvaluationView = (CreatePhysicalEvaluationView)context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(FallDiagnosisCategory fallDiagnosisCategory) {
        createPhysicalEvaluationInteractor.setFallDiagnosisCategory(fallDiagnosisCategory);
        createPhysicalEvaluationView.init();

        View decorView = createPhysicalEvaluationView.getDecorView();
        createPhysicalEvaluationView.setToolbar(decorView);
        createPhysicalEvaluationView.showToolbarTitle("신체능력 평가");
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            createPhysicalEvaluationView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            createPhysicalEvaluationView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onLoadData() {
        User user = preferenceUtil.getUserInfo();
        createPhysicalEvaluationInteractor.getPhysicalEvaluationCategories(user);
    }

    @Override
    public void onSuccessGetPhysicalEvaluationCategories(ArrayList<PhysicalEvaluationCategory> physicalEvaluationList) {
        createPhysicalEvaluationView.setCreatePhysicalEvaluationAdapter(physicalEvaluationList);
    }

    @Override
    public void onClickNext(int position, int count) {
        int listCount = count-1;

        if(position != listCount){
            createPhysicalEvaluationView.setNextView(position);
        } else {
            // result
        }

    }
}
