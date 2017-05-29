package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.view;

import android.view.View;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public interface PhysicalEvaluationResultView {
    void init();

    void showMessage(String message);

    View getDecorView();

    void setToolbar(View decorView);

    void showToolbarTitle(String message);

    void showProgressbar(int score, int count);

}
