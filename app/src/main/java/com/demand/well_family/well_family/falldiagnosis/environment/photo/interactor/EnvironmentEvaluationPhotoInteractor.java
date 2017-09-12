package com.demand.well_family.well_family.falldiagnosis.environment.photo.interactor;

import android.net.Uri;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.RealPathUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-30.
 */

public interface EnvironmentEvaluationPhotoInteractor {

    FallDiagnosisCategory getFallDiagnosisCategory();

    void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory);

    FallDiagnosisContentCategory getFallDiagnosisContentCategory();

    void setFallDiagnosisContentCategory(FallDiagnosisContentCategory fallDiagnosisContentCategory);

    ArrayList<Integer> getAnswerList();

    void setAnswerList(ArrayList<Integer> answerList);

    ArrayList<String> getPathList();

    void setPathList(ArrayList<String> pathList);

    ArrayList<Uri> getPhotoList();

    void setPhotoList(ArrayList<Uri> photoList);

    User getUser();

    void setUser(User user);

    void setPhotoPath(RealPathUtil realPathUtil, Uri uri);

    void setPhotoPathAndUri(String path, Uri uri);

    int getEnvironmentEvaluationCategorySize();

    void setEnvironmentEvaluationCategorySize(int environmentEvaluationCategorySize);
}
