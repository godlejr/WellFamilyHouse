package com.demand.well_family.well_family.main.agreement.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.main.agreement.interator.AgreementInterator;
import com.demand.well_family.well_family.main.agreement.interator.impl.AgreementInteratorImpl;
import com.demand.well_family.well_family.main.agreement.presenter.AgreementPresenter;
import com.demand.well_family.well_family.main.agreement.view.AgreementView;



public class AgreementPresenterImpl implements AgreementPresenter {
    private AgreementView agreementView;
    private AgreementInterator agreementInterator;

    public AgreementPresenterImpl(Context context){
        this.agreementView = (AgreementView) context;
        this.agreementInterator = new AgreementInteratorImpl(this);
    }


    @Override
    public void onCreate() {
        agreementView.init();
        View decorView = agreementView.getDecorView();
        agreementView.setToolbar(decorView);
        agreementView.showToolbarTitle("약관동의");
    }

    @Override
    public void onClickAgreement(boolean agreement1Check, boolean agreement2Check) {
        if(agreement1Check && agreement2Check){
            agreementView.navigateToJoinActivity();
        }else{
            agreementView.showMessage("이용약관에 동의하세요.");
        }
    }

    @Override
    public void onCheckedChangedAgreementAll(boolean agreementAllCheck) {
        boolean agreementCheck = agreementInterator.isAgreementCheck();

        if(agreementAllCheck){
            agreementView.setAgreement1Check(true);
            agreementView.setAgreement2Check(true);
        }else{
            if(agreementCheck){
                agreementView.setAgreement1Check(false);
                agreementView.setAgreement2Check(false);
            }
        }
        agreementInterator.setAgreementCheck(!agreementCheck);
    }

    @Override
    public void onCheckedChangedAgreement(boolean agreement1Check, boolean agreement2Check) {

        if(agreement1Check && agreement2Check){
            agreementView.setAgreementAll(true);
        }else if(!agreement1Check || !agreement2Check){
            boolean agreementCheck = agreementInterator.isAgreementCheck();
            agreementInterator.setAgreementCheck(!agreementCheck);
            agreementView.setAgreementAll(false);
        }
    }


}
