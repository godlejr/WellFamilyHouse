package com.demand.well_family.well_family.falldiagnosis.environment.base.interactor;

import com.demand.well_family.well_family.dto.Evaluation;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public interface EnvironmentEvaluationInteractor {
    void getDangerEvaluationList(User user);

    FallDiagnosisCategory getFallDiagnosisCategory();
    void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory);
}
