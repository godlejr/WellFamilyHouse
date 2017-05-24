package com.demand.well_family.well_family.users.search.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.flag.FamilyJoinFlag;
import com.demand.well_family.well_family.users.search.adapter.UserAdapter;
import com.demand.well_family.well_family.users.search.interactor.SearchUserInteractor;
import com.demand.well_family.well_family.users.search.interactor.impl.SearchUserInteractorImpl;
import com.demand.well_family.well_family.users.search.presenter.SearchUserPresenter;
import com.demand.well_family.well_family.users.search.view.SearchUserView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public class SearchUserPresenterImpl implements SearchUserPresenter {
    private SearchUserInteractor searchUserInteractor;
    private SearchUserView searchUserView;
    private PreferenceUtil preferenceUtil;

    public SearchUserPresenterImpl(Context context) {
        this.searchUserInteractor = new SearchUserInteractorImpl(this);
        this.searchUserView = (SearchUserView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(Family family) {
        searchUserInteractor.setFamily(family);
        searchUserView.init();

        View decorView = searchUserView.getDecorView();
        searchUserView.setToolbar(decorView);
        searchUserView.showToolbarTitle("가족 찾기");
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            searchUserView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            searchUserView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onClickBack() {
        Family family = searchUserInteractor.getFamily();
        searchUserView.navigateToBackWithFamily(family);
    }

    @Override
    public void onClickSetUserSearched(String searchKeyword) {
        if (searchKeyword.length() != 0) {
            User user = preferenceUtil.getUserInfo();
            Family family = searchUserInteractor.getFamily();
            searchUserInteractor.setUserSearched(user, family, searchKeyword);
        } else {
            searchUserView.showMessage("검색어를 입력해주세요.");
        }
    }

    @Override
    public void onClickSetUserJoinFamily(UserInfoForFamilyJoin userFound) {
        User user = preferenceUtil.getUserInfo();
        searchUserInteractor.setUserJoinFamily(user, userFound);
    }

    @Override
    public void onClickSetUserInvited(UserInfoForFamilyJoin userFound, UserAdapter.UserViewHolder holder) {
        User user = preferenceUtil.getUserInfo();
        searchUserInteractor.setUserInvited(user, userFound, holder);
    }

    @Override
    public void setUserStateButton(UserAdapter.UserViewHolder holder, UserInfoForFamilyJoin userFound, int joinFlag) {
        User user = preferenceUtil.getUserInfo();
        int userId = user.getId();

        if (joinFlag == FamilyJoinFlag.FAMILY) {
            searchUserView.setUserStateButtonForFamily(holder);
        }

        if (joinFlag == FamilyJoinFlag.USER_TO_FAMILY) {
            searchUserView.setUserStateButtonForJoin(holder, userFound);
        }

        if (joinFlag == FamilyJoinFlag.FAMILY_TO_USER) {
            searchUserView.setUserStateButtonForStay(holder);
        }

        if (userId == userFound.getId()) {
            searchUserView.setUserStateButtonForMe(holder);
        }

        if (joinFlag == FamilyJoinFlag.USERS) {
            //public
            searchUserView.setUserStateButtonForInvite(holder,userFound);
        }
    }

    @Override
    public void onSuccessSetUserSearched(ArrayList<UserInfoForFamilyJoin> userList) {
        searchUserView.setUserAdapterInit(userList);
    }

    @Override
    public void onSuccessSetUserJoinFamily(UserInfoForFamilyJoin userFound) {
        String userFoundName = userFound.getName();
        searchUserView.showMessage(userFoundName + "님의 요청을 승인하였습니다.");
    }

    @Override
    public void onSuccessSetUserInvited(UserInfoForFamilyJoin userFound, UserAdapter.UserViewHolder holder) {
        searchUserView.showMessage(userFound.getName() + "님을 가족으로 초대하였습니다.");
        searchUserView.setUserStateButtonForStay(holder);
    }


}
