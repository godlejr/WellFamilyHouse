package com.demand.well_family.well_family.exercisestory.detail.view;

import android.view.View;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.ExerciseStory;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-27.
 */

public interface ExerciseStoryDetailView {
    void showMessage(String message);

    View getDecorView();

    void showToolbarTitle(String message);

    void setToolbar(View decorView);

    void init();

    void showTitle(String message);

    void showDate(String message);

    void showScore(float score);

    void showScoreText(String message);

    void showResult(String message);

    void showCommentCount(String message);

    void showLikeCount(String message);


    void navigateToExerciseStoryDialogActivity(ExerciseStory exerciseStory);

    void showCommentAdapterNotifyItemInserted();

    void setCommentAdapterAdded(CommentInfo commentInfo);

    void showCommentAdapterNotifyItemDelete(int position);

    void setCommentAdapterDelete(int position);

    void setCommentAdapterSetContent(int position, String content);

    void showCommentAdapterNotifyItemChanged(int position);

    void navigateToCommentDialogActivityForMember(CommentInfo commentInfo);

    void navigateToCommentDialogActivityForOwner(CommentInfo commentInfo);

    void setExerciseStoryCommentAdapterItem(ArrayList<CommentInfo> commentInfoList);

    void showCommentEdit(String message);

    void showCommentScrollDown();

    void goneMenuButton();

    void setExerciseStoryLikeChecked();

    void setExerciseStoryLikeUnChecked();

    void setExerciseStoryLikeIsChecked();

    void setExerciseStoryLikeIsUnChecked();

    void navigateToBack(ExerciseStory exerciseStory);
}
