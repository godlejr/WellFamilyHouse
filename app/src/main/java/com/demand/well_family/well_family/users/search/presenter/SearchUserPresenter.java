package com.demand.well_family.well_family.users.search.presenter;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.users.search.adapter.UserAdapter;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface SearchUserPresenter {
    void onCreate(Family family);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void setUserStateButton(UserAdapter.UserViewHolder holder, UserInfoForFamilyJoin userFound, int joinFlag);

    void onClickBack();

    void onClickSetUserSearched(String searchKeyword);

    void onClickSetUserJoinFamily(UserInfoForFamilyJoin userFound);

    void onClickSetUserInvited(UserInfoForFamilyJoin userFound, UserAdapter.UserViewHolder holder);


    void onSuccessSetUserSearched(ArrayList<UserInfoForFamilyJoin> userList);

    void onSuccessSetUserJoinFamily(UserInfoForFamilyJoin userFound);

    void onSuccessSetUserInvited(UserInfoForFamilyJoin userFound, UserAdapter.UserViewHolder holder);
}
