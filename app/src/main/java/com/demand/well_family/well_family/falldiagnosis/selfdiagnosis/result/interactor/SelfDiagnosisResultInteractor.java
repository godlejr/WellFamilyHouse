package com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.result.interactor;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public interface SelfDiagnosisResultInteractor {

     User getUser();

    void setUser(User user);

    ArrayList<Integer> getAnswerList();

    void setAnswerList(ArrayList<Integer> answerList);


    FallDiagnosisCategory getFallDiagnosisCategory();

    void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory);

    FallDiagnosisStory getFallDiagnosisStory();


    void setFallDiagnosisStory(FallDiagnosisStory fallDiagnosisStory);

    void setStoryAdded();

    void setSelfDiagnosisAdded(int storyId, int fallDiagnosisContentCategoryId, int index);

    int getFallDiagnosisContentCategorySize();

    void setFallDiagnosisContentCategorySize(int fallDiagnosisContentCategorySize);

    int getFallDiagnosisRiskCategoryId();

    void setFallDiagnosisRiskCategoryId(int fallDiagnosisRiskCategoryId);
}
