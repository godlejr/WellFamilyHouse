package com.demand.well_family.well_family.family.base.presenter;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.base.adapter.content.ContentAdapter;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-18.
 */

public interface FamilyPresenter {

    void onCreate(Family family, boolean notificationFlag);

    String validateUserIdentification(User userOfList);

    void setToolbarAndMenu(String familyName);

    void setUserBirth(String birth);

    void onLoadContent(ContentAdapter.ContentViewHolder holder, StoryInfo storyInfo);

    void onSuccessSetThreadContentAdd(StoryInfo storyInfo);

    void onSuccessGetContentUser(User user);

    void onSuccessSetContentLikeCheck(ContentAdapter.ContentViewHolder holder, int position);

    void onSuccessSetContentLikeUncheck(ContentAdapter.ContentViewHolder holder, int position);

    void onSuccessGetContentLikeCheck(ContentAdapter.ContentViewHolder holder, int check, int position);

    void onSuccessGetContentCommentCount(ContentAdapter.ContentViewHolder holder, int count);

    void onSuccessGetContentLikeCount(ContentAdapter.ContentViewHolder holder, int count);

    void onSuccessGetContentPhotoData(ContentAdapter.ContentViewHolder holder, ArrayList<Photo> photoList, StoryInfo storyInfo);

    void onSuccessGetContentData();

    void onSuccessGetUserData(ArrayList<User> tempUserList);

    void onSuccessFamilyDeleteCheck(Family family);

    void onSuccessGetContentDataAdded(int position);

    void onSuccessThreadRun();

    void onGettingContentDataAdded();

    void onScrollChange(int difference);

    void onCheckedChangeForLike(ContentAdapter.ContentViewHolder holder, StoryInfo storyInfo, boolean isFirstChecked, boolean isChecked);

    void validateFamilyCreatorForEdit(int userId, int familyUserId);

    void onClickContentBody(StoryInfo storyInfo);

    void onClickContentPhoto(StoryInfo storyInfo);

    void onClickContentUser(int contentUserId);

    void onClickBack();

    void onClickUser();

    void onClickUser(User user);

    void onClickLogout();

    void onClickSongMain();

    void onClickAppGames(String appPackageName);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onActivityResultForWriteResultOk(StoryInfo storyInfo);

    void onActivityResultForStoryDetailResultOk(int position, String content, Boolean likeCheck);

    void onActivityResultForStoryDetailDelete(int position);

    void onActivityResultForEditFamilyResultOk(String familyName, String familyContent, String familyAvatar);

}
