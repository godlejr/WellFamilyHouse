package com.demand.well_family.well_family.story.detail.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.story.detail.interactor.StoryDetailInteractor;
import com.demand.well_family.well_family.story.detail.interactor.impl.StoryDetailInteractorImpl;
import com.demand.well_family.well_family.story.detail.presenter.StoryDetailPresenter;
import com.demand.well_family.well_family.story.detail.view.StoryDetailView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public class StoryDetailPresenterImpl implements StoryDetailPresenter {
    private StoryDetailView storyDetailView;
    private StoryDetailInteractor storyDetailInteractor;
    private PreferenceUtil preferenceUtil;

    public StoryDetailPresenterImpl(Context context) {
        this.storyDetailView = (StoryDetailView) context;
        this.storyDetailInteractor = new StoryDetailInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(StoryInfo storyInfo, boolean likeChecked) {
        User user = preferenceUtil.getUserInfo();
        storyDetailInteractor.setUser(user);
        storyDetailInteractor.setStoryInfo(storyInfo);
        storyDetailInteractor.setLikeChecked(likeChecked);
        storyDetailView.init(storyInfo);

        View decorView = storyDetailView.getDecorView();
        storyDetailView.setToolbar(decorView);
        storyDetailView.showToolbarTitle("가족 설정");
    }

    @Override
    public void onLoadStoryData() {
        boolean likeChecked = storyDetailInteractor.getLikeChecked();

        storyDetailInteractor.setStoryHit();
        storyDetailInteractor.getStoryLikeCount();
        storyDetailInteractor.getStoryCommentCount();

        if (likeChecked) {
            storyDetailView.showLikeChecked(true);
        } else {
            storyDetailView.showLikeChecked(false);
        }
        storyDetailInteractor.setFirstLikeChecked(true);

        storyDetailInteractor.getPhotoData();
        storyDetailInteractor.getCommentData();
    }

    @Override
    public void onCheckedChangeForLike(boolean isChecked) {
        boolean firstLikeChecked = storyDetailInteractor.isFirstLikeChecked();

        if (firstLikeChecked) {
            if (isChecked) {
                storyDetailInteractor.setLikeCheck();
            } else {
                storyDetailInteractor.setLikeUncheck();
            }
        }
    }

    @Override
    public void onClickPhoto(int position) {
        storyDetailView.navigateToPhotoPopupActivity(position);
    }

    @Override
    public void onClickComment(CommentInfo commentInfo) {
        User user = storyDetailInteractor.getUser();
        int commentUserId = commentInfo.getUser_id();

        if (user.getId() == commentUserId) {
            storyDetailView.navigateToStoryCommentDialogActivityForOwner(commentInfo);
        } else {
            storyDetailView.navigateToStoryDialogActivityForMember(commentInfo);
        }

    }

    @Override
    public void onClickCommentAdd(String content) {
        if (content.length() != 0) {
            storyDetailInteractor.setCommentAdded(content);
        } else {
            return;
        }
    }

    @Override
    public void onClickToolbarBack() {
        StoryInfo storyInfo = storyDetailInteractor.getStoryInfo();
        boolean likeChecked = storyDetailInteractor.getLikeChecked();
        storyDetailView.navigateToBack(storyInfo, likeChecked);
    }

    @Override
    public void onClickStoryMenu() {
        StoryInfo storyInfo = storyDetailInteractor.getStoryInfo();
        storyDetailView.navigateToStoryDialogActivity(storyInfo);
    }

    @Override
    public void onSuccessSetLikeCheck() {
        storyDetailView.showLikeCheck(1);
        storyDetailInteractor.setLikeChecked(!storyDetailInteractor.getLikeChecked());
    }

    @Override
    public void onSuccessSetLikeUncheck() {
        storyDetailView.showLikeCheck(-1);
        storyDetailInteractor.setLikeChecked(!storyDetailInteractor.getLikeChecked());
    }

    @Override
    public void onSuccessSetCommentAdded(Comment comment) {
        User user = storyDetailInteractor.getUser();
        CommentInfo commentInfo = new CommentInfo(comment.getId(), user.getId(), user.getName(), user.getAvatar(), comment.getContent(), comment.getCreated_at());

        storyDetailView.setCommentAdapterAdded(commentInfo);
        storyDetailView.showCommentAdapterNotifyItemInserted();

        //recounting
        storyDetailInteractor.getStoryCommentCount();

        //reset
        storyDetailView.showCommentEdit("");

        //pointing
        storyDetailView.showCommentScrollDown();

    }

    @Override
    public void onSuccessGetCommentData(ArrayList<CommentInfo> commentInfoList) {
        storyDetailView.setCommentItem(commentInfoList);
        storyDetailView.setCommentItemSpace();
    }

    @Override
    public void onSuccessGetPhotoData(ArrayList<Photo> photoList) {
        int photoSize = photoList.size();

        if (photoSize == 0) {
            storyDetailView.gonePhotoItem();
        } else {
            storyDetailView.setPhotoItem(photoList);
        }
    }

    @Override
    public void onSuccessSetStoryCommentCount(int count) {
        storyDetailView.showCommentCount(String.valueOf(count));
    }

    @Override
    public void onSuccessSetStoryLikeCount(int count) {
        storyDetailView.showLikeCount(String.valueOf(count));
    }

    @Override
    public void onBackPressed() {
        StoryInfo storyInfo = storyDetailInteractor.getStoryInfo();
        boolean likeChecked = storyDetailInteractor.getLikeChecked();
        storyDetailView.navigateToBack(storyInfo, likeChecked);
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            storyDetailView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            storyDetailView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onActivityResultForPopupResultOk(String content) {
        StoryInfo storyInfo = storyDetailInteractor.getStoryInfo();
        boolean likeChecked = storyDetailInteractor.getLikeChecked();

        storyInfo.setContent(content);
        storyDetailView.navigateToBack(storyInfo, likeChecked);
    }

    @Override
    public void onActivityResultForPopupDelete() {
        StoryInfo storyInfo = storyDetailInteractor.getStoryInfo();
        storyDetailView.navigateToBackForPopupDelete(storyInfo);
    }

    @Override
    public void onActivityResultForCommentEditResultOkModifyOrReport(int position, String content) {
        storyDetailView.setCommentAdapterSetContent(position, content);
        storyDetailView.showCommentAdapterNotifyItemChanged(position);
    }

    @Override
    public void onActivityResultForCommentEditResultOkDELETE(int position) {
        storyDetailView.setCommentAdapterDelete(position);
        storyDetailView.showCommentAdapterNotifyItemDelete(position);
    }
}
