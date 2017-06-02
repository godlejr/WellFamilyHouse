package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.interactor;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.PhysicalEvaluation;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluationScore;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public interface PhysicalEvaluationResultInteractor {

    FallDiagnosisCategory getFallDiagnosisCategory();

    void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory);

    ArrayList<PhysicalEvaluationCategory> getPhysicalEvaluationCategoryList();

    void setPhysicalEvaluationCategoryList(ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList);

    ArrayList<PhysicalEvaluation> getPhysicalEvaluationList();

    void setPhysicalEvaluationList(ArrayList<PhysicalEvaluation> physicalEvaluationList);


    User getUser();

    void setUser(User user);

    PhysicalEvaluationScore getPhysicalEvaluationScore();

    void setPhysicalEvaluationScore(PhysicalEvaluationScore physicalEvaluationScore);

    void setFallDiagnosisStory(FallDiagnosisStory fallDiagnosisStory);

    void setStoryAdded();

    void setPhysicalEvaluationAdded(int storyId, PhysicalEvaluation physicalEvaluation);

    void setPhysicalEvaluationScoreAdded(int storyId);

    int getFallDiagnosisRiskCategoryId();

    void setFallDiagnosisRiskCategoryId(int fallDiagnosisRiskCategoryId);
}
