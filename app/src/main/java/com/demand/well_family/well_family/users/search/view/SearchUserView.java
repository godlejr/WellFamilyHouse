package com.demand.well_family.well_family.users.search.view;

import android.view.View;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.users.search.adapter.UserAdapter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface SearchUserView {
    void init();

    void showMessage(String message);

    View getDecorView();

    void setToolbar(View decorView);

    void showToolbarTitle(String title);

    void setUserAdapterInit(ArrayList<UserInfoForFamilyJoin> userList);

    void navigateToBackWithFamily(Family family);

    void setUserStateButtonForFamily(UserAdapter.UserViewHolder holder);

    void setUserStateButtonForJoin(UserAdapter.UserViewHolder holder, UserInfoForFamilyJoin userFound);

    void setUserStateButtonForStay(UserAdapter.UserViewHolder holder);

    void setUserStateButtonForMe(UserAdapter.UserViewHolder holder);

    void setUserStateButtonForInvite(UserAdapter.UserViewHolder holder, UserInfoForFamilyJoin userFound);

}
