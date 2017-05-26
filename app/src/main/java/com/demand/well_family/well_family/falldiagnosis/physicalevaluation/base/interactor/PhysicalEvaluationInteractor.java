package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.interactor;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public interface PhysicalEvaluationInteractor {
    void getPhysicalEvaluationCategories(User user);

    FallDiagnosisCategory getFallDiagnosisCategory();

    void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory);
}
