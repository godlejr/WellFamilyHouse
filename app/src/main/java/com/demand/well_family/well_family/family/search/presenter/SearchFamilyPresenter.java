package com.demand.well_family.well_family.family.search.presenter;

import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.family.search.adapter.FamilyAdapter;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-24.
 */

public interface SearchFamilyPresenter {
    void onCreate();

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onClickSetFamilySearched(String keyword);

    void onSuccessSetFamilySearched(ArrayList<FamilyInfoForFamilyJoin> familyList);

    void onSuccessSetUserJoinFamily(FamilyAdapter.FamilyViewHolder holder);

    void setFamilyStateButton(FamilyAdapter.FamilyViewHolder holder, FamilyInfoForFamilyJoin familyFound, int joinFlag);

    void onClickSetUserJoinFamily(FamilyInfoForFamilyJoin familyFound, FamilyAdapter.FamilyViewHolder holder);
}
