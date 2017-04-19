package com.demand.well_family.well_family.setting.deactivationReason.view;

import android.view.View;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public interface DeactivationReasonView {
    void init();

    void setDeactivationPeriodSpinner(String[] spinnerArray);

    void showMessage(String message);

    void showGuidance(int position);
    void goneGuidance(int position);

    void navigateToDeactivationPopupActivity();
    void navigateToLoginActivity();
    void navigateToBack();

    //toolbar
    View getDecorView();
    void setToolbar(View decorView);
    void showToolbarTitle(String message);

}
