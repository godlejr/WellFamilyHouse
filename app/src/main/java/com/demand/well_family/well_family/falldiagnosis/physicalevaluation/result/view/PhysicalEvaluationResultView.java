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

    void showTotalScore(String score);


    void showTotalScoreTextChangeColorWithSafe();

    void showTotalScoreTextChangeColorWithCaution();

    void showTotalScoreTextChangeColorWithRisk();

    void showProgressBarChangeColorWithSafe();

    void showProgressBarChangeColorWithCaution();

    void showProgressBarChangeColorWithRisk();

    void showBalanceScore(String score);

    void showMovementScore(String score);

    void showLegStrengthScore(String score);

    void showResult(String message);

    void navigateToBack();

}
