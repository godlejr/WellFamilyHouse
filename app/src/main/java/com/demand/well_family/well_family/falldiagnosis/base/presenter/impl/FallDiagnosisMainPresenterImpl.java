package com.demand.well_family.well_family.falldiagnosis.base.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.Category;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.base.adapter.FallDiagnosisCategoryAdapter;
import com.demand.well_family.well_family.falldiagnosis.base.interactor.FallDiagnosisMainInteractor;
import com.demand.well_family.well_family.falldiagnosis.base.interactor.impl.FallDiagnosisMainInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.base.presenter.FallDiagnosisMainPresenter;
import com.demand.well_family.well_family.falldiagnosis.base.view.FallDiagnosisMainView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class FallDiagnosisMainPresenterImpl implements FallDiagnosisMainPresenter {
    private FallDiagnosisMainInteractor fallDiagnosisInteractor;
    private FallDiagnosisMainView fallDiagnosisView;
    private PreferenceUtil preferenceUtil;

    public FallDiagnosisMainPresenterImpl(Context context) {
        this.fallDiagnosisView = (FallDiagnosisMainView) context;
        this.fallDiagnosisInteractor = new FallDiagnosisMainInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        View decorView = fallDiagnosisView.getDecorView();
        fallDiagnosisView.setToolbar(decorView);
        fallDiagnosisView.showToolbarTitle("낙상 위험 진단");

        fallDiagnosisView.init();
    }

    @Override
    public String setCategoryContent(FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder holder, int categoryId) {
        String content = "";
        switch (categoryId){
            case 1:
                content = "자가진단 설문지를 통해\n나의 낙상위험정도를 알아봅시다.";
                fallDiagnosisView.setBackgroundColorForSelfDiagnosis(holder);
                break;

            case 2:
                content = "낙상 예방의 중요한 운동 능력인\n균형감, 움직임, 하지근력의 기능을 평가합니다.";
                fallDiagnosisView.setBackgroundColorForBodyEvaluation(holder);
                break;

            case 3:
                content = "실내 환경을 점검하여\n낙상 위험 요인을 진단합니다.";
                fallDiagnosisView.setBackgroundColorForEvEvaluation(holder);
                break;
        }

        return content;
    }

    @Override
    public void getDiagnosisCategory() {
        User user = preferenceUtil.getUserInfo();
        fallDiagnosisInteractor.getCategoryList(user);
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
    public void onSuccessGetCategoryList(ArrayList<Category> categoryList) {
        fallDiagnosisView.setCategoryAdapterInit(categoryList);
    }
}
