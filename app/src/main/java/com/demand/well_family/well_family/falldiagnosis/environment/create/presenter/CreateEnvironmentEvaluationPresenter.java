package com.demand.well_family.well_family.falldiagnosis.environment.create.presenter;

import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-30.
 */

public interface CreateEnvironmentEvaluationPresenter {
    void onCreate( FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory);
    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onLoadData();
    void onSuccessGetEnvironmentEvaluationCategories(ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList);
    void onClickAnswer(int position, int categorySize, int flag);
}
