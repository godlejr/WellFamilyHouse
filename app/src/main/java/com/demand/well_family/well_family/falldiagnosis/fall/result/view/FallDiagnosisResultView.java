package com.demand.well_family.well_family.falldiagnosis.fall.result.view;

import android.view.View;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public interface FallDiagnosisResultView {
    void init();

    View getDecorView();
    void setToolbar(View decorView);
    void showToolbarTitle(String title);

    void showMessage(String message);
}
