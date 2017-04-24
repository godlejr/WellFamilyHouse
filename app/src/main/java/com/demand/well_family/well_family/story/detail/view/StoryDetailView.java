package com.demand.well_family.well_family.story.detail.view;

import android.view.View;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public interface StoryDetailView {

    void init(StoryInfo storyInfo);

    void setToolbar(View view);

    void showToolbarTitle(String message);

    View getDecorView();

    void showCommentCount(String count);

    void showLikeCount(String count);

    void showLikeChecked(boolean isChecked);

    void setCommentItemSpace();

    void setCommentItem(ArrayList<CommentInfo> commentInfoList);

    void setCommentAdapterAdded(CommentInfo commentInfo);

    void showLikeCheck(int value);

    void setCommentAdapterSetContent(int position, String content);

    void setCommentAdapterDelete(int position);

    void showCommentAdapterNotifyItemInserted();

    void showCommentAdapterNotifyItemDelete(int position);

    void showCommentAdapterNotifyItemChanged(int position);

    void showCommentEdit(String message);

    void showCommentScrollDown();

    void gonePhotoItem();

    void setPhotoItem(ArrayList<Photo> photoList);

    void showMessage(String message);

    void navigateToBack(StoryInfo storyInfo, boolean likeChecked);

    void navigateToBackForPopupDelete(StoryInfo storyInfo);

    void navigateToStoryDialogActivity(StoryInfo storyInfo);

    void navigateToStoryCommentDialogActivityForOwner(CommentInfo commentInfo);

    void navigateToStoryDialogActivityForMember(CommentInfo commentInfo);

    void navigateToPhotoPopupActivity(int position);


}
