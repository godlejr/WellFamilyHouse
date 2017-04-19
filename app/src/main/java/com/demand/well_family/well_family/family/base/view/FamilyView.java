package com.demand.well_family.well_family.family.base.view;

import android.view.View;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.base.adapter.content.ContentAdapter;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-18.
 */

public interface FamilyView {
    void init(User user, Family family);

    void setUserItem(ArrayList<User> userList);

    void setContentAdapterInit(ArrayList<StoryInfo> storyList);

    void setContentAdapter(ContentAdapter contentAdapter);

    void setContentAdapterNoPhoto(ContentAdapter.ContentViewHolder holder);

    void setContentAdpaterOnePhoto(ContentAdapter.ContentViewHolder holder,ArrayList<Photo> photoList);

    void setContentAdapterTwoPhoto(ContentAdapter.ContentViewHolder holder,ArrayList<Photo> photoList);

    void setContentAdapterMultiPhoto(ContentAdapter.ContentViewHolder holder,ArrayList<Photo> photoList, StoryInfo storyInfo);

    void setContentAdapterLikeCount(ContentAdapter.ContentViewHolder holder,String count);

    void setContentAdapterCommentCount(ContentAdapter.ContentViewHolder holder,String count);

    void setContentAdapterLikeIsChecked(ContentAdapter.ContentViewHolder holder,int position);

    void setContentAdapterLikeIsNotChecked(ContentAdapter.ContentViewHolder holder,int position);

    void setContentAdapterLikeCheck(ContentAdapter.ContentViewHolder holder,int position);

    void setContentAdapterLikeUncheck(ContentAdapter.ContentViewHolder holder,int position);

    void setContentAdapterContentAdd(StoryInfo storyInfo);

    void setContentAdapterContentChange(int position, String content, Boolean likeCheck);

    void setContentAdapterContentDelete(int position);


    ContentAdapter getContentAdapter();

    View getDecorView();

    void setMenu(View view);

    void setToolbar(View view);

    void showContentAdapterNotifyItemDelete(int position);

    void showContentAdapterNotifyItemChanged(int position);

    void showContentAdapterNotifyItemInserted(int position);


    void showEditFamilyButton();

    void goneEditFamilyButton();

    void showProgressDialog();

    void goneProgressDialog();

    void showToolbarTitle(String message);

    void showMenuUserInfo(User user);

    void showMenuUserBirth(String birth);


    void showMessage(String message);

    void navigateToBack();

    void navigateToStoryDetailActivity(StoryInfo storyInfo);

    void navigateToUserActivity(User user);

    void navigateToSearchUserActivity(Family family);

    void navigateToPhotosActivity(Family family);

    void navigateToCreateStoryActivity(Family family);

    void navigateToEditFamilyActivity(Family family);

    void navigateToPhotoPopupActivity(Family family);

    void navigateToMainActivity();

    void navigateToMarketMainActivity();

    void navigateToManageFamilyActivity();

    void navigateToSettingActivity();

    void navigateToLoginActivity();

    void navigateToSongMainActivity();


    void navigateToAppGame(String appPackageName);

}
