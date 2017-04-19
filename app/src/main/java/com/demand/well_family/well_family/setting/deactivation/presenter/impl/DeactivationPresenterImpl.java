package com.demand.well_family.well_family.setting.deactivation.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.setting.deactivation.interator.DeactivationInterator;
import com.demand.well_family.well_family.setting.deactivation.interator.impl.DeactivationInteratorImpl;
import com.demand.well_family.well_family.setting.deactivation.presenter.DeactivationPresenter;
import com.demand.well_family.well_family.setting.deactivation.view.DeactivationView;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public class DeactivationPresenterImpl implements DeactivationPresenter {
    private DeactivationView deactivationView;
    private DeactivationInterator deactivationInterator;

    public DeactivationPresenterImpl(Context context) {
        this.deactivationView = (DeactivationView) context;
        this.deactivationInterator = new DeactivationInteratorImpl(this);
    }

    @Override
    public void onCreate() {
        deactivationView.init();

        setToolbar();
    }

    @Override
    public void setToolbar() {
        View decorView = deactivationView.getDecorView();
        deactivationView.setToolbar(decorView);
        deactivationView.showToolbarTitle("계정 비활성화");
    }
}
