package com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.flag.SelfDiagnosisCodeFlag;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.interactor.SelfDiagnosisInteractor;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.interactor.impl.SelfDiagnosisInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter.SelfDiagnosisPresenter;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.view.SelfDiagnosisView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class SelfDiagnosisPresenterImpl implements SelfDiagnosisPresenter {
    private SelfDiagnosisInteractor selfDiagnosisInteractor;
    private SelfDiagnosisView fallDiagnosisView;
    private PreferenceUtil preferenceUtil;

    public SelfDiagnosisPresenterImpl(Context context) {
        this.selfDiagnosisInteractor = new SelfDiagnosisInteractorImpl(this);
        this.fallDiagnosisView = (SelfDiagnosisView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(FallDiagnosisCategory fallDiagnosisCategory) {
        User user = preferenceUtil.getUserInfo();
        selfDiagnosisInteractor.setUser(user);
        selfDiagnosisInteractor.setFallDiagnosisCategory(fallDiagnosisCategory);

        fallDiagnosisView.init();

        View decorView = fallDiagnosisView.getDecorView();
        fallDiagnosisView.setToolbar(decorView);
        fallDiagnosisView.showToolbarTitle("낙상위험 자가진단");
    }


    @Override
    public void onClickAnswer(int position, int categorySize, int flag) {
        int endOfCategoryList = categorySize - 1;

        if (flag == SelfDiagnosisCodeFlag.YES) {
            selfDiagnosisInteractor.setAnswerAdded(true);
        }

        if (flag == SelfDiagnosisCodeFlag.NO) {
            selfDiagnosisInteractor.setAnswerAdded(false);
        }

        if (position == endOfCategoryList) {
            FallDiagnosisCategory fallDiagnosisCategory = selfDiagnosisInteractor.getFallDiagnosisCategory();
            ArrayList<Boolean> answerList = selfDiagnosisInteractor.getAnswerList();
            fallDiagnosisView.navigateToResultActivity(fallDiagnosisCategory, answerList);
        } else {
            fallDiagnosisView.setNextView(position + 1);
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
    public void onSuccessGetDiagnosisCategories(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList) {
        fallDiagnosisView.setDiagnosisCategoryAdapter(fallDiagnosisContentCategoryList);
    }

    @Override
    public void onLoadData() {
        User user = preferenceUtil.getUserInfo();
        selfDiagnosisInteractor.getDiagnosisCategories(user);
    }
}
