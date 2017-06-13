package com.demand.well_family.well_family.notification.falldiagnosis.presenter;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.EnvironmentPhoto;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryComment;
import com.demand.well_family.well_family.dto.PhysicalEvaluationScore;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-13.
 */

public interface NotificationFallDiagnosisStoryPresenter {
    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onCreate(FallDiagnosisStory fallDiagnosisStory);

    void onLoadData();

    void onSuccessSetContentLikeUp();

    void onSuccessSetContentLikeDown();

    void onSuccessGetPhysicalEvaluationScore(PhysicalEvaluationScore physicalEvaluationScore);

    void onSuccessGetSelfDiagnosisList(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList);

    void onSuccessGetEnvironmentPhoto(ArrayList<EnvironmentPhoto> environmentPhotoList);

    void onSuccessGetEnvironmentEvaluationList(ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList);

    void onSuccessGetFallDiagnosisStoryCommentList(ArrayList<CommentInfo> commentInfoList);

    void onSuccessSetFallDiagnosisStoryComment(FallDiagnosisStoryComment fallDiagnosisStoryComment);

    void onSuccessGetFallDiagnosisStoryCommentCount(int commentCount);

    void onSuccessGetFallDiagnosisStoryLikeCount(int likeCount);
}
