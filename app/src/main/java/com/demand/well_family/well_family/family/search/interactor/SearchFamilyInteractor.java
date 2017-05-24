package com.demand.well_family.well_family.family.search.interactor;

import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.search.adapter.FamilyAdapter;

/**
 * Created by ㅇㅇ on 2017-05-24.
 */

public interface SearchFamilyInteractor {
    void setFamilySearched(User user, String keyword);

    void setUserJoinFamily(FamilyInfoForFamilyJoin family, User user, FamilyAdapter.FamilyViewHolder holder);
}
