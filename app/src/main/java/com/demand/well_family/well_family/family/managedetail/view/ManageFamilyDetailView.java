package com.demand.well_family.well_family.family.managedetail.view;

import android.view.View;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.family.managedetail.adapter.UserForFamilyJoinAdapter;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public interface ManageFamilyDetailView {

    void init(Family family);

    void setToolbar(View view);

    void showToolbarTitle(String message);

    View getDecorView();

    void setUserForFamilyJoinItem(ArrayList<UserInfoForFamilyJoin> userList);

    void setUserForFamilyJoinAdapterAgree(UserForFamilyJoinAdapter.UserForFamilyJoinViewHolder holder, String message);

    void setUserForFamilyJoinAdapterHold(UserForFamilyJoinAdapter.UserForFamilyJoinViewHolder holder, String message);

    void showMessage(String message);

    void navigateToBack();

    void navigateToBackForResultCancel(int position);

    void navigateToBackForFamilyDelete(int position);

    void setUserForFamilyJoinAdapterDelete(int position);

    void showUserForFamilyJoinAdapterNotifyItemDelete(int position);


    void navigateToFamilyPopupActivityForFamilyDelete(Family family);

    void navigateToFamilyPopupActivityForFamilyJoin(Family family, UserInfoForFamilyJoin userInfoForFamilyJoin);


}
