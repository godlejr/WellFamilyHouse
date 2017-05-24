package com.demand.well_family.well_family.falldiagnosis.base.view;

import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.falldiagnosis.base.adapter.FallDiagnosisCategoryAdapter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public interface FallDiagnosisView {
    void init();

    void navigateToFallEvaluationActivity(FallDiagnosisCategory fallDiagnosisCategory);

    void navigateToPhysicalEvaluationActivity(FallDiagnosisCategory fallDiagnosisCategory);

    void navigateToEvEvaluationActivity(FallDiagnosisCategory fallDiagnosisCategory);

    void showToolbarTitle(String title);

    View getDecorView();

    void setToolbar(View decorView);

    void showMessage(String message);

    void setCategoryAdapterInit(ArrayList<FallDiagnosisCategory> categoryList);

    void setBackgroundColorForSelfDiagnosis(FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder holder);

    void setBackgroundColorForBodyEvaluation(FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder holder);

    void setBackgroundColorForEvEvaluation(FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder holder);
}
