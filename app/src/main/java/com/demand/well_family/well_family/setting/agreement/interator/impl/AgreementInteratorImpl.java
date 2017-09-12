package com.demand.well_family.well_family.setting.agreement.interator.impl;

import com.demand.well_family.well_family.setting.agreement.interator.AgreementInterator;
import com.demand.well_family.well_family.setting.agreement.presenter.AgreementPresenter;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public class AgreementInteratorImpl implements AgreementInterator {
    AgreementPresenter agreementPresenter;

    public AgreementInteratorImpl(AgreementPresenter agreementPresenter) {
        this.agreementPresenter = agreementPresenter;
    }

}
