package com.demand.well_family.well_family.falldiagnosistory.detail.interactor;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-06-07.
 */

public interface FallDiagnosisStoryDetailInteractor {
    void getFallDiagnosisStoryCommentList();

    void setFallDiagnosisStoryComment(CommentInfo commentInfo);

    FallDiagnosisStory getFallDiagnosisStory();

    void setFallDiagnosisStory(FallDiagnosisStory fallDiagnosisStory);

    FallDiagnosisStoryInfo getFallDiagnosisStoryInfo();

    void setFallDiagnosisStoryInfo(FallDiagnosisStoryInfo fallDiagnosisStoryInfo);

    void getFallDiagnosisStoryCommentCount();

    void getFallDiagnosisStoryLikeCount();

    User getUser();

    void setUser(User user);

    void setContentLikeUp();
    void setContentLikeDown();

    void getPhysicalEvaluationScore();
    void getSelfDiagnosisList();

    void getEnvironmentPhoto();
    void getEnvironmentEvaluationList();


}
