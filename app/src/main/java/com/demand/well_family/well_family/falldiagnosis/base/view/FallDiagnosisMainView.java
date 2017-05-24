package com.demand.well_family.well_family.falldiagnosis.base.view;

import android.view.View;

import com.demand.well_family.well_family.dto.Category;
import com.demand.well_family.well_family.falldiagnosis.base.adapter.FallDiagnosisCategoryAdapter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public interface FallDiagnosisMainView {
    void init();

    void navigateToFallEvaluationActivity();

    void navigateToPhysicalEvaluationActivity();

    void navigateToEvEvaluationActivity();

    void showToolbarTitle(String title);

    View getDecorView();

    void setToolbar(View decorView);

    void showMessage(String message);

    void setCategoryAdapterInit(ArrayList<Category> categoryList);

    void setBackgroundColorForSelfDiagnosis(FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder holder);

    void setBackgroundColorForBodyEvaluation(FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder holder);

    void setBackgroundColorForEvEvaluation(FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder holder);
}
