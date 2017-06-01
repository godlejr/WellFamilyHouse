package com.demand.well_family.well_family.falldiagnosis.environment.create.view;

import android.view.View;

import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-30.
 */

public interface CreateEnvironmentEvaluationView {
    void init();

    void showMessage(String message);

    View getDecorView();

    void setToolbar(View decorView);

    void showToolbar(String message);

    void setCreateEnvironmentEvaluationAdapterInit(ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList);

    void setNextView(int position);


    void navigateToEnvironmentEvaluationAPhotoActivity(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory, ArrayList<Integer> answerList, int environmentEvaluationCategorySize);
}
