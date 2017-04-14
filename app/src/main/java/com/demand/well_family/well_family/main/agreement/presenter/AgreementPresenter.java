package com.demand.well_family.well_family.main.agreement.presenter;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public interface AgreementPresenter {
    void onCreate();

    void onClickAgreement(boolean agreement1Check, boolean agreement2Check);

    void onCheckedChangedAgreementAll(boolean agreementAllCheck);

    void onCheckedChangedAgreement(boolean agreement1Check, boolean agreement2Check);
}
