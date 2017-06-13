package com.demand.well_family.well_family.falldiagnosis.environment.result.presenter;

import android.net.Uri;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-31.
 */

public interface EnvironmentEvaluationResultPresenter {

    void onCreate(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory, ArrayList<Integer> answerList,int environmentEvaluationCategorySize, ArrayList<Uri> photoList, ArrayList<String> pathList);


    void onLoadData();

    void onClickSendResult();

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onSuccessSetStoryAdded(int storyId);

}
