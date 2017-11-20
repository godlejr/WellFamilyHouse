package com.demand.well_family.well_family.falldiagnosis.environment.photo.presenter;

import android.net.Uri;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-30.
 */

public interface EnvironmentEvaluationPhotoPresenter {
    void onCreate(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory, ArrayList<Integer> answerList , int environmentEvaluationCategorySize);

    void onLoadData();

    void onRequestPermissionsResultForCameraExternalStorage(int[] grantResults);

    void onClickCamera();

    void onActivityResultForCameraUriResultOk(Uri uri, String path);

    void onActivityResultForCameraUriResultOk(Uri uri);

    void onClickPhotoDelete(int position);

    void onClickJump();

    void onClickSave();

    void onClickNotDefaultCamera();
}
