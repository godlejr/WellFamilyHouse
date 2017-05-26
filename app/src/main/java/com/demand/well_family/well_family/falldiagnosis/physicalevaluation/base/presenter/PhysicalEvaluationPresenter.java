package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.presenter;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public interface PhysicalEvaluationPresenter {
    void onCreate(FallDiagnosisCategory fallDiagnosisCategory);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onLoadData();

    void onSuccessGetPhysicalEvaluationCategories(ArrayList<FallDiagnosisContentCategory> physicalEvaluationList);

    void onClickStart();

}
