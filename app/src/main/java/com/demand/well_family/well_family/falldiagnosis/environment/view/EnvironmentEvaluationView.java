package com.demand.well_family.well_family.falldiagnosis.environment.view;

import android.view.View;

import com.demand.well_family.well_family.dto.Evaluation;
import com.demand.well_family.well_family.falldiagnosis.environment.adapter.EnvironmentEvaluationAdapter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public interface EnvironmentEvaluationView {
    void init();
    void setToolbar(View decorView);
    void showToolbarTitle(String title);
    View getDecorView();

    void setDangerEvaluationList(ArrayList<Evaluation> dangerEvaluationList);
    void setDangerEvaluationAdapter(EnvironmentEvaluationAdapter environmentEvaluationAdapter);
}
