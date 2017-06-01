package com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.create.interactor;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
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

    ArrayList<Integer> getAnswerList();

    void setAnswerList(ArrayList<Integer> answerList);

    void setAnswerAdded(int fallContentCategoryId);

    ArrayList<FallDiagnosisContentCategory> getFallDiagnosisContentCategoryList();

    void setFallDiagnosisContentCategoryList(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList);


}
