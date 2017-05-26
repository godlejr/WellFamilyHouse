package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.view;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public interface PhysicalEvaluationView {
    void init();

    void showToolbarTitle(String message);

    View getDecorView();

    void setToolbar(View decorView);

    void showMessage(String message);

    void setPhysicalEvaluationAdapterInit(ArrayList<String> physicalEvaluationList);

    void navigateToCreatePhysicalEvaluationActivity();
}
