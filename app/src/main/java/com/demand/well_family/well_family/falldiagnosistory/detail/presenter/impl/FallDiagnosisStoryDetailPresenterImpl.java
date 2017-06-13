package com.demand.well_family.well_family.falldiagnosistory.detail.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.EnvironmentPhoto;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryComment;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.PhysicalEvaluationScore;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosistory.detail.interactor.FallDiagnosisStoryDetailInteractor;
import com.demand.well_family.well_family.falldiagnosistory.detail.interactor.impl.FallDiagnosisStoryDetailInteractorImpl;
import com.demand.well_family.well_family.falldiagnosistory.detail.presenter.FallDiagnosisStoryDetailPresenter;
import com.demand.well_family.well_family.falldiagnosistory.detail.view.FallDiagnosisStoryDetailView;
import com.demand.well_family.well_family.falldiagnosistory.flag.FallDiagnosisStoryCodeFlag;
import com.demand.well_family.well_family.flag.FallDiagnosisFlag;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-07.
 */

public class FallDiagnosisStoryDetailPresenterImpl implements FallDiagnosisStoryDetailPresenter {
    private FallDiagnosisStoryDetailView fallDiagnosisStoryDetailView;
    private FallDiagnosisStoryDetailInteractor fallDiagnosisStoryDetailInteractor;
    private PreferenceUtil preferenceUtil;

