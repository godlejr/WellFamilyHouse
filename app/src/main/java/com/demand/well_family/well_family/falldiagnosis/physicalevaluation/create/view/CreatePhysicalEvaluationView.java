package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.view;

import android.view.View;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public interface CreatePhysicalEvaluationView {
    void init();

    void showToolbarTitle(String message);

    View getDecorView();

    void setToolbar(View decorView);

    void showMessage(String message);
}
