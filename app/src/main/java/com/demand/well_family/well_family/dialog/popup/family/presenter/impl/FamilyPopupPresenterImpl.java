package com.demand.well_family.well_family.dialog.popup.family.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.family.interactor.FamilyPopupInteractor;
import com.demand.well_family.well_family.dialog.popup.family.interactor.impl.FamilyPopupInteractorImpl;
import com.demand.well_family.well_family.dialog.popup.family.presenter.FamilyPopupPresenter;
import com.demand.well_family.well_family.dialog.popup.family.view.FamilyPopupView;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.flag.FamilyJoinFlag;

import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public class FamilyPopupPresenterImpl implements FamilyPopupPresenter {
    private FamilyPopupInteractor familyPopupInteractor;
    private FamilyPopupView familyPopupView;
    private PreferenceUtil preferenceUtil;

    public FamilyPopupPresenterImpl(Context context) {
        this.familyPopupView = (FamilyPopupView) context;
        this.familyPopupInteractor = new FamilyPopupInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(Family family, UserInfoForFamilyJoin userInfoForFamilyJoin, boolean deleteFlag) {
        User user = preferenceUtil.getUserInfo();
        familyPopupInteractor.setUser(user);
        familyPopupInteractor.setFamily(family);
        familyPopupInteractor.setUserInfoForFamilyJoin(userInfoForFamilyJoin);
        familyPopupInteractor.setDeleteFlag(deleteFlag);

        familyPopupView.setDisplay();
        familyPopupView.init();
    }

    @Override
    public void onLoadData() {
        Family family = familyPopupInteractor.getFamily();
        UserInfoForFamilyJoin userInfoForFamilyJoin = familyPopupInteractor.getUserInfoForFamilyJoin();
        User user = familyPopupInteractor.getUser();
        boolean deleteFlag = familyPopupInteractor.getDeleteFlag();

        String familyName = family.getName();
        String userName = user.getName();
        int joinFlag = userInfoForFamilyJoin.getJoin_flag();
        String joinerName = userInfoForFamilyJoin.getName();

        familyPopupView.setPopupFamilyAvatar(family);

        if (joinFlag == FamilyJoinFlag.FAMILY_TO_USER) {
            familyPopupView.setPopupTitle("가족 가입");
            familyPopupView.setPopupContent("\'" + familyName + "\' 가족이 " + userName + "님을 \n가족으로 초대하였습니다.");
            familyPopupView.setPopupButtonText("수락", "거절");
            familyPopupView.setPopupButtonBackground(R.drawable.round_corner_green_r10);
        }

        if (joinFlag == FamilyJoinFlag.FAMILY) {
            familyPopupView.setPopupTitle("가족 탈퇴");
            familyPopupView.setPopupContent("가족 페이지를 탈퇴할 경우, \n\'" + familyName + "\' 가족과 함께 나누었던 소중한 추억들을 \n확인하실 수 없습니다.\n그래도 \'" + familyName + "\' 가족 페이지를 탈퇴하시겠습니까?");
            familyPopupView.setPopupButtonText("탈퇴", "취소");
            familyPopupView.setPopupButtonBackground(R.drawable.round_corner_red_r10);
        }

        if (joinFlag == FamilyJoinFlag.USER_TO_FAMILY) {
            familyPopupView.setPopupTitle("가족 승인하기");
            familyPopupView.setPopupContent(joinerName + "회원님이 \'" + familyName + "\' 가족의 구성원이 되고싶어합니다.");
            familyPopupView.setPopupButtonText("수락", "거절");
            familyPopupView.setPopupButtonBackground(R.drawable.round_corner_green_r10);
        }
        if (deleteFlag) {
            familyPopupView.setPopupTitle("가족 삭제");
            familyPopupView.setPopupContent("가족 페이지를 삭제할 경우, \n\'" + familyName + "\' 가족과 함께 나누었던 소중한 추억들이 \n모두 삭제됩니다.\n그래도 \"" + familyName + "\" 가족 페이지를 삭제하시겠습니까?");
            familyPopupView.setPopupButtonText("삭제", "취소");
            familyPopupView.setPopupButtonBackground(R.drawable.round_corner_red_r10);
        }

    }

    @Override
    public void onSuccessAcceptInvitation() {
        Family family = familyPopupInteractor.getFamily();
        String familyName = family.getName();

        familyPopupView.showMessage("\"" + familyName + "\" 에 가입되었습니다.");
        familyPopupView.navigateToBackAfterAcceptInvitation(family);
    }

    @Override
    public void onSuccessFamilySecession() {
        UserInfoForFamilyJoin userInfoForFamilyJoin = familyPopupInteractor.getUserInfoForFamilyJoin();
        Family family = familyPopupInteractor.getFamily();

        int joinFlag = userInfoForFamilyJoin.getJoin_flag();
        String familyName = family.getName();

        if (joinFlag == FamilyJoinFlag.FAMILY) {
            familyPopupView.showMessage("\"" + familyName + "\" 에서 탈퇴하였습니다.");
        }

        if (joinFlag == FamilyJoinFlag.FAMILY_TO_USER) {
            familyPopupView.showMessage("\"" + familyName + "\" 의 초대를 거절하였습니다.");
        }

        familyPopupView.navigateToBackAfterSecessionAndDelete(family);
    }

    @Override
    public void onSuccessAcceptRequest() {
        UserInfoForFamilyJoin userInfoForFamilyJoin = familyPopupInteractor.getUserInfoForFamilyJoin();
        familyPopupView.navigateToBackAfterAcceptRequest(userInfoForFamilyJoin);
    }

    @Override
    public void onSuccessDeleteFamily() {
        Family family = familyPopupInteractor.getFamily();
        String familyName = family.getName();
        familyPopupView.showMessage("\"" + familyName + "\" 이 삭제되었습니다.");

        familyPopupView.navigateToBackAfterSecessionAndDelete(family);
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            familyPopupView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            familyPopupView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void setPopupButtonBackground(int resId) {
        familyPopupView.setPopupButtonBackground(resId);
    }

    @Override
    public void onClickCommit() {
        UserInfoForFamilyJoin userInfoForFamilyJoin = familyPopupInteractor.getUserInfoForFamilyJoin();
        boolean deleteFlag = familyPopupInteractor.getDeleteFlag();
        int joinFlag = userInfoForFamilyJoin.getJoin_flag();

        if (joinFlag == FamilyJoinFlag.FAMILY_TO_USER) {
            familyPopupInteractor.setAcceptInvitation();
        }

        if (joinFlag == FamilyJoinFlag.FAMILY) {
            familyPopupInteractor.setFamilySecession();
        }

        if (joinFlag == FamilyJoinFlag.USER_TO_FAMILY) {
            familyPopupInteractor.setAcceptRequest();
        }

        if (deleteFlag) {
            familyPopupInteractor.setDeleteFamily();
        }

        familyPopupView.setButtonUnClickable();

    }

    @Override
    public void onClickClose() {
        UserInfoForFamilyJoin userInfoForFamilyJoin = familyPopupInteractor.getUserInfoForFamilyJoin();
        int joinFlag = userInfoForFamilyJoin.getJoin_flag();

        if (joinFlag == FamilyJoinFlag.FAMILY_TO_USER) { //reject
            familyPopupInteractor.setFamilySecession();
        } else {
            familyPopupView.navigateToBack();
        }
    }

}
