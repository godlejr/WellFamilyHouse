package com.demand.well_family.well_family.family.managedetail.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.family.managedetail.interactor.ManageFamilyDetailInteractor;
import com.demand.well_family.well_family.family.managedetail.interactor.impl.ManageFamilyDetailInteractorImpl;
import com.demand.well_family.well_family.family.managedetail.presenter.ManageFamilyDetailPresenter;
import com.demand.well_family.well_family.family.managedetail.view.ManageFamilyDetailView;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public class ManageFamilyDetailPresenterImpl implements ManageFamilyDetailPresenter {
    private ManageFamilyDetailView manageFamilyDetailView;
    private ManageFamilyDetailInteractor manageFamilyDetailInteractor;
    private PreferenceUtil preferenceUtil;


    public ManageFamilyDetailPresenterImpl(Context context){
        this.manageFamilyDetailView = (ManageFamilyDetailView) context;
        this.manageFamilyDetailInteractor = new ManageFamilyDetailInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

}
