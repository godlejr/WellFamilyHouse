package com.demand.well_family.well_family.exercisestory.detail.presenter;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.ExerciseStoryComment;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-27.
 */

public interface ExerciseStoryDetailPresenter {
    void onCreate(ExerciseStory exerciseStory);
    void onLoadData();

    void onCheckedChangeForLike(boolean isChecked);

    void onSuccessSetContentLikeDown(int position);

    void onSuccessSetContentLikeUp(int position);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onBackPressed();

    void onSuccessGetCommentCount(int count);

    void onSuccessGetLikeCount(int count);

    void onClickMenuButton();

    void setExerciseStoryComment(CommentInfo commentInfo);

    void onSuccessSetExerciseStoryComment(ExerciseStoryComment exerciseStoryComment);

    void onSuccessGetExerciseStoryCommentList(ArrayList<CommentInfo> commentInfoList);

    void onClickComment(CommentInfo commentInfo);

    void onActivityResultForCommentEditResultOkModifyOrReport(int position, String content);

    void onActivityResultForCommentEditResultOkDELETE(int position);


    void onSuccessGetContentLikeCheck(int isChecked, int position);
}
