package com.demand.well_family.well_family.family.manage.view;

import android.view.View;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.family.manage.adapter.member.FamilyForMemberAdapter;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-20.
 */

public interface ManageFamilyView {

    void init();

    void setToolbar(View view);

    void showToolbarTitle(String message);

    View getDecorView();

    void setFamilyForOwnerItem(ArrayList<Family> familyList);

    void setFamilyForMemberItem(ArrayList<FamilyInfoForFamilyJoin> familyInfoForFamilyJoinList);

    void setFamilyForMemberAdapterSecession(FamilyForMemberAdapter.FamilyForMemberViewHolder holder, String message);

    void setFamilyForMemberAdapterHold(FamilyForMemberAdapter.FamilyForMemberViewHolder holder, String message);

    void setFamilyForMemberAdapterAgree(FamilyForMemberAdapter.FamilyForMemberViewHolder holder, String message);

    void showFamilyForOwnerAdapterNotifyItemDelete(int position);

    void showFamilyForMemberAdapterNotifyItemDelete(int position);

    void showFamilyForMemberAdapterNotifyItemChange(int position);

    void setFamilyForOwnerAdapterDelete(int position);

    void setFamilyForMemberAdapterDelete(int position);

    void setFamilyForMemberAdapterChangeForJoinFlag(int position, int joinFlag);

    void showMessage(String message);


    void navigateToBack();

    void navigateToSearchFamilyActivity();

    void navigateToSearchFamilyPopupActivity(FamilyInfoForFamilyJoin familyInfoForFamilyJoin);

    void navigateToManageFamilyDetailActivity(Family family);


}
