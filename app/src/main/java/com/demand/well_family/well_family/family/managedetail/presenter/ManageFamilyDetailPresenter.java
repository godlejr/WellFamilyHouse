package com.demand.well_family.well_family.family.managedetail.presenter;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.family.managedetail.adapter.UserForFamilyJoinAdapter;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public interface ManageFamilyDetailPresenter {

    void onCreate(Family family, boolean notificationFlag);

    void onClickFamilyDelete();

    void getUserDataForFamilyJoin();


    void onSuccessGetUserDataForFamilyJoin(ArrayList<UserInfoForFamilyJoin> userList);

    void onLoadUserForFamilyJoin(UserForFamilyJoinAdapter.UserForFamilyJoinViewHolder holder, UserInfoForFamilyJoin userInfoForFamilyJoin);

    void onClickFamilyJoin(UserInfoForFamilyJoin userInfoForFamilyJoin);

    void onBackPressed();

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onActivityResultForPopupJoin(int position);

    void onActivityResultForDeleteFamilyResult(int position);

}
