package com.demand.well_family.well_family.story.detail.presenter;

import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public interface StoryDetailPresenter {
    void onCreate(StoryInfo storyInfo, boolean likeChecked);

    void onLoadStoryData();

    void onCheckedChangeForLike(boolean isChecked);

    void onClickPhoto(int position);

    void onClickComment(CommentInfo commentInfo);

    void onClickCommentAdd(String content);

    void onClickToolbarBack();

    void onClickStoryMenu();

    void onSuccessSetLikeCheck();

    void onSuccessSetLikeUncheck();

    void onSuccessSetCommentAdded(Comment comment);

    void onSuccessGetCommentData(ArrayList<CommentInfo> commentInfoList);

    void onSuccessGetPhotoData(ArrayList<Photo> photoList);

    void onSuccessSetStoryCommentCount(int count);

    void onSuccessSetStoryLikeCount(int count);

    void onBackPressed();

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onActivityResultForPopupResultOk(String content);

    void onActivityResultForPopupDelete();

    void onActivityResultForCommentEditResultOkModifyOrReport(int position, String content);

    void onActivityResultForCommentEditResultOkDELETE(int position);

}
