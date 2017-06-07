package com.demand.well_family.well_family.users.base.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.users.base.interactor.UserInteractor;
import com.demand.well_family.well_family.users.base.interactor.impl.UserInteractorImpl;
import com.demand.well_family.well_family.users.base.presenter.UserPresenter;
import com.demand.well_family.well_family.users.base.view.UserView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by Dev-0 on 2017-05-24.
 */

public class UserPresenterImpl implements UserPresenter {
    private UserView userView;
    private UserInteractor userInteractor;
    private PreferenceUtil preferenceUtil;

    public UserPresenterImpl(Context context) {
        this.userView = (UserView) context;
        this.userInteractor = new UserInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(User storyUser) {
        User user = preferenceUtil.getUserInfo();
        userInteractor.setUser(user);
        userInteractor.setStoryUser(storyUser);


        userView.init(user, storyUser);
        View decorView = userView.getDecorView();
        userView.setToolbar(decorView);
        userView.showToolbarTitle("마이페이지");
    }

    @Override
    public void onClickSongStory() {
        User storyUser = userInteractor.getStoryUser();
        userView.navigateToSongStoryActivity(storyUser);
    }

    @Override
    public void onClickAvatar() {
        User storyUser = userInteractor.getStoryUser();
        userView.navigateToPhotoPopupActivity(storyUser);
    }

    @Override
    public void onLoadData() {
        User user = userInteractor.getUser();
        User storyUser = userInteractor.getStoryUser();

        int userId = user.getId();
        int storyUserId = storyUser.getId();

        if (userId == storyUserId) {
            userView.goneUserPhone();
        } else {
            userView.goneUserEdit();
        }

        userInteractor.getFamilyCheck();

    }

    @Override
    public void setUserBirth(String birth) {
        String date = userInteractor.getUserBirth(birth);
        userView.showUserBirth(date);
    }

    @Override
    public void setUserPhone(String phone) {
        String phoneWithHyphen = userInteractor.getUserPhoneWithHyphen(phone);
        userView.showUserPhone(phoneWithHyphen);

    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            userView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            userView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onSuccessGetFamilyCheck(int check) {
        if (check > 0) {
            userView.setPhoneOnClickListener();
        } else {
            userView.goneUserPhone();
            userView.goneFallDiagnosisStory();
        }
    }

    @Override
    public void onClickCall() {
        User storyUser = userInteractor.getStoryUser();
        userView.navigateToCall(storyUser.getPhone());
    }

    @Override
    public void onClickFallDiagnosisStory() {
        User storyUser = userInteractor.getStoryUser();
        userView.navigateToFallDiagnosisStoryActivity(storyUser);
    }
}
