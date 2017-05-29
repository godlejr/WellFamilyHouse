package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.interactor;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluation;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public interface CreatePhysicalEvaluationInteractor {
    void getPhysicalEvaluationCategories(User user);

    FallDiagnosisCategory getFallDiagnosisCategory();

    void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory);

    void getCountDownSecond();

    void getTimer();

    void setTimerPause();

    int getMinute();

    void setMinute(int minute);

    int getSecond();

    void setSecond(int second);

    int getMillisecond();

    void setMillisecond(int millsecond);

    String getTimes(int time);

    ArrayList<PhysicalEvaluationCategory> getPhysicalEvaluationCategoryList();

    void setPhysicalEvaluationCategoryList(ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList);

    ArrayList<PhysicalEvaluation> getPhysicalEvaluationList();

    void setPhysicalEvaluationList(ArrayList<PhysicalEvaluation> physicalEvaluationList);

    void setPhysicalEvaluationAdded(PhysicalEvaluation physicalEvaluation);

}
