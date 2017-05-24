package com.demand.well_family.well_family.falldiagnosis.fall.result.interactor.impl;

import com.demand.well_family.well_family.falldiagnosis.fall.result.interactor.FallDiagnosisResultInteractor;
import com.demand.well_family.well_family.falldiagnosis.fall.result.presenter.FallDiagnosisResultPresenter;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class FallDiagnosisResultInteractorImpl implements FallDiagnosisResultInteractor{
    private FallDiagnosisResultPresenter fallDiagnosisResultPresenter;

    public FallDiagnosisResultInteractorImpl(FallDiagnosisResultPresenter fallDiagnosisResultPresenter) {
        this.fallDiagnosisResultPresenter = fallDiagnosisResultPresenter;
    }
}
