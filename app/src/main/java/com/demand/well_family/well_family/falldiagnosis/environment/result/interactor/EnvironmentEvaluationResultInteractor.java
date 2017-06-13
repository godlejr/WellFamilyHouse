package com.demand.well_family.well_family.falldiagnosis.environment.result.interactor;

import android.net.Uri;

import com.demand.well_family.well_family.dto.EnvironmentEvaluationStatus;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.FileToBase64Util;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-31.
 */

public interface EnvironmentEvaluationResultInteractor {

    User getUser();

    void setUser(User user);

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

    int getEnvironmentEvaluationCategorySize();

    void setEnvironmentEvaluationCategorySize(int environmentEvaluationCategorySize);

    FallDiagnosisStory getFallDiagnosisStory();

    void setFallDiagnosisStory(FallDiagnosisStory fallDiagnosisStory);

    void setStoryAdded();

    void setEnvironmentEvaluationAdded(int storyId, int environmentEvaluationCategoryId);

    void setPhotoAdded(FileToBase64Util fileToBase64Util, int storyId, Uri photo, String path);

    int getFallDiagnosisRiskCategoryId();

    void setFallDiagnosisRiskCategoryId(int fallDiagnosisRiskCategoryId);


    void setEnvironmentEvaluationStatus(EnvironmentEvaluationStatus environmentEvaluationStatus);
}
