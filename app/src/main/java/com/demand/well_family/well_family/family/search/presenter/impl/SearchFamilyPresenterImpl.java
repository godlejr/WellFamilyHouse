package com.demand.well_family.well_family.family.search.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.search.adapter.FamilyAdapter;
import com.demand.well_family.well_family.family.search.interactor.SearchFamilyInteractor;
import com.demand.well_family.well_family.family.search.interactor.impl.SearchFamilyInteractorImpl;
import com.demand.well_family.well_family.family.search.presenter.SearchFamilyPresenter;
import com.demand.well_family.well_family.family.search.view.SearchFamilyView;
import com.demand.well_family.well_family.flag.FamilyJoinFlag;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-24.
 */

public class SearchFamilyPresenterImpl implements SearchFamilyPresenter {
    private SearchFamilyInteractor searchFamilyInteractor;
    private SearchFamilyView searchFamilyView;
    private PreferenceUtil preferenceUtil;

    public SearchFamilyPresenterImpl(Context context) {
        this.searchFamilyInteractor = new SearchFamilyInteractorImpl(this);
        this.searchFamilyView = (SearchFamilyView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        searchFamilyView.init();

        View decorView =searchFamilyView.getDecorView();
        searchFamilyView.setToolbar(decorView);
        searchFamilyView.showToolbarTitle("가족 찾기");
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            searchFamilyView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            searchFamilyView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onClickSetFamilySearched(String keyword) {
        if (keyword.length() != 0) {
            User user = preferenceUtil.getUserInfo();
            searchFamilyInteractor.setFamilySearched(user, keyword);
        } else {
            searchFamilyView.showMessage("검색어를 입력해주세요.");
        }

    }

    @Override
    public void onSuccessSetFamilySearched(ArrayList<FamilyInfoForFamilyJoin> familyList) {
        searchFamilyView.setFamilyAdapter(familyList);
    }

    @Override
    public void onSuccessSetUserJoinFamily(FamilyAdapter.FamilyViewHolder holder) {
        searchFamilyView.showMessage("가입 신청이 완료되었습니다.");
        searchFamilyView.setFamilyStateButtonForStay(holder);
    }

    @Override
    public void setFamilyStateButton(FamilyAdapter.FamilyViewHolder holder, FamilyInfoForFamilyJoin family, int joinFlag) {
        User user = preferenceUtil.getUserInfo();
        int userId = user.getId();

        if (joinFlag == FamilyJoinFlag.FAMILY || userId == family.getUser_id()) {
            searchFamilyView.setFamilyStateButtonForMe(holder);
        }

        if (joinFlag == FamilyJoinFlag.USER_TO_FAMILY) {
            searchFamilyView.setFamilyStateButtonForStay(holder);
        }

        if (joinFlag == FamilyJoinFlag.USERS) {
            searchFamilyView.setFamilyStateButtonForJoin(holder, family);
            family.setJoin_flag(FamilyJoinFlag.USER_TO_FAMILY);
        }

    }

    @Override
    public void onClickSetUserJoinFamily(FamilyInfoForFamilyJoin familyFound , FamilyAdapter.FamilyViewHolder holder) {
        User user = preferenceUtil.getUserInfo();
        searchFamilyInteractor.setUserJoinFamily(familyFound, user, holder);
    }
}
