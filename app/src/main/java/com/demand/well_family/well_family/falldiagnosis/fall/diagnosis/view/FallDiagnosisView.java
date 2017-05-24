package com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.view;

import android.view.View;

import com.demand.well_family.well_family.dto.SelfDiagnosisCategory;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public interface FallDiagnosisView {
    void init();

    View getDecorView();
    void setToolbar(View decorView);
    void showToolbarTitle(String title);

    void showMessage(String message);

    void setNextView(int page);
    void setPreviousView(int page);

    void navigateToResultActivity();

    int getDiagnosisItemCount();

    void setDiagnosisCategoryAdapter(ArrayList<SelfDiagnosisCategory> diagnosisCategoryList);
}
