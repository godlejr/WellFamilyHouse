package com.demand.well_family.well_family.falldiagnosis.base.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.falldiagnosis.base.interactor.FallDiagnosisInteractor;
import com.demand.well_family.well_family.falldiagnosis.base.interactor.impl.FallDiagnosisInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.base.presenter.FallDiagnosisPresenter;
import com.demand.well_family.well_family.falldiagnosis.base.view.FallDiagnosisView;
import com.demand.well_family.well_family.util.PreferenceUtil;

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
        fallDiagnosisView.init();

        View decorView = fallDiagnosisView.getDecorView();
        fallDiagnosisView.setToolbar(decorView);
        fallDiagnosisView.showToolbarTitle("낙상 위험 진단");
    }
}