    public FallDiagnosisStoryDetailPresenterImpl(Context context) {
        this.fallDiagnosisStoryDetailView = (FallDiagnosisStoryDetailView) context;
        this.fallDiagnosisStoryDetailInteractor = new FallDiagnosisStoryDetailInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(FallDiagnosisStory fallDiagnosisStory, FallDiagnosisStoryInfo fallDiagnosisStoryInfo) {
        User user = preferenceUtil.getUserInfo();
        fallDiagnosisStoryDetailInteractor.setUser(user);
        fallDiagnosisStoryDetailInteractor.setFallDiagnosisStoryInfo(fallDiagnosisStoryInfo);
        fallDiagnosisStoryDetailInteractor.setFallDiagnosisStory(fallDiagnosisStory);

        View decorView = fallDiagnosisStoryDetailView.getDecorView();
        fallDiagnosisStoryDetailView.setToolbar(decorView);
        fallDiagnosisStoryDetailView.showToolbarTitle("낙상 진단");

        fallDiagnosisStoryDetailView.init(fallDiagnosisStory, fallDiagnosisStoryInfo);
    }


    @Override
    public void onLoadData() {
        fallDiagnosisStoryDetailInteractor.getFallDiagnosisStoryCommentCount();
        fallDiagnosisStoryDetailInteractor.getFallDiagnosisStoryLikeCount();

        FallDiagnosisStory fallDiagnosisStory = fallDiagnosisStoryDetailInteractor.getFallDiagnosisStory();
        boolean isLikeChecked = fallDiagnosisStory.getChecked();

        if (isLikeChecked) {
            fallDiagnosisStoryDetailView.setFallDiagnosisStoryLikeIsChecked();
        } else {
            fallDiagnosisStoryDetailView.setFallDiagnosisStoryLikeIsUnChecked();
        }
        fallDiagnosisStory.setFirstChecked(true);

        User user = fallDiagnosisStoryDetailInteractor.getUser();
        int userId = user.getId();
        int storyId = fallDiagnosisStory.getUser_id();
        if (storyId != user.getId()) {
            fallDiagnosisStoryDetailView.goneMenuButton();
        }

        fallDiagnosisStoryDetailInteractor.getFallDiagnosisStoryCommentCount();
        fallDiagnosisStoryDetailInteractor.getFallDiagnosisStoryLikeCount();
        fallDiagnosisStoryDetailInteractor.getEnvironmentPhoto();
        fallDiagnosisStoryDetailInteractor.getEnvironmentEvaluationList();
        fallDiagnosisStoryDetailInteractor.getFallDiagnosisStoryCommentList();

        int categoryId = fallDiagnosisStory.getFall_diagnosis_category_id();

        if (categoryId == FallDiagnosisFlag.SELF_DIAGNOSIS) {
            fallDiagnosisStoryDetailInteractor.getSelfDiagnosisList();
        }
        if (categoryId == FallDiagnosisFlag.PHYSICAL_EVALUATION) {
            fallDiagnosisStoryDetailInteractor.getPhysicalEvaluationScore();
        }
        if (categoryId == FallDiagnosisFlag.RISK_EVALUATION) {
            fallDiagnosisStoryDetailInteractor.getEnvironmentPhoto();
            fallDiagnosisStoryDetailInteractor.getEnvironmentEvaluationList();
        }

        setScore();
    }

    @Override
    public void setScore() {
        FallDiagnosisStoryInfo fallDiagnosisStoryInfo = fallDiagnosisStoryDetailInteractor.getFallDiagnosisStoryInfo();
        FallDiagnosisStory fallDiagnosisStory = fallDiagnosisStoryDetailInteractor.getFallDiagnosisStory();

        int score = fallDiagnosisStoryInfo.getScore();
        int totalCount = fallDiagnosisStoryInfo.getTotal_count();
        int categoryId = fallDiagnosisStory.getFall_diagnosis_category_id();
        int riskCategoryId = fallDiagnosisStory.getFall_diagnosis_risk_category_id();

        if (categoryId == FallDiagnosisFlag.PHYSICAL_EVALUATION) {
            fallDiagnosisStoryDetailView.showScore(score + "점");
        }

        if (categoryId == FallDiagnosisFlag.SELF_DIAGNOSIS || categoryId == FallDiagnosisFlag.RISK_EVALUATION) {
            fallDiagnosisStoryDetailView.showScore(score + "/" + totalCount);
        }

        if (riskCategoryId == FallDiagnosisStoryCodeFlag.SAFE) {
            fallDiagnosisStoryDetailView.showScoreTextChangeColorWithSafe();
        }
        if (riskCategoryId == FallDiagnosisStoryCodeFlag.CAUTION) {
            fallDiagnosisStoryDetailView.showScoreTextChangeColorWithCaution();
        }
        if (riskCategoryId == FallDiagnosisStoryCodeFlag.RISK) {
            fallDiagnosisStoryDetailView.showScoreTextChangeColorWithRisk();
        }
    }

    @Override
    public void onSuccessGetFallDiagnosisStoryCommentCount(int count) {
        fallDiagnosisStoryDetailView.showCommentCount(String.valueOf(count));
    }

    @Override
    public void onSuccessGetFallDiagnosisStoryLikeCount(int count) {
        fallDiagnosisStoryDetailView.showLikeCount(String.valueOf(count));
    }

    @Override
    public void onCheckedChangeForLike(boolean isChecked) {
        boolean isFirstChecked = fallDiagnosisStoryDetailInteractor.getFallDiagnosisStory().getFirstChecked();

        if (isFirstChecked) {
            if (isChecked) {
                fallDiagnosisStoryDetailInteractor.setContentLikeUp();
            } else {
                fallDiagnosisStoryDetailInteractor.setContentLikeDown();
            }
        }

    }

    @Override
    public void onSuccessSetContentLikeDown() {
        fallDiagnosisStoryDetailInteractor.getFallDiagnosisStory().setChecked(false);
        fallDiagnosisStoryDetailView.setFallDiagnosisStoryLikeUnChecked();
    }

    @Override
    public void onSuccessSetContentLikeUp() {
        fallDiagnosisStoryDetailInteractor.getFallDiagnosisStory().setChecked(true);
        fallDiagnosisStoryDetailView.setFallDiagnosisStoryLikeChecked();
    }

    @Override
    public void onBackPressed() {
        FallDiagnosisStory fallDiagnosisStory = fallDiagnosisStoryDetailInteractor.getFallDiagnosisStory();
        fallDiagnosisStory.setFirstChecked(false);
        fallDiagnosisStoryDetailView.navigateToBack(fallDiagnosisStory);
    }

    @Override
    public void onSuccessGetPhysicalEvaluationScore(PhysicalEvaluationScore physicalEvaluationScore) {
        fallDiagnosisStoryDetailView.setContentForPhysicalEvaluation(physicalEvaluationScore);
    }

    @Override
    public void onSuccessGetSelfDiagnosisList(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList) {
        fallDiagnosisStoryDetailView.setContentForSelfDiagnosis(fallDiagnosisContentCategoryList);
    }

    @Override
    public void onSuccessGetEnvironmentPhoto(ArrayList<EnvironmentPhoto> environmentPhotoList) {
        fallDiagnosisStoryDetailView.setContentForRiskEvaluationPhoto(environmentPhotoList);
    }

    @Override
    public void onSuccessGetEnvironmentEvaluationList(ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList) {
        fallDiagnosisStoryDetailView.setContentForRiskEvaluationCategory(environmentEvaluationCategoryList);
    }

    @Override
    public void onSuccessGetFallDiagnosisStoryCommentList(ArrayList<CommentInfo> commentInfoList) {
        fallDiagnosisStoryDetailView.setFallDiagnosisStoryCommentAdapterList(commentInfoList);
    }

    @Override
    public void onSuccessSetFallDiagnosisStoryComment(FallDiagnosisStoryComment fallDiagnosisStoryComment) {
        User user = fallDiagnosisStoryDetailInteractor.getUser();
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setCreated_at(fallDiagnosisStoryComment.getCreated_at());
        commentInfo.setAvatar(user.getAvatar());
        commentInfo.setUser_id(user.getId());
        commentInfo.setContent(fallDiagnosisStoryComment.getContent());
        commentInfo.setComment_id(fallDiagnosisStoryComment.getId());
        commentInfo.setUser_name(user.getName());

        fallDiagnosisStoryDetailView.setCommentAdapterAdded(commentInfo);
        fallDiagnosisStoryDetailInteractor.getFallDiagnosisStoryCommentCount();
        fallDiagnosisStoryDetailView.showCommentAdapterNotifyItemInserted();
        fallDiagnosisStoryDetailView.showCommentEdit("");
        fallDiagnosisStoryDetailView.showCommentScrollDown();
    }

    @Override
    public void setFalldiagnosisStoryComment(CommentInfo commentInfo) {
        User user = fallDiagnosisStoryDetailInteractor.getUser();
        commentInfo.setUser_id(user.getId());

        fallDiagnosisStoryDetailInteractor.setFallDiagnosisStoryComment(commentInfo);
    }

    @Override
    public void onActivityResultForCommentEditResultOkDELETE(int position) {
        fallDiagnosisStoryDetailInteractor.getFallDiagnosisStoryCommentCount();
        fallDiagnosisStoryDetailView.setCommentAdapterDelete(position);
        fallDiagnosisStoryDetailView.showCommentAdapterNotifyItemDelete(position);
    }

    @Override
    public void onActivityResultForCommentEditResultOkModifyOrReport(int position, String content) {
        fallDiagnosisStoryDetailView.setCommentAdapterSetContent(position, content);
        fallDiagnosisStoryDetailView.showCommentAdapterNotifyItemChanged(position);
    }

    @Override
    public void onClickPhoto(ArrayList<EnvironmentPhoto> environmentPhotoList) {
        ArrayList<Photo> photoList = new ArrayList<>();
        int size = environmentPhotoList.size();

        for (int i = 0; i < size; i++) {
            EnvironmentPhoto environmentPhoto = environmentPhotoList.get(i);

            Photo photo = new Photo();
            photo.setName(environmentPhoto.getName());
            photo.setExt(environmentPhoto.getExt());
            photo.setStory_id(environmentPhoto.getFall_diagnosis_story_id());
            photoList.add(photo);
        }

        fallDiagnosisStoryDetailView.navigateToPhotoPopupActivity(photoList);
    }

    @Override
    public void onClickComment(CommentInfo commentInfo) {
        User user = fallDiagnosisStoryDetailInteractor.getUser();
        int userId = user.getId();
        int commentUserId = commentInfo.getUser_id();

        if (userId != commentUserId) {
            fallDiagnosisStoryDetailView.navigateToCommentDialogActivityForMember(commentInfo);
        } else {
            fallDiagnosisStoryDetailView.navigateToCommentDialogActivityForOwner(commentInfo);
        }

    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            fallDiagnosisStoryDetailView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            fallDiagnosisStoryDetailView.showMessage(apiErrorUtil.message());
        }
    }

}
