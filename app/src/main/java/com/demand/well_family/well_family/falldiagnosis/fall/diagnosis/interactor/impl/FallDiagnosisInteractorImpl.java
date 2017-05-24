package com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.interactor.impl;

import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.interactor.FallDiagnosisInteractor;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter.FallDiagnosisPresenter;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class FallDiagnosisInteractorImpl implements FallDiagnosisInteractor{
    private FallDiagnosisPresenter fallDiagnosisPresenter;

    public FallDiagnosisInteractorImpl(FallDiagnosisPresenter fallDiagnosisPresenter) {
        this.fallDiagnosisPresenter = fallDiagnosisPresenter;
    }
}
