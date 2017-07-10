package com.demand.well_family.well_family.falldiagnosis.base.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.base.adapter.FallDiagnosisCategoryAdapter;
import com.demand.well_family.well_family.falldiagnosis.base.interactor.FallDiagnosisInteractor;
import com.demand.well_family.well_family.falldiagnosis.base.interactor.impl.FallDiagnosisInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.base.presenter.FallDiagnosisPresenter;
import com.demand.well_family.well_family.falldiagnosis.base.view.FallDiagnosisView;
import com.demand.well_family.well_family.flag.FallDiagnosisFlag;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class FallDiagnosisPresenterImpl implements FallDiagnosisPresenter {
    private FallDiagnosisInteractor fallDiagnosisInteractor;
    private FallDiagnosisView fallDiagnosisView;
    private PreferenceUtil preferenceUtil;

    public FallDiagnosisPresenterImpl(Context context) {
        this.fallDiagnosisView = (FallDiagnosisView) context;
        this.fallDiagnosisInteractor = new FallDiagnosisInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        User user = preferenceUtil.getUserInfo();
        fallDiagnosisInteractor.setUser(user);

        View decorView = fallDiagnosisView.getDecorView();
        fallDiagnosisView.setToolbar(decorView);
        fallDiagnosisView.showToolbarTitle("낙상 위험 진단");

        fallDiagnosisView.init();
    }

    @Override
    public String setCategoryContent(FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder holder, int categoryId) {
        String content = null;

        if (categoryId == FallDiagnosisFlag.SELF_DIAGNOSIS) {
            content = "자가진단 설문지를 통해\n나의 낙상위험정도를 알아봅시다.";
            fallDiagnosisView.setBackgroundColorForSelfDiagnosis(holder);
        }

        if (categoryId == FallDiagnosisFlag.PHYSICAL_EVALUATION) {
            content = "낙상 예방의 중요한 운동 능력인\n균형감, 움직임, 하지근력의 기능을 평가합니다.";
            fallDiagnosisView.setBackgroundColorForBodyEvaluation(holder);
        }

        if (categoryId == FallDiagnosisFlag.RISK_EVALUATION) {
            content = "실내 환경을 점검하여\n낙상 위험 요인을 진단합니다.";
            fallDiagnosisView.setBackgroundColorForEvEvaluation(holder);
        }

        return content;
    }

    @Override
    public void getDiagnosisCategory() {
        fallDiagnosisInteractor.getCategoryList();
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
    public void onSuccessGetCategoryList(ArrayList<FallDiagnosisCategory> fallDiagnosisCategoryList) {
        fallDiagnosisView.setCategoryAdapterInit(fallDiagnosisCategoryList);
    }

    @Override
    public void onClickCategory(FallDiagnosisCategory fallDiagnosisCategory) {
        int categoryId = fallDiagnosisCategory.getId();

        if (categoryId == FallDiagnosisFlag.SELF_DIAGNOSIS) {
            fallDiagnosisView.navigateToFallEvaluationActivity(fallDiagnosisCategory);
        }
        if (categoryId == FallDiagnosisFlag.PHYSICAL_EVALUATION) {
            fallDiagnosisView.navigateToPhysicalEvaluationActivity(fallDiagnosisCategory);
        }
        if (categoryId == FallDiagnosisFlag.RISK_EVALUATION) {
            fallDiagnosisView.navigateToEvEvaluationActivity(fallDiagnosisCategory);
        }
    }
}
