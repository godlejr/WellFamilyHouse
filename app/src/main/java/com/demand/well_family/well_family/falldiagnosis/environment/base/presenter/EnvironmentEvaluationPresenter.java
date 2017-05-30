package com.demand.well_family.well_family.falldiagnosis.environment.base.presenter;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public interface EnvironmentEvaluationPresenter {
    void onCreate(FallDiagnosisCategory fallDiagnosisCategory);
    void onLoadData();

    void onSuccessGetDiagnosisCategories(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList);
    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onClickEnvironmentEvaluationItem(FallDiagnosisContentCategory fallDiagnosisContentCategory);
}
