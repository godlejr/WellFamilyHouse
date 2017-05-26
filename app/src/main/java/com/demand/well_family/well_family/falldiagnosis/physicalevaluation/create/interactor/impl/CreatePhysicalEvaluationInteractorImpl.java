package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.interactor.impl;

import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.interactor.CreatePhysicalEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.CreatePhysicalEvaluationPresenter;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public class CreatePhysicalEvaluationInteractorImpl implements CreatePhysicalEvaluationInteractor {
    private CreatePhysicalEvaluationPresenter createPhysicalEvaluationPresenter;

    public CreatePhysicalEvaluationInteractorImpl(CreatePhysicalEvaluationPresenter createPhysicalEvaluationPresenter) {
        this.createPhysicalEvaluationPresenter = createPhysicalEvaluationPresenter;
    }
}
