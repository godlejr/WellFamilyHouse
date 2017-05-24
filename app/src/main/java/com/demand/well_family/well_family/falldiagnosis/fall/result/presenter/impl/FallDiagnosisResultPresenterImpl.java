package com.demand.well_family.well_family.falldiagnosis.fall.result.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.falldiagnosis.fall.result.interactor.FallDiagnosisResultInteractor;
import com.demand.well_family.well_family.falldiagnosis.fall.result.interactor.impl.FallDiagnosisResultInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.fall.result.presenter.FallDiagnosisResultPresenter;
import com.demand.well_family.well_family.falldiagnosis.fall.result.view.FallDiagnosisResultView;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class FallDiagnosisResultPresenterImpl implements FallDiagnosisResultPresenter{
    private FallDiagnosisResultInteractor fallDiagnosisResultInteractor;
    private FallDiagnosisResultView fallDiagnosisResultView;
    private PreferenceUtil preferenceUtil;

    public FallDiagnosisResultPresenterImpl(Context context) {
        this.fallDiagnosisResultInteractor = new FallDiagnosisResultInteractorImpl(this);
        this.fallDiagnosisResultView = (FallDiagnosisResultView)context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {

    }
}
