package com.demand.well_family.well_family.setting.deactivation.view;

import android.view.View;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public interface DeactivationView {
    void init();

    // toolbar
    void setToolbar(View decorView);
    View getDecorView();
    void showToolbarTitle(String message);

    void navigateToReasonForDeactivation();
    void navigateToBack();
}
