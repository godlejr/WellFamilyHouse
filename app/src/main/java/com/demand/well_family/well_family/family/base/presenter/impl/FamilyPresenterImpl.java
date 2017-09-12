package com.demand.well_family.well_family.family.base.presenter.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.base.adapter.content.ContentAdapter;
import com.demand.well_family.well_family.family.base.interactor.FamilyInteractor;
import com.demand.well_family.well_family.family.base.interactor.impl.FamilyInteractorImpl;
import com.demand.well_family.well_family.family.base.presenter.FamilyPresenter;
import com.demand.well_family.well_family.family.base.view.FamilyView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-18.
 */

public class FamilyPresenterImpl implements FamilyPresenter {
    private FamilyView familyView;
    private FamilyInteractor familyInteractor;
    private PreferenceUtil preferenceUtil;

    private MainHandler mainHandler;
    private ContentChangeHandler contentChangeHandler;
    private ContentAddHandler contentAddHandler;

    public FamilyPresenterImpl(Context context) {
        this.familyView = (FamilyView) context;
        this.familyInteractor = new FamilyInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(Family family, boolean notificationFlag) {
        User user = preferenceUtil.getUserInfo();

        familyInteractor.setNotificationFlag(notificationFlag);
        familyInteractor.setUser(user);
        familyInteractor.setFamily(family);
        familyInteractor.getFamilyDeleteCheck();
    }

    @Override
    public String validateUserIdentification(User userOfList) {
        String message = null;
        User user = familyInteractor.getUser();

        if (user.getId() == userOfList.getId()) {
            message = "나";
        } else {
            message = userOfList.getName();
        }

        return message;
    }

    @Override
    public void setToolbarAndMenu(String familyName) {
        User user = preferenceUtil.getUserInfo();
        View decorView = familyView.getDecorView();

        familyView.setToolbar(decorView);
        familyView.setMenu(decorView);

        familyView.showToolbarTitle(familyName);

        familyView.showMenuUserInfo(user);
    }

    @Override
    public void setUserBirth(String birth) {
        String date = familyInteractor.getUserBirth(birth);
        familyView.showMenuUserBirth(date);
    }

    @Override
    public void onLoadContent(ContentAdapter.ContentViewHolder holder, StoryInfo storyInfo) {
        //photo
        familyInteractor.getContentPhotoData(holder, storyInfo);
        //likeCount
        familyInteractor.getContentLikeCount(holder, storyInfo);
        //commentCount
        familyInteractor.getContentCommentCount(holder, storyInfo);
        //likeCheck
        familyInteractor.getContentLikeCheck(holder, storyInfo);

    }

    @Override
public void onSuccessSetThreadContentAdd(StoryInfo storyInfo) {
        familyView.setContentAdapterContentAdd(storyInfo);

        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("item_id", 0);
        message.setData(bundle);

        contentAddHandler.sendMessage(message);
    }

    @Override
    public void onSuccessGetContentUser(User user) {
        if (user == null) {
            //data parsing error
        } else {
            familyView.navigateToUserActivity(user);
        }
    }

    @Override
    public void onSuccessSetContentLikeCheck(ContentAdapter.ContentViewHolder holder, int position) {
        familyView.setContentAdapterLikeCheck(holder, position);
    }

    @Override
    public void onSuccessSetContentLikeUncheck(ContentAdapter.ContentViewHolder holder, int position) {
        familyView.setContentAdapterLikeUncheck(holder, position);
    }

    @Override
    public void onSuccessGetContentLikeCheck(ContentAdapter.ContentViewHolder holder, int check, int position) {
        if (check == 1) {
            familyView.setContentAdapterLikeIsChecked(holder, position);
        } else {
            familyView.setContentAdapterLikeIsNotChecked(holder, position);
        }
    }

    @Override
    public void onSuccessGetContentCommentCount(ContentAdapter.ContentViewHolder holder, int count) {
        familyView.setContentAdapterCommentCount(holder, String.valueOf(count));
    }

    @Override
    public void onSuccessGetContentLikeCount(ContentAdapter.ContentViewHolder holder, int count) {
        familyView.setContentAdapterLikeCount(holder, String.valueOf(count));
    }

    @Override
    public void onSuccessGetContentPhotoData(ContentAdapter.ContentViewHolder holder, ArrayList<Photo> photoList, StoryInfo storyInfo) {
        int photoSize = photoList.size();

        if (photoSize == 0) {
            familyView.setContentAdapterNoPhoto(holder);
        }

        if (photoSize == 1) {
            familyView.setContentAdpaterOnePhoto(holder, photoList);
        }

        if (photoSize == 2) {
            familyView.setContentAdapterTwoPhoto(holder, photoList);
        }

        if (photoSize > 2) {
            familyView.setContentAdapterMultiPhoto(holder, photoList, storyInfo);
        }
    }


    @Override
    public void onSuccessGetContentData() {
        ArrayList<StoryInfo> storyList = familyInteractor.getStoryListWithOffset();
        familyView.setContentAdapterInit(storyList);

        Bundle bundle = new Bundle();
        bundle.putSerializable("contentAdapter", familyView.getContentAdapter());

        Message message = new Message();
        message.setData(bundle);
        mainHandler.sendMessage(message);

        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                familyView.goneProgressDialog();
            }
        }, 200);
    }

    @Override
    public void onSuccessGetUserData(ArrayList<User> tempUserList) {
        User user = familyInteractor.getUser();
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User(user.getId(), user.getEmail(), user.getName(), user.getBirth(), user.getPhone(), user.getAvatar(), user.getLevel()));

        int userSize = tempUserList.size();
        if (userSize == 0) {

        } else {
            for (int i = 0; i < userSize; i++) {
                userList.add(new User(tempUserList.get(i).getId(), tempUserList.get(i).getEmail(), tempUserList.get(i).getName(),
                        tempUserList.get(i).getBirth(), tempUserList.get(i).getPhone(), tempUserList.get(i).getAvatar(), tempUserList.get(i).getLevel()));
            }
        }
        familyView.setUserItem(userList);
    }

    @Override
    public void onSuccessFamilyDeleteCheck(Family family) {
        if (family == null) {
            familyView.showMessage("삭제된 가족입니다.");
            familyView.navigateToMainActivity();
        } else {
            User user = familyInteractor.getUser();
            familyView.init(user, family);
            familyInteractor.getUserData();

            mainHandler = new MainHandler();
            familyView.showProgressDialog();
            familyInteractor.getContentData();
        }
    }

    @Override
    public void onSuccessGetContentDataAdded(int position) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("item_id", position);
        message.setData(bundle);
        contentChangeHandler.sendMessage(message);
    }

    @Override
    public void onSuccessThreadRun() {
        familyView.goneProgressDialog();
    }

    @Override
    public void onGettingContentDataAdded() {
        familyView.showProgressDialog();
        contentChangeHandler = new ContentChangeHandler();
    }

    @Override
    public void onScrollChange(int difference) {
        if (difference <= 0) {
            familyInteractor.getContentDataAdded();
        }
    }

    @Override
    public void onCheckedChangeForLike(ContentAdapter.ContentViewHolder holder, StoryInfo storyInfo, boolean isFirstChecked, boolean isChecked) {
        if (isFirstChecked) {
            if (isChecked) {
                familyInteractor.setContentLikeCheck(holder, storyInfo);
            } else {
                familyInteractor.setContentLikeUncheck(holder, storyInfo);
            }
        }
    }

    @Override
    public void validateFamilyCreatorForEdit(int userId, int familyUserId) {
        if (userId == familyUserId) {
            familyView.showEditFamilyButton();
        } else {
            familyView.goneEditFamilyButton();
        }
    }

    @Override
    public void onClickContentBody(StoryInfo storyInfo) {
        familyView.navigateToStoryDetailActivity(storyInfo);
    }

    @Override
    public void onClickContentPhoto(StoryInfo storyInfo) {
        familyView.navigateToStoryDetailActivity(storyInfo);
    }

    @Override
    public void onClickContentUser(int contentUserId) {
        familyInteractor.getContentUser(contentUserId);
    }

    @Override
    public void onClickBack() {
        if (familyInteractor.getNotificationFlag()) {
            familyView.navigateToBack();
        } else {
            familyView.navigateToMainActivity();
        }
    }


    @Override
    public void onClickUser() {
        User user = preferenceUtil.getUserInfo();
        familyView.navigateToUserActivity(user);
    }

    @Override
    public void onClickUser(User user) {
        familyView.navigateToUserActivity(user);
    }

    @Override
    public void onClickLogout() {
        preferenceUtil.removeUserInfo();
        familyView.navigateToLoginActivity();
    }

    @Override
    public void onClickSongMain() {
        familyView.navigateToSongMainActivity();
    }

    @Override
    public void onClickAppGames(String appPackageName) {
        familyView.navigateToAppGame(appPackageName);
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            familyView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            familyView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onActivityResultForWriteResultOk(StoryInfo storyInfo) {
        familyView.showProgressDialog();
        contentAddHandler = new ContentAddHandler();
        familyInteractor.setThreadContentAdd(storyInfo);
    }

    @Override
    public void onActivityResultForStoryDetailResultOk(int position, String content, Boolean likeCheck) {
        familyView.setContentAdapterContentChange(position, content, likeCheck);
        familyView.showContentAdapterNotifyItemChanged(position);
    }

    @Override
    public void onActivityResultForStoryDetailDelete(int position) {
        familyView.setContentAdapterContentDelete(position);
        familyView.showContentAdapterNotifyItemDelete(position);
    }

    @Override
    public void onActivityResultForEditFamilyResultOk(String familyName, String familyContent, String familyAvatar) {
        User user = familyInteractor.getUser();
        Family family = familyInteractor.getFamily();

        family.setName(familyName);
        family.setContent(familyContent);
        family.setAvatar(familyAvatar);

        //initialization
        familyView.init(user, family);
    }

    @Override
    public void onClickExercise() {
        familyView.navigateToExerciseActivity();
    }

    @Override
    public void onClickFallDiagnosis() {
        familyView.navigateToFallDiagnosisActivity();
    }


    public class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            ContentAdapter contentAdapter = (ContentAdapter) bundle.getSerializable("contentAdapter");

            familyView.setContentAdapter(contentAdapter);
        }
    }


    public class ContentChangeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int position = bundle.getInt("item_id");

            familyView.showContentAdapterNotifyItemChanged(position);
        }
    }

    public class ContentAddHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int position = bundle.getInt("item_id");

            familyView.showContentAdapterNotifyItemInserted(position);
        }
    }

}
