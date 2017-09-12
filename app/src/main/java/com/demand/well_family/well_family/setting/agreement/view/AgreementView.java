package com.demand.well_family.well_family.setting.agreement.view;

import android.view.View;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public interface AgreementView {
    void init();

    // toolbar
    void setToolbar(View decorView);
    View getDecorView();
    void showToolbarTitle(String message);

    void navigateToBack();
}
