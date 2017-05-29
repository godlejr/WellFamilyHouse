package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.interactor.PhysicalEvaluationResultInteractor;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.interactor.impl.PhysicalEvaluationResultInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.presenter.PhysicalEvaluationResultPresenter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.view.PhysicalEvaluationResultView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public class PhysicalEvaluationResultPresenterImpl implements PhysicalEvaluationResultPresenter{
    private PhysicalEvaluationResultInteractor physicalEvaluationResultInteractor;
    private PhysicalEvaluationResultView physicalEvaluationResultView;
    private PreferenceUtil preferenceUtil;

    public PhysicalEvaluationResultPresenterImpl(Context context) {
        this.physicalEvaluationResultInteractor = new PhysicalEvaluationResultInteractorImpl(this);
        this.physicalEvaluationResultView = (PhysicalEvaluationResultView)context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            physicalEvaluationResultView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            physicalEvaluationResultView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onClickConfirm() {

    }
}
