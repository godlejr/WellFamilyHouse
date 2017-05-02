package com.demand.well_family.well_family.falldiagnosis.base.view;

import android.view.View;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public interface FallDiagnosisView {
    void init();

    void navigateToFallEvaluationActivity();
    void navigateToPhysicalEvaluationActivity();
    void navigateToDangerActivity();

    void showToolbarTitle(String title);
    View getDecorView();
    void setToolbar(View decorView);

}
