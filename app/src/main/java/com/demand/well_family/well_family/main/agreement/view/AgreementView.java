package com.demand.well_family.well_family.main.agreement.view;

import android.view.View;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public interface AgreementView {
    void init();

    void setToolbar(View view);

    View getDecorView();

    void setAgreementAll(boolean isCheck);

    void setAgreement1Check(boolean isCheck);

    void setAgreement2Check(boolean isCheck);

    void showToolbarTitle(String message);


    void showMessage(String message);

    void navigateToBack();

    void navigateToJoinActivity();

}
