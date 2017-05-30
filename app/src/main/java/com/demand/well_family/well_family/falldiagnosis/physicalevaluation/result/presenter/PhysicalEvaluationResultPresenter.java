package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.presenter;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluation;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public interface PhysicalEvaluationResultPresenter {
    void onCreate(FallDiagnosisCategory fallDiagnosisCategory, ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList, ArrayList<PhysicalEvaluation> physicalEvaluationList);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onClickSendResult();

    void onLoadData();

    void onSuccessSetStoryAdded(int storyId);

    void onSuccessSetPhysicalEvaluationScoreAdded();
}
