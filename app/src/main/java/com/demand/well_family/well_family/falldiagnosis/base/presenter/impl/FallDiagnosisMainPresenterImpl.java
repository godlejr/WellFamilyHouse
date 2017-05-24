package com.demand.well_family.well_family.falldiagnosis.base.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.falldiagnosis.base.interactor.FallDiagnosisMainInteractor;
import com.demand.well_family.well_family.falldiagnosis.base.interactor.impl.FallDiagnosisMainInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.base.presenter.FallDiagnosisMainPresenter;
import com.demand.well_family.well_family.falldiagnosis.base.view.FallDiagnosisMainView;
import com.demand.well_family.well_family.util.PreferenceUtil;

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
        fallDiagnosisView.init();

        View decorView = fallDiagnosisView.getDecorView();
        fallDiagnosisView.setToolbar(decorView);
        fallDiagnosisView.showToolbarTitle("낙상 위험 진단");
    }
}
