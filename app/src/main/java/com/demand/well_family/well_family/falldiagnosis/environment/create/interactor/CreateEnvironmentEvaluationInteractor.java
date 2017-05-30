package com.demand.well_family.well_family.falldiagnosis.environment.create.interactor;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-30.
 */

public interface CreateEnvironmentEvaluationInteractor {
    void getEnvironmentEvaluationCategory(User user);

    FallDiagnosisCategory getFallDiagnosisCategory();

    void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory);

    FallDiagnosisContentCategory getFallDiagnosisContentCategory();

    void setFallDiagnosisContentCategory(FallDiagnosisContentCategory fallDiagnosisContentCategory);

    void setAnswerAdded(boolean answer);

    ArrayList<Boolean> getAnswerList();
    void setAnswerList( ArrayList<Boolean> answerList);
}
