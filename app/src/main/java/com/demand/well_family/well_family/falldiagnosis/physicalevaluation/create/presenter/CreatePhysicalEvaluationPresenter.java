package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public interface CreatePhysicalEvaluationPresenter {
    void onCreate(FallDiagnosisCategory fallDiagnosisCategory);
    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onLoadData();

    void onSuccessGetPhysicalEvaluationCategories(ArrayList<PhysicalEvaluationCategory> physicalEvaluationList);

    void onClickNext(int position, int count);

}
