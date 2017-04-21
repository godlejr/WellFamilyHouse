package com.demand.well_family.well_family.family.manage.presenter;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.family.manage.adapter.member.FamilyForMemberAdapter;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-20.
 */

public interface ManageFamilyPresenter {
    void onCreate();

    void getFamilyDataForOwner();

    void getFamilyDataForMember();

    void onLoadFamilyForMember(FamilyForMemberAdapter.FamilyForMemberViewHolder holder, FamilyInfoForFamilyJoin familyInfoForFamilyJoin);

    void onSuccessGetFamilyDataForOwner(ArrayList<Family> familyList);

    void onSuccessGetFamilyDataForMember(ArrayList<FamilyInfoForFamilyJoin> familyInfoForFamilyJoinList);

    void onClickFamilyForOwner(Family family);

    void onClickFamilyJoin(FamilyInfoForFamilyJoin familyInfoForFamilyJoin);


    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onActivityResultForPopupDeleteUserToFamily(int position);

    void onActivityResultForPopupJoin(int position);

    void onActivityResultForDeleteFamilyResult(int position);


}
