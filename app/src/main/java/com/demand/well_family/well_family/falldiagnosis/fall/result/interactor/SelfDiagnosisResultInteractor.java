package com.demand.well_family.well_family.falldiagnosis.fall.result.interactor;

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

    ArrayList<Boolean> getAnswerList();

    void setAnswerList(ArrayList<Boolean> answerList);

    ArrayList<Integer> getTrueList();

    void setTrueList(ArrayList<Integer> trueList);

    FallDiagnosisCategory getFallDiagnosisCategory();

    void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory);

    FallDiagnosisStory getFallDiagnosisStory();

    void setFallDiagnosisStory(FallDiagnosisStory fallDiagnosisStory);

    void setStoryAdded();

    void setSelfDiagnosisAdded(int storyId, int fallDiagnosisContentCategoryId);
}
