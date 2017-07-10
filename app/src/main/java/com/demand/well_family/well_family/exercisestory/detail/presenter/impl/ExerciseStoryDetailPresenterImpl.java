package com.demand.well_family.well_family.exercisestory.detail.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.ExerciseStoryComment;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.exercise.flag.ExerciseCodeFlag;
import com.demand.well_family.well_family.exercisestory.detail.interactor.ExerciseStoryDetailInteractor;
import com.demand.well_family.well_family.exercisestory.detail.interactor.impl.ExerciseStoryDetailInteractorImpl;
import com.demand.well_family.well_family.exercisestory.detail.presenter.ExerciseStoryDetailPresenter;
import com.demand.well_family.well_family.exercisestory.detail.view.ExerciseStoryDetailView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.CalculateDateUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-27.
 */

public class ExerciseStoryDetailPresenterImpl implements ExerciseStoryDetailPresenter {
    private ExerciseStoryDetailInteractor exerciseStoryDetailInteractor;
    private ExerciseStoryDetailView exerciseStoryDetailView;
    private PreferenceUtil preferenceUtil;

    public ExerciseStoryDetailPresenterImpl(Context context) {
        this.exerciseStoryDetailInteractor = new ExerciseStoryDetailInteractorImpl(this);
        this.exerciseStoryDetailView = (ExerciseStoryDetailView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(ExerciseStory exerciseStory) {
        User user = preferenceUtil.getUserInfo();
        exerciseStoryDetailInteractor.setUser(user);
        exerciseStoryDetailInteractor.setExerciseStory(exerciseStory);

        View decorView = exerciseStoryDetailView.getDecorView();
        exerciseStoryDetailView.setToolbar(decorView);
        exerciseStoryDetailView.showToolbarTitle("낙상예방");

        exerciseStoryDetailView.init();
    }

    @Override
    public void onLoadData() {
        exerciseStoryDetailInteractor.getCommentCount();
        exerciseStoryDetailInteractor.getLikeCount();
        exerciseStoryDetailInteractor.getExerciseStoryCommentList();

        ExerciseStory exerciseStory = exerciseStoryDetailInteractor.getExerciseStory();
        User user = exerciseStoryDetailInteractor.getUser();

        String title = exerciseStory.getTitle();
        String date = exerciseStory.getCreated_at();
        int score = exerciseStory.getScore();
        String scoreMessage = score + "점";
        Boolean isLikeChecked = exerciseStory.getChecked();

        exerciseStoryDetailView.showTitle(title);
        exerciseStoryDetailView.showDate(CalculateDateUtil.calculateDate(date));
        exerciseStoryDetailView.showScore(score);
        exerciseStoryDetailView.showScoreText(scoreMessage);

        if (score == ExerciseCodeFlag.SCORE_ONE) {
            exerciseStoryDetailView.showResult(ExerciseCodeFlag.SCORE_ONE_TITLE);
        }
        if (score == ExerciseCodeFlag.SCORE_TWO) {
            exerciseStoryDetailView.showResult(ExerciseCodeFlag.SCORE_TWO_TITLE);
        }
        if (score == ExerciseCodeFlag.SCORE_THREE) {
            exerciseStoryDetailView.showResult(ExerciseCodeFlag.SCORE_THREE_TITLE);
        }
        if (score == ExerciseCodeFlag.SCORE_FOUR) {
            exerciseStoryDetailView.showResult(ExerciseCodeFlag.SCORE_FOUR_TITLE);
        }
        if (score == ExerciseCodeFlag.SCORE_FIVE) {
            exerciseStoryDetailView.showResult(ExerciseCodeFlag.SCORE_FIVE_TITLE);
        }





        if (isLikeChecked != null) {
            if (isLikeChecked) {
                exerciseStoryDetailView.setExerciseStoryLikeIsChecked();
            } else {
                exerciseStoryDetailView.setExerciseStoryLikeIsUnChecked();
            }

            exerciseStory.setFirstChecked(true);
        } else {
            exerciseStoryDetailInteractor.getContentLikeCheck();
        }


        int userId = user.getId();
        int storyId = exerciseStory.getUser_id();
        if (storyId != userId) {
            exerciseStoryDetailView.goneMenuButton();
        }

    }

    @Override
    public void onBackPressed() {
        ExerciseStory exerciseStory = exerciseStoryDetailInteractor.getExerciseStory();
        exerciseStory.setFirstChecked(false);
        exerciseStoryDetailView.navigateToBack(exerciseStory);
    }


    @Override
    public void onSuccessGetCommentCount(int count) {
        exerciseStoryDetailView.showCommentCount(String.valueOf(count));
    }

    @Override
    public void onSuccessGetLikeCount(int count) {
        exerciseStoryDetailView.showLikeCount(String.valueOf(count));
    }

    @Override
    public void onClickMenuButton() {
        ExerciseStory exerciseStory = exerciseStoryDetailInteractor.getExerciseStory();
        exerciseStoryDetailView.navigateToExerciseStoryDialogActivity(exerciseStory);
    }

    @Override
    public void setExerciseStoryComment(CommentInfo commentInfo) {
        int commentLength = commentInfo.getContent().length();
        if (commentLength > 0) {
            User user = exerciseStoryDetailInteractor.getUser();
            commentInfo.setUser_id(user.getId());

            exerciseStoryDetailInteractor.setExerciseStoryComment(commentInfo);
        } else {
            exerciseStoryDetailView.showMessage("댓글을 입력해주세요");
        }
    }

    @Override
    public void onSuccessSetExerciseStoryComment(ExerciseStoryComment exerciseStoryComment) {
        ExerciseStory exerciseStory = exerciseStoryDetailInteractor.getExerciseStory();
        int exerciseStoryId = exerciseStory.getId();

        User user = exerciseStoryDetailInteractor.getUser();
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setCreated_at(exerciseStoryComment.getCreated_at());
        commentInfo.setAvatar(user.getAvatar());
        commentInfo.setUser_id(user.getId());
        commentInfo.setContent(exerciseStoryComment.getContent());
        commentInfo.setComment_id(exerciseStoryComment.getId());
        commentInfo.setUser_name(user.getName());

        exerciseStoryDetailView.setCommentAdapterAdded(commentInfo);
        exerciseStoryDetailInteractor.getCommentCount();
        exerciseStoryDetailView.showCommentAdapterNotifyItemInserted();
        exerciseStoryDetailView.showCommentEdit("");
        exerciseStoryDetailView.showCommentScrollDown();
    }

    @Override
    public void onSuccessGetExerciseStoryCommentList(ArrayList<CommentInfo> commentInfoList) {
        exerciseStoryDetailView.setExerciseStoryCommentAdapterItem(commentInfoList);
    }

    @Override
    public void onClickComment(CommentInfo commentInfo) {
        User user = exerciseStoryDetailInteractor.getUser();
        int userId = user.getId();
        int commentUserId = commentInfo.getUser_id();

        if (userId != commentUserId) {
            exerciseStoryDetailView.navigateToCommentDialogActivityForMember(commentInfo);
        } else {
            exerciseStoryDetailView.navigateToCommentDialogActivityForOwner(commentInfo);
        }

    }

    @Override
    public void onActivityResultForCommentEditResultOkModifyOrReport(int position, String content) {
        exerciseStoryDetailView.setCommentAdapterSetContent(position, content);
        exerciseStoryDetailView.showCommentAdapterNotifyItemChanged(position);
    }

    @Override
    public void onActivityResultForCommentEditResultOkDELETE(int position) {
        ExerciseStory exerciseStory = exerciseStoryDetailInteractor.getExerciseStory();
        int exerciseStoryId = exerciseStory.getId();

        exerciseStoryDetailInteractor.getCommentCount();
        exerciseStoryDetailView.setCommentAdapterDelete(position);
        exerciseStoryDetailView.showCommentAdapterNotifyItemDelete(position);
    }

    @Override
    public void onSuccessGetContentLikeCheck(int isChecked, int position) {
        ExerciseStory exerciseStory = exerciseStoryDetailInteractor.getExerciseStory();
        if (isChecked > 0) {
            exerciseStoryDetailView.setExerciseStoryLikeIsChecked();
        } else {
            exerciseStoryDetailView.setExerciseStoryLikeIsUnChecked();
        }
        exerciseStory.setFirstChecked(true);

    }

    @Override
    public void onCheckedChangeForLike(boolean isChecked) {
        Boolean isFirstChecked = exerciseStoryDetailInteractor.getExerciseStory().getFirstChecked();

        if (isFirstChecked) {
            if (isChecked) {
                exerciseStoryDetailInteractor.setContentLikeUp();
            } else {
                exerciseStoryDetailInteractor.setContentLikeDown();
            }
        }

    }

    @Override
    public void onSuccessSetContentLikeDown(int position) {
        exerciseStoryDetailInteractor.getExerciseStory().setChecked(false);
        exerciseStoryDetailView.setExerciseStoryLikeUnChecked();
    }

    @Override
    public void onSuccessSetContentLikeUp(int position) {
        exerciseStoryDetailInteractor.getExerciseStory().setChecked(true);
        exerciseStoryDetailView.setExerciseStoryLikeChecked();
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            exerciseStoryDetailView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            exerciseStoryDetailView.showMessage(apiErrorUtil.message());
        }
    }


}
