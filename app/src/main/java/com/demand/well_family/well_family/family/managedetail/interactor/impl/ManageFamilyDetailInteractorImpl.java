package com.demand.well_family.well_family.family.managedetail.interactor.impl;

import com.demand.well_family.well_family.family.managedetail.interactor.ManageFamilyDetailInteractor;
import com.demand.well_family.well_family.family.managedetail.presenter.ManageFamilyDetailPresenter;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public class ManageFamilyDetailInteractorImpl implements ManageFamilyDetailInteractor {
    private ManageFamilyDetailPresenter manageFamilyDetailPresenter;

    public ManageFamilyDetailInteractorImpl(ManageFamilyDetailPresenter manageFamilyDetailPresenter) {
        this.manageFamilyDetailPresenter = manageFamilyDetailPresenter;
    }
}
