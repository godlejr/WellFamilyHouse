package com.demand.well_family.well_family.family.manage.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.manage.adapter.member.FamilyForMemberAdapter;
import com.demand.well_family.well_family.family.manage.interactor.ManageFamilyInteractor;
import com.demand.well_family.well_family.family.manage.interactor.impl.ManageFamilyInteractorImpl;
import com.demand.well_family.well_family.family.manage.presenter.ManageFamilyPresenter;
import com.demand.well_family.well_family.family.manage.view.ManageFamilyView;
import com.demand.well_family.well_family.flag.FamilyJoinFlag;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-20.
 */

public class ManageFamilyPresenterImpl implements ManageFamilyPresenter {
    private ManageFamilyView manageFamilyView;
    private ManageFamilyInteractor manageFamilyInteractor;
    private PreferenceUtil preferenceUtil;

    public ManageFamilyPresenterImpl(Context context) {
        this.manageFamilyView = (ManageFamilyView) context;
        this.manageFamilyInteractor = new ManageFamilyInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        User user = preferenceUtil.getUserInfo();
        manageFamilyView.init();
        manageFamilyInteractor.setUser(user);

        //toolbar
        View decorView = manageFamilyView.getDecorView();
        manageFamilyView.setToolbar(decorView);
        manageFamilyView.showToolbarTitle("가족 설정");

        getFamilyDataForOwner();
        getFamilyDataForMember();
    }

    @Override
    public void getFamilyDataForOwner() {
        manageFamilyInteractor.getFamilyDataForOwner();
    }

    @Override
    public void getFamilyDataForMember() {
        manageFamilyInteractor.getFamilyDataForMember();
    }

    @Override
    public void onLoadFamilyForMember(FamilyForMemberAdapter.FamilyForMemberViewHolder holder, FamilyInfoForFamilyJoin familyInfoForFamilyJoin) {
        int joinFlag = familyInfoForFamilyJoin.getJoin_flag();

        if (joinFlag == FamilyJoinFlag.FAMILY) {
            manageFamilyView.setFamilyForMemberAdapterSecession(holder, "탈퇴 하기");
        }

        if (joinFlag == FamilyJoinFlag.USER_TO_FAMILY) {
            manageFamilyView.setFamilyForMemberAdapterHold(holder, "승인 대기");
        }

        if (joinFlag == FamilyJoinFlag.FAMILY_TO_USER) {
            manageFamilyView.setFamilyForMemberAdapterAgree(holder, "초대 승인");
        }
    }

    @Override
    public void onSuccessGetFamilyDataForOwner(ArrayList<Family> familyList) {
        manageFamilyView.setFamilyForOwnerItem(familyList);
    }

    @Override
    public void onSuccessGetFamilyDataForMember(ArrayList<FamilyInfoForFamilyJoin> familyInfoForFamilyJoinList) {
        manageFamilyView.setFamilyForMemberItem(familyInfoForFamilyJoinList);
    }

    @Override
    public void onClickFamilyForOwner(Family family) {
        manageFamilyView.navigateToManageFamilyDetailActivity(family);
    }

    @Override
    public void onClickFamilyJoin(FamilyInfoForFamilyJoin familyInfoForFamilyJoin) {
        int joinFlag = familyInfoForFamilyJoin.getJoin_flag();

        if (joinFlag == FamilyJoinFlag.FAMILY || joinFlag == FamilyJoinFlag.FAMILY_TO_USER) {
            manageFamilyView.navigateToSearchFamilyPopupActivity(familyInfoForFamilyJoin);
        }

        if (joinFlag == FamilyJoinFlag.USER_TO_FAMILY) {
            // ready
        }
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            manageFamilyView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            manageFamilyView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onActivityResultForPopupDeleteUserToFamily(int position) {
        manageFamilyView.setFamilyForMemberAdapterDelete(position);
        manageFamilyView.showFamilyForMemberAdapterNotifyItemDelete(position);
    }

    @Override
    public void onActivityResultForPopupJoin(int position) {
        manageFamilyView.setFamilyForMemberAdapterChangeForJoinFlag(position, FamilyJoinFlag.FAMILY);
        manageFamilyView.showFamilyForMemberAdapterNotifyItemChange(position);
    }

    @Override
    public void onActivityResultForDeleteFamilyResult(int position) {
        manageFamilyView.setFamilyForOwnerAdapterDelete(position);
        manageFamilyView.showFamilyForOwnerAdapterNotifyItemDelete(position);
    }
}
