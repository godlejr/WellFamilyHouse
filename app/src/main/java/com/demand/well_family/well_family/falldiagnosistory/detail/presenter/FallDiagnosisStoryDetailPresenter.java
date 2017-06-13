package com.demand.well_family.well_family.falldiagnosistory.detail.presenter;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.EnvironmentPhoto;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryComment;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.PhysicalEvaluationScore;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosistory.detail.activity.FallDiagnosisStoryDetailActivity;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-07.
 */

public interface FallDiagnosisStoryDetailPresenter {
    void onClickPhoto(ArrayList<EnvironmentPhoto> environmentPhotoList);

    void onClickComment(CommentInfo commentInfo);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onCreate(FallDiagnosisStory fallDiagnosisStory, FallDiagnosisStoryInfo fallDiagnosisStoryInfo);


    void onLoadData();

    void setScore();

    void onSuccessGetFallDiagnosisStoryCommentCount(int count);

    void onSuccessGetFallDiagnosisStoryLikeCount(int count);

    void onCheckedChangeForLike(boolean isChecked);

    void onSuccessSetContentLikeDown();

    void onSuccessSetContentLikeUp();

    void onBackPressed();

    void onSuccessGetPhysicalEvaluationScore(PhysicalEvaluationScore physicalEvaluationScore);

    void onSuccessGetSelfDiagnosisList(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList);

    void onSuccessGetEnvironmentPhoto(ArrayList<EnvironmentPhoto> environmentPhotoList);

    void onSuccessGetEnvironmentEvaluationList(ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList);

    void onSuccessGetFallDiagnosisStoryCommentList(ArrayList<CommentInfo> commentInfoList);

    void onSuccessSetFallDiagnosisStoryComment(FallDiagnosisStoryComment fallDiagnosisStoryComment);

    void setFalldiagnosisStoryComment(CommentInfo commentInfo);

    void onActivityResultForCommentEditResultOkDELETE(int position);

    void onActivityResultForCommentEditResultOkModifyOrReport(int position, String content);
}
