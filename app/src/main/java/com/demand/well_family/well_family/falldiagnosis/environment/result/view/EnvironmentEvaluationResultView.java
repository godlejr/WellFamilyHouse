package com.demand.well_family.well_family.falldiagnosis.environment.result.view;

import android.view.View;

/**
 * Created by Dev-0 on 2017-05-31.
 */

public interface EnvironmentEvaluationResultView {


    void init();

    View getDecorView();

    void setToolbar(View decorView);

    void showToolbarTitle(String title);

    void showTotalCount(int count);

    void showProgressbar(int score, int count);

    void showScore(int score);

    void showScoreTextChangeColorWithSafe();

    void showScoreTextChangeColorWithCaution();

    void showScoreTextChangeColorWithRisk();

    void showTotalCountTextChangeColorWithSafe();

    void showTotalCountTextChangeColorWithCaution();

    void showTotalCountTextChangeColorWithRisk();

    void showProgressBarChangeColorWithSafe();

    void showProgressBarChangeColorWithCaution();

    void showProgressBarChangeColorWithRisk();

    void showProgressDialog();

    void setProgressDialog(int position);

    void goneProgressDialog();

    void showResult(String message);

    void showMessage(String message);

    void navigateToBack();
}
