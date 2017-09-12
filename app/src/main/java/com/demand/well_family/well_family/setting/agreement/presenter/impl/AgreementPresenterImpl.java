package com.demand.well_family.well_family.setting.agreement.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.setting.agreement.interator.AgreementInterator;
import com.demand.well_family.well_family.setting.agreement.interator.impl.AgreementInteratorImpl;
import com.demand.well_family.well_family.setting.agreement.presenter.AgreementPresenter;
import com.demand.well_family.well_family.setting.agreement.view.AgreementView;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public class AgreementPresenterImpl implements AgreementPresenter {
    private AgreementView agreementView;
    private AgreementInterator agreementInterator;

    public AgreementPresenterImpl(Context context) {
        this.agreementView = (AgreementView) context;
        this.agreementInterator = new AgreementInteratorImpl(this);
    }

    @Override
    public void onCreate() {
        agreementView.init();

        setToolbar();
    }

    @Override
    public void setToolbar() {
        View decorView = agreementView.getDecorView();
        agreementView.setToolbar(decorView);
        agreementView.showToolbarTitle("약관 및 정책");
    }
}
