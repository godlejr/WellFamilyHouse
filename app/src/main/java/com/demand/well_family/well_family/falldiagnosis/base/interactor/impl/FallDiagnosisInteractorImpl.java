package com.demand.well_family.well_family.falldiagnosis.base.interactor.impl;

import com.demand.well_family.well_family.falldiagnosis.base.interactor.FallDiagnosisInteractor;
import com.demand.well_family.well_family.falldiagnosis.base.presenter.FallDiagnosisPresenter;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class FallDiagnosisInteractorImpl implements FallDiagnosisInteractor{
    private FallDiagnosisPresenter fallDiagnosisPresenter;

    public FallDiagnosisInteractorImpl(FallDiagnosisPresenter fallDiagnosisPresenter) {
        this.fallDiagnosisPresenter = fallDiagnosisPresenter;
    }
}
