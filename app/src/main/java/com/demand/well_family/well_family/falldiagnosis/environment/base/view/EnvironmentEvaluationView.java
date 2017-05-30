package com.demand.well_family.well_family.falldiagnosis.environment.base.view;

import android.view.View;

import com.demand.well_family.well_family.dto.Evaluation;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.falldiagnosis.environment.base.adapter.EnvironmentEvaluationAdapter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public interface EnvironmentEvaluationView {
    void init();
    void setToolbar(View decorView);
    void showToolbarTitle(String title);
    View getDecorView();
    void showMessage(String message);

    void setDangerEvaluationAdapterInit(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList);

    void navigateToCreateEnvironmentEvaluationActivity(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory);
}
