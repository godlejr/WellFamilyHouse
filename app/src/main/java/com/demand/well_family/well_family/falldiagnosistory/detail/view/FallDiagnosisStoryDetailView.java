package com.demand.well_family.well_family.falldiagnosistory.detail.view;

import android.view.View;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.EnvironmentPhoto;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.PhysicalEvaluationScore;
import com.demand.well_family.well_family.falldiagnosistory.base.adapter.FallDiagnosisStoryAdapter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-07.
 */

public interface FallDiagnosisStoryDetailView {

    void init(FallDiagnosisStory fallDiagnosisStory, FallDiagnosisStoryInfo fallDiagnosisStoryInfo);

    View getDecorView();

    void setToolbar(View decorView);

    void showToolbarTitle(String message);

    void showMessage(String message);


    void showScore(String score);

    void showScoreTextChangeColorWithSafe();

    void showScoreTextChangeColorWithCaution();

    void showScoreTextChangeColorWithRisk();

    void showCommentCount(String count);

    void showLikeCount(String count);

    void setFallDiagnosisStoryLikeChecked();

    void setFallDiagnosisStoryLikeUnChecked();

    void setFallDiagnosisStoryLikeIsChecked();

    void setFallDiagnosisStoryLikeIsUnChecked();

    void navigateToBack(FallDiagnosisStory fallDiagnosisStory);

    void setContentForSelfDiagnosis(ArrayList<FallDiagnosisContentCategory> categoryList);

    void setContentForPhysicalEvaluation(PhysicalEvaluationScore physicalEvaluationScore);


    void setContentForRiskEvaluationPhoto(ArrayList<EnvironmentPhoto> environmentPhotoList);

    void setContentForRiskEvaluationCategory(ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList);

    void navigateToPhotoPopupActivity(ArrayList<Photo> photoList);


    void navigateToCommentDialogActivityForOwner(CommentInfo commentInfo);

    void navigateToCommentDialogActivityForMember(CommentInfo commentInfo);

    void setFallDiagnosisStoryCommentAdapterList(ArrayList<CommentInfo> commentInfoList);

    void showCommentAdapterNotifyItemInserted();

    void showCommentEdit(String message);

    void showCommentScrollDown();

    void setCommentAdapterAdded(CommentInfo commentInfo);


    void showCommentAdapterNotifyItemDelete(int position);

    void setCommentAdapterDelete(int position);

    void setCommentAdapterSetContent(int position, String content);

    void showCommentAdapterNotifyItemChanged(int position);

    void goneMenuButton();
}
