package com.demand.well_family.well_family.falldiagnosis.environment.base.interactor;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public interface EnvironmentEvaluationInteractor {
    void getDangerEvaluationList();

    FallDiagnosisCategory getFallDiagnosisCategory();
    void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory);

    User getUser();

    void setUser(User user);
}
