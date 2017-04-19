package com.demand.well_family.well_family.setting.deactivationReason.interactor.impl;

import com.demand.well_family.well_family.setting.deactivationReason.interactor.DeactivationReasonInteractor;
import com.demand.well_family.well_family.setting.deactivationReason.presenter.DeactivationReasonPresenter;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public class DeactivationReasonInteractorImpl implements DeactivationReasonInteractor {
    private DeactivationReasonPresenter deactivationReasonPresenter;

    public DeactivationReasonInteractorImpl(DeactivationReasonPresenter deactivationReasonPresenter) {
        this.deactivationReasonPresenter = deactivationReasonPresenter;
    }
}
