package com.demand.well_family.well_family.falldiagnosis.environment.photo.view;

import android.net.Uri;
import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-30.
 */

public interface EnvironmentEvaluationPhotoView {

    void init();

    void showMessage(String message);

    void showContent(String message);

    View getDecorView();

    void setToolbar(View decorView);

    void showToolbar(String message);

    void setPermission();

    void navigateToBack();

    void setNoPhotoView();

    void showCamera();

    void setPhotoItem(ArrayList<Uri> photoList);

    void showPhotoAdapterNotifyDataChanged();

    void setPhotoAdapterDelete(int position);

    void goneInflatedView();

    void showPhotoSize(String message);
    void navigateToEnvironmentEvaluationResultActivity(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory, ArrayList<Integer> answerList, int environmentEvaluationCategorySize);


    void navigateToEnvironmentEvaluationResultActivity(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory, ArrayList<Integer> answerList, ArrayList<Uri> photoList, ArrayList<String> pathList, int environmentEvaluationCategorySize);

}
