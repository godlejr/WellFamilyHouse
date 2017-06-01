package com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.create.view;

import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public interface SelfDiagnosisView {
    void init();

    View getDecorView();
    void setToolbar(View decorView);
    void showToolbarTitle(String title);

    void showMessage(String message);

    void setNextView(int position);
    void setPreviousView(int position);

    void navigateToResultActivity(FallDiagnosisCategory fallDiagnosisCategory, ArrayList<Integer> answerList, int fallDiagnosisContentCategorySize);

    void setDiagnosisCategoryAdapter(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList);
}
