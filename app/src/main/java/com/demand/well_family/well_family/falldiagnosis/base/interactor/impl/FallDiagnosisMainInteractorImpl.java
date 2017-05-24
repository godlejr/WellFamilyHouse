package com.demand.well_family.well_family.falldiagnosis.base.interactor.impl;

import com.demand.well_family.well_family.falldiagnosis.base.interactor.FallDiagnosisMainInteractor;
import com.demand.well_family.well_family.falldiagnosis.base.presenter.FallDiagnosisMainPresenter;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class FallDiagnosisMainInteractorImpl implements FallDiagnosisMainInteractor {
    private FallDiagnosisMainPresenter fallDiagnosisPresenter;

    public FallDiagnosisMainInteractorImpl(FallDiagnosisMainPresenter fallDiagnosisPresenter) {
        this.fallDiagnosisPresenter = fallDiagnosisPresenter;
    }
}
