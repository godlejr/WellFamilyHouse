package com.demand.well_family.well_family.main.agreement.interator.impl;

import com.demand.well_family.well_family.main.agreement.interator.AgreementInterator;
import com.demand.well_family.well_family.main.agreement.presenter.AgreementPresenter;



public class AgreementInteratorImpl implements AgreementInterator{
    private AgreementPresenter agreementPresenter;
    private boolean agreementCheck;

    public AgreementInteratorImpl(AgreementPresenter agreementPresenter){
        this.agreementPresenter = agreementPresenter;
        this.agreementCheck = false;
    }

    @Override
    public boolean isAgreementCheck() {
        return agreementCheck;
    }

    @Override
    public void setAgreementCheck(boolean agreementCheck) {
        this.agreementCheck = agreementCheck;
    }
}
