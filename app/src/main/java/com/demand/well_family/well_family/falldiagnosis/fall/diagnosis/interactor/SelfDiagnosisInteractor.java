package com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.interactor;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public interface SelfDiagnosisInteractor {
    void getDiagnosisCategories(User user);

    FallDiagnosisCategory getFallDiagnosisCategory();

    void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory);

    User getUser();

    void setUser(User user);

    ArrayList<Boolean> getAnswerList();

    void setAnswerList(ArrayList<Boolean> answerList);

    void setAnswerAdded(boolean check);

}
