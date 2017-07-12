package com.demand.well_family.well_family.falldiagnosis.environment.create.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.environment.create.flag.CreateEnvironmentEvaluationCodeFlag;
import com.demand.well_family.well_family.falldiagnosis.environment.create.interactor.CreateEnvironmentEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.environment.create.interactor.impl.CreateEnvironmentEvaluationInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.environment.create.presenter.CreateEnvironmentEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.environment.create.view.CreateEnvironmentEvaluationView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-30.
 */

public class CreateEnvironmentEvaluationPresenterImpl implements CreateEnvironmentEvaluationPresenter {
    private CreateEnvironmentEvaluationInteractor createEnvironmentEvaluationInteractor;
    private CreateEnvironmentEvaluationView createEnvironmentEvaluationView;
    private PreferenceUtil preferenceUtil;

    public CreateEnvironmentEvaluationPresenterImpl(Context context) {
        this.createEnvironmentEvaluationInteractor = new CreateEnvironmentEvaluationInteractorImpl(this);
        this.createEnvironmentEvaluationView = (CreateEnvironmentEvaluationView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory) {
        createEnvironmentEvaluationInteractor.setFallDiagnosisCategory(fallDiagnosisCategory);
        createEnvironmentEvaluationInteractor.setFallDiagnosisContentCategory(fallDiagnosisContentCategory);
        createEnvironmentEvaluationView.init();

        View decorView = createEnvironmentEvaluationView.getDecorView();
        createEnvironmentEvaluationView.setToolbar(decorView);
        createEnvironmentEvaluationView.showToolbar(fallDiagnosisContentCategory.getName());
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            createEnvironmentEvaluationView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            createEnvironmentEvaluationView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onLoadData() {
        User user = preferenceUtil.getUserInfo();
        createEnvironmentEvaluationInteractor.getEnvironmentEvaluationCategory(user);
    }

    @Override
    public void onSuccessGetEnvironmentEvaluationCategories(ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList) {
        createEnvironmentEvaluationView.setCreateEnvironmentEvaluationAdapterInit(environmentEvaluationCategoryList);
    }

    @Override
    public void onClickAnswer(int position, int categorySize, int environmentEvaluationCategoryId, int flag) {
        int endOfCategoryList = categorySize - 1;

        if (flag == CreateEnvironmentEvaluationCodeFlag.NO) {
            createEnvironmentEvaluationInteractor.setAnswerAdded(environmentEvaluationCategoryId);
        }

        if (endOfCategoryList == position) {

            ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList = createEnvironmentEvaluationInteractor.getEnvironmentEvaluationCategoryList();
            int environmentEvaluationCategorySize  = environmentEvaluationCategoryList.size();
            FallDiagnosisCategory fallDiagnosisCategory = createEnvironmentEvaluationInteractor.getFallDiagnosisCategory();
            FallDiagnosisContentCategory fallDiagnosisContentCategory = createEnvironmentEvaluationInteractor.getFallDiagnosisContentCategory();
            ArrayList<Integer> answerList = createEnvironmentEvaluationInteractor.getAnswerList();

            createEnvironmentEvaluationView.navigateToEnvironmentEvaluationAPhotoActivity(fallDiagnosisCategory, fallDiagnosisContentCategory, answerList, environmentEvaluationCategorySize);
        } else {
            createEnvironmentEvaluationView.setNextView(position + 1);
        }

    }
}
