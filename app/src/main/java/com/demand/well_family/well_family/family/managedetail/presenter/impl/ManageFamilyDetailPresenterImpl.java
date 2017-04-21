package com.demand.well_family.well_family.family.managedetail.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.family.managedetail.adapter.UserForFamilyJoinAdapter;
import com.demand.well_family.well_family.family.managedetail.interactor.ManageFamilyDetailInteractor;
import com.demand.well_family.well_family.family.managedetail.interactor.impl.ManageFamilyDetailInteractorImpl;
import com.demand.well_family.well_family.family.managedetail.presenter.ManageFamilyDetailPresenter;
import com.demand.well_family.well_family.family.managedetail.view.ManageFamilyDetailView;
import com.demand.well_family.well_family.flag.FamilyJoinFlag;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public class ManageFamilyDetailPresenterImpl implements ManageFamilyDetailPresenter {
    private ManageFamilyDetailView manageFamilyDetailView;
    private ManageFamilyDetailInteractor manageFamilyDetailInteractor;
    private PreferenceUtil preferenceUtil;


    public ManageFamilyDetailPresenterImpl(Context context) {
        this.manageFamilyDetailView = (ManageFamilyDetailView) context;
        this.manageFamilyDetailInteractor = new ManageFamilyDetailInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(Family family, boolean notificationFlag) {
        User user = preferenceUtil.getUserInfo();

        manageFamilyDetailView.init(family);
        manageFamilyDetailInteractor.setUser(user);
        manageFamilyDetailInteractor.setFamily(family);
        manageFamilyDetailInteractor.setNotificationFlag(notificationFlag);

        //toolbar
        View decorView = manageFamilyDetailView.getDecorView();
        manageFamilyDetailView.setToolbar(decorView);
        manageFamilyDetailView.showToolbarTitle("가족 설정");

        getUserDataForFamilyJoin();
    }

    @Override
    public void onClickFamilyDelete() {
        Family family = manageFamilyDetailInteractor.getFamily();
        manageFamilyDetailView.navigateToFamilyPopupActivityForFamilyDelete(family);
    }

    @Override
    public void getUserDataForFamilyJoin() {
        manageFamilyDetailInteractor.getUserDataForFamilyJoin();
    }


    @Override
    public void onSuccessGetUserDataForFamilyJoin(ArrayList<UserInfoForFamilyJoin> userList) {
        manageFamilyDetailView.setUserForFamilyJoinItem(userList);
    }

    @Override
    public void onLoadUserForFamilyJoin(UserForFamilyJoinAdapter.UserForFamilyJoinViewHolder holder, UserInfoForFamilyJoin userInfoForFamilyJoin) {
        int joinFlag = userInfoForFamilyJoin.getJoin_flag();

        if (joinFlag == FamilyJoinFlag.USER_TO_FAMILY) {
            manageFamilyDetailView.setUserForFamilyJoinAdapterAgree(holder, "가입 승인");
        }

        if (joinFlag == FamilyJoinFlag.FAMILY_TO_USER) {
            manageFamilyDetailView.setUserForFamilyJoinAdapterHold(holder, "요청 대기");
        }
    }

    @Override
    public void onClickFamilyJoin(UserInfoForFamilyJoin userInfoForFamilyJoin) {
        int joinFlag = userInfoForFamilyJoin.getJoin_flag();

        if (joinFlag == FamilyJoinFlag.USER_TO_FAMILY) {
            Family family = manageFamilyDetailInteractor.getFamily();
            manageFamilyDetailView.navigateToFamilyPopupActivityForFamilyJoin(family, userInfoForFamilyJoin);
        }
    }

    @Override
    public void onBackPressed() {
        Family family = manageFamilyDetailInteractor.getFamily();
        boolean notificationFlag = manageFamilyDetailInteractor.getNotificationFlag();

        if (notificationFlag) {
            manageFamilyDetailView.navigateToBack();
        } else {
            manageFamilyDetailView.navigateToBackForResultCancel(family.getPosition());
        }
    }


    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            manageFamilyDetailView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            manageFamilyDetailView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onActivityResultForPopupJoin(int position) {
        manageFamilyDetailView.setUserForFamilyJoinAdapterDelete(position);
        manageFamilyDetailView.showUserForFamilyJoinAdapterNotifyItemDelete(position);
        manageFamilyDetailView.showMessage("가입 요청을 승인하였습니다");
    }

    @Override
    public void onActivityResultForDeleteFamilyResult(int position) {
        boolean notificationFlag = manageFamilyDetailInteractor.getNotificationFlag();

        if (notificationFlag) {
            manageFamilyDetailView.navigateToBack();
        } else {
            manageFamilyDetailView.navigateToBackForFamilyDelete(position);
        }
    }
}
