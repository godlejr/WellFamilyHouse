package com.demand.well_family.well_family.dialog.popup.deactivation.interactor.impl;

import com.demand.well_family.well_family.dialog.popup.deactivation.interactor.DeactivationPopupInteractor;
import com.demand.well_family.well_family.dialog.popup.deactivation.presenter.DeactivationPopupPresenter;


/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public class DeactivationPopupInteractorImpl implements DeactivationPopupInteractor{
    private DeactivationPopupPresenter deactivationPopupPresenter;

    public DeactivationPopupInteractorImpl(DeactivationPopupPresenter deactivationPopupPresenter) {
        this.deactivationPopupPresenter = deactivationPopupPresenter;
    }


}
